package com.bt.ecommerce.primary.service;

import com.bt.ecommerce.configuration.SpringBeanContext;
import com.bt.ecommerce.exception.BadRequestException;
import com.bt.ecommerce.messaging.FcmComponent;
import com.bt.ecommerce.messaging.FcmNotificationBean;
import com.bt.ecommerce.primary.dto.CartDto;
import com.bt.ecommerce.primary.mapper.AddressMapper;
import com.bt.ecommerce.primary.mapper.CartMapper;
import com.bt.ecommerce.primary.mapper.ItemMapper;
import com.bt.ecommerce.primary.pojo.*;
import com.bt.ecommerce.primary.pojo.enums.DiscountTypeEnum;
import com.bt.ecommerce.primary.pojo.enums.SettingEnum;
import com.bt.ecommerce.primary.pojo.user.Customer;
import com.bt.ecommerce.primary.pojo.user.SystemUser;
import com.bt.ecommerce.primary.repository.SequenceRepository;
import com.bt.ecommerce.security.JwtTokenUtil;
import com.bt.ecommerce.security.JwtUserDetailsService;
import com.bt.ecommerce.utils.TextUtils;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class CartService extends _BaseService {

    @Autowired
    private FcmComponent fcmComponent;

    public CartDto.DetailCart getCartDetail(String authorizationToken, String deviceId) throws BadRequestException {
        Cart cart = getCartByDeviceId(authorizationToken, deviceId);
        cart = cartPriceCalculation(cart);
        return CartMapper.MAPPER.mapToDetailCartDto(cart);
    }

    public CartDto.DetailCart updateCart(String authorizationToken, String deviceId, String uuid, CartDto.UpdateCart cartDto) throws BadRequestException {
        Cart cart = cartRepository.findByUuid(uuid);
        if (cart == null) {
            cart = createNewCart(authorizationToken, deviceId);
        }
        cart = mapToCartItem(cart, cartDto);
        cart = cartPriceCalculation(cart);
        cart = cartRepository.save(cart);
        // Return Cart Details
        return CartMapper.MAPPER.mapToDetailCartDto(cart);
    }

    public void clearCart(String cartUuid) {
        Cart cart = cartRepository.findByUuid(cartUuid);
        if (cart == null) {
            return;
        }
        cartRepository.delete(cart);
    }

    public CartDto.DetailCart removeItemFromCart(String cartUuid, String itemUuid) throws BadRequestException {
        Cart cart = cartRepository.findByUuid(cartUuid);
        if (cart == null) {
            throw new BadRequestException("There is no valid cart");
        }
        Item item = itemRepository.findByUuid(itemUuid);
        if (item == null) {
            throw new BadRequestException("Please provide a valid item");
        }
        if (cart.getItemIds().contains(item.getId())) {
            Cart.ItemDetail removedItemDetail = null;
            for (Cart.ItemDetail itemDetail : cart.getItemDetailList()) {
                if (itemDetail.getItemUuid().equals(itemUuid)) {
                    removedItemDetail = itemDetail;
                    break;
                }
            }
            // Item Removed
            cart.getItemIds().remove(item.getId());
            cart.getItemDetailList().remove(removedItemDetail);
        }
        cart = cartPriceCalculation(cart);
        cartRepository.save(cart);
        return CartMapper.MAPPER.mapToDetailCartDto(cart);
    }

    public CartDto.DetailCart updateItemQuantity(String cartUuid, String itemUuid, long quantity) throws BadRequestException {
        Cart cart = cartRepository.findByUuid(cartUuid);
        if (cart == null) {
            throw new BadRequestException("Please provide a valid cart");
        }
        cart.getItemDetailList().stream()
                .filter(x -> x.getItemUuid().equalsIgnoreCase(itemUuid)).findFirst().ifPresent(x -> x.setQuantity(quantity));
        cart = cartPriceCalculation(cart);
        cartRepository.save(cart);
        return CartMapper.MAPPER.mapToDetailCartDto(cart);
    }

    private Cart mapToCartItem(Cart cart, CartDto.UpdateCart cartDto) throws BadRequestException {
        if (cartDto.getItemQuantityMap() != null && !cartDto.getItemQuantityMap().isEmpty()) {
            List<ObjectId> itemIds = new ArrayList<>();
            List<Cart.ItemDetail> itemDetailList = new ArrayList<>();
            for (String itemUuid : cartDto.getItemQuantityMap().keySet()) {
                Item item = itemRepository.findByUuid(itemUuid);
                Long itemQuantity = cartDto.getItemQuantityMap().get(itemUuid);
                if (itemQuantity < 1) {
                    throw new BadRequestException("Please provide a valid item quantity");
                }
                if (item == null) {
                    throw new BadRequestException("Invalid item selection");
                }
                if (item.isDeleted() || !item.isActive()) {
                    throw new BadRequestException("Invalid item selection, Please select a active item");
                }
                // Map a new item To cart
                Cart.ItemDetail itemDetail = ItemMapper.MAPPER.mapToCartItem(item);
                itemDetail.setItemTotal(item.getSellingPrice());
                itemDetail.setQuantity(itemQuantity);
                itemIds.add(item.getId());
                itemDetailList.add(itemDetail);
            }
            cart.setItemIds(itemIds);
            cart.setItemDetailList(itemDetailList);
        }

        // To Add Item First Time in Cart
        if (cartDto.getItemUuid() != null) {
            Item item = itemRepository.findByUuid(cartDto.getItemUuid());
            if (item == null) {
                throw new BadRequestException("Invalid item selection");
            }
            if (item.isDeleted() || !item.isActive()) {
                throw new BadRequestException("Invalid item selection, Please select a active item");
            }
            Cart.ItemDetail itemDetail = null;
            if (cart.getItemIds().contains(item.getId())) {
                for (Cart.ItemDetail detail : cart.getItemDetailList()) {
                    if (item.getUuid().equals(detail.getItemUuid())) {
                        itemDetail = detail;
                        break;
                    }
                }
            } else {
                // Map a new item To cart
                itemDetail = ItemMapper.MAPPER.mapToCartItem(item);
                cart.getItemIds().add(item.getId());
            }
            itemDetail.setItemTotal(item.getSellingPrice());
            cart.getItemDetailList().add(itemDetail);
        }
        if (cartDto.getAddressUuid() != null) {
            Address address = addressRepository.findByUuid(cartDto.getAddressUuid());
            if (address == null) {
                throw new BadRequestException("Invalid address selection");
            }
            cart.setCustomerAddressDetail(AddressMapper.MAPPER.mapToCartAddress(address));
        }
        if (cartDto.getCouponCodeUuid() != null) {
            CouponCode couponCode = couponCodeRepository.findByUuid(cartDto.getCouponCodeUuid());
            if (couponCode == null) {
                throw new BadRequestException("Invalid coupon code selection");
            }
            cart.setCouponCodeId(couponCode.getId());
        }
        return cart;
    }

    private Cart cartPriceCalculation(Cart cart) {
        double cartSubTotal = 0;
        double packingCharges = 0;
        double itemTotalWeight = 0.0;
        double deliveryCharges = 0.0;
        // Set And Update Coupon Code Details
        cart = calculateCouponCodeDiscountAmount(cart);
        List<ObjectId> removedItemIds = new ArrayList<>();
        List<Cart.ItemDetail> removedItemList = new ArrayList<>();
        for (Cart.ItemDetail itemDetail : cart.getItemDetailList()) {
            Item item = itemRepository.findByUuid(itemDetail.getItemUuid());
            if (item.isDeleted() || !item.isActive()) {
                removedItemIds.add(item.getId());
                removedItemList.add(itemDetail);
                continue;
            }
            itemDetail.setItemTotal(item.getSellingPrice());
            itemTotalWeight += item.getWeight();
            cartSubTotal += itemDetail.getItemTotal() * itemDetail.getQuantity();
            packingCharges += SpringBeanContext.getBean(SettingService.class).getBaseChargesValue(SettingEnum.PackingCharges)
                    * itemDetail.getQuantity();
        }
        deliveryCharges = getItemTotalDeliveryCharges(cart.isStandardDelivery(), itemTotalWeight);
        cart.setSubTotal(cartSubTotal);
        cart.setPackingCharges(packingCharges);
        cart.setDeliveryCharges(deliveryCharges);
        // Cart Order Does not include packing charges and delivery charges
        cart.setOrderTotal(cartSubTotal + packingCharges + deliveryCharges);
        return cart;
    }

    private double getItemTotalDeliveryCharges(boolean standardDelivery, double itemTotalWeight){
        // itemTotalWeight + packagingWeight
        if (itemTotalWeight == 0.0) return 0.0;
        itemTotalWeight += SpringBeanContext.getBean(SettingService.class).getBaseChargesValue(SettingEnum.PackingWeight);
        return SpringBeanContext.getBean(SettingService.class).getDeliveryCharges(true, itemTotalWeight);
    }

    private Cart calculateCouponCodeDiscountAmount(Cart cart) {
        if (cart.getCouponCodeId() == null) return cart;
        CouponCode couponCode = couponCodeRepository.findById(cart.getCouponCodeId()).orElse(null);
        if (couponCode.getEndDate().isBefore(LocalDate.now())) {
            cart.setCouponCodeId(null);
            cart.setCouponCodeRefDetail(null);
            cart.setCouponDiscountAmount(0.0);
            return cart;
        }
        if (couponCode.getUsedCount() >= couponCode.getMaxUsePerUser()) {
            cart.setCouponCodeId(null);
            cart.setCouponCodeRefDetail(null);
            cart.setCouponDiscountAmount(0.0);
            return cart;
        }
        if (cart.getSubTotal() < couponCode.getMinOrderValue()) {
            cart.setCouponCodeId(null);
            cart.setCouponCodeRefDetail(null);
            cart.setCouponDiscountAmount(0.0);
            return cart;
        }
        double couponApplicableCartSubTotal = 0;
        for (Cart.ItemDetail itemDetail : cart.getItemDetailList()) {
            if(!itemDetail.getOfferApplicable()) continue;
            couponApplicableCartSubTotal += itemDetail.getItemTotal() * itemDetail.getQuantity();
        }
        if(couponApplicableCartSubTotal == 0) return cart;
        // implement coupon code
        double couponCodeDiscountAmount = 0.0;
        if (couponCode.getDiscountType().equals(DiscountTypeEnum.Percentage)) {
            if (!couponCode.getDiscountValue().equals(0.0)) {
                double couponDiscount = (couponCode.getDiscountValue() / 100.0) * couponApplicableCartSubTotal;
                if (couponDiscount > couponCode.getMaxDiscountAmount()) {
                    couponDiscount = couponCode.getMaxDiscountAmount();
                }
                couponCodeDiscountAmount = couponDiscount;
            }
        } else if (couponCode.getDiscountType().equals(DiscountTypeEnum.Amount)) {
            couponCodeDiscountAmount = couponCode.getDiscountValue();
        }
        // Increase Coupon Code Used Count
        // couponCode.setUsedCount(couponCode.getUsedCount() + 1);
        // Update Coupon Detail To Cart
        cart.setCouponCodeRefDetail(new CouponCode.CouponCodeRef(
                couponCode.getUuid(), couponCode.getTitle(),
                couponCode.getCouponCode(), couponCode.getDiscountType()));
        cart.setCouponDiscountAmount(couponCodeDiscountAmount);
        return cart;
    }

    private Cart createNewCart(String authorizationToken, String deviceId) throws BadRequestException {
        Customer loggedInCustomer = null;
        Cart cart = null;
        try {
            if (!TextUtils.isEmpty(authorizationToken)) {
                String username = SpringBeanContext.getBean(JwtTokenUtil.class).validateToken(authorizationToken);
                loggedInCustomer = customerRepository.findFirstByUsername(username);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (loggedInCustomer != null) {
            cart = cartRepository.findByCustomerId(loggedInCustomer.getId());
        }
//        if (cart == null && !TextUtils.isEmpty(deviceId)) {
//            cart = cartRepository.findByDeviceId(deviceId);
//        }
        if (cart == null) {
            cart = new Cart();
        }
        if (!TextUtils.isEmpty(deviceId)) {
            cart.setDeviceId(deviceId);
        }
        if (loggedInCustomer != null) {
            cart.setCustomerId(loggedInCustomer.getId());
            cart.setCustomerDetail(new Cart.CustomerRefDetail(
                    loggedInCustomer.getUuid(), loggedInCustomer.getFirstName(),
                    loggedInCustomer.getLastName(), loggedInCustomer.getIsdCode(),
                    loggedInCustomer.getMobile()));
        }
        cart = cartRepository.save(cart);
        if (cart == null) {
            throw new BadRequestException("Invalid DeviceId Provided And Authorization");
        }
        return cart;
    }

    public String placeOrder(String cartUuid) throws BadRequestException {
        // TODO > InOrder To Place Order You Need To Login > Then Update > Customer Details Into It.
        Customer loggedInCustomer = (Customer) SpringBeanContext.getBean(JwtUserDetailsService.class).getLoggedInUser();
        Cart cart = cartRepository.findByUuid(cartUuid);
        if (cart == null) {
            throw new BadRequestException("There is no valid cart");
        }
        if (TextUtils.isEmpty(cart.getItemDetailList())) {
            throw new BadRequestException("there is no valid item in your cart");
        }
        if(cart.getCustomerId() == null){
            cart.setCustomerId(loggedInCustomer.getId());
            cart.setCustomerDetail(new Cart.CustomerRefDetail(
                    loggedInCustomer.getUuid(), loggedInCustomer.getFirstName(),
                    loggedInCustomer.getLastName(), loggedInCustomer.getIsdCode(),
                    loggedInCustomer.getMobile()));
        }
        cart = cartPriceCalculation(cart);
        Order order = CartMapper.MAPPER.mapToOrder(cart);
        // Generate > invoice number | order number
        order.setInvoiceNumber(SpringBeanContext.getBean(SequenceRepository.class).getNextSequenceId(Order.class.getSimpleName()));
        order.setOrderId(TextUtils.getOrderReferenceId(order.getInvoiceNumber()));
        order = SpringBeanContext.getBean(OrderService.class).updateOrderStatusLog(order, order.getOrderStatus());
        orderRepository.save(order);
        // clear the cart
        clearCart(cart.getUuid());

        // Send Notification To Admin for order Alert
        SystemUser notifyAdmin =  systemUserRepository.findFirstByUsername("admin@admin.com");
        FcmNotificationBean.Notification notification = new FcmNotificationBean.Notification("New Order Received" , "New Order Total of " + order.getOrderTotal() +  " with " + order.getItemDetailList().size() + " Items");
        FcmNotificationBean.Data data = new FcmNotificationBean.Data("New Order Received" , "New Order Total of " + order.getOrderTotal() + " with " + order.getItemDetailList().size() + " Items" , "" , new Date().toString(),"Order" ,order.getOrderId() , order.getOrderStatus().toString());
        fcmComponent.sendNotificationToUser(notifyAdmin.getDeviceType() , notifyAdmin.getFcmDeviceToken(),notification,data);

        return order.getOrderId();
    }

    public long getCartCount() {
        return cartRepository.count();
    }


    public List<CartDto.DetailCart> getCartList() throws BadRequestException {
        List<Cart> cartList = cartRepository.findAll();
        List<CartDto.DetailCart> detailCartList = new ArrayList<>();
        for (Cart cart : cartList) {
            // Skip The Empty Cart
            if(TextUtils.isEmpty(cart.getItemDetailList())) continue;
            detailCartList.add(CartMapper.MAPPER.mapToDetailCartDto(cart));
        }
        return detailCartList;
    }

    public CartDto.CartItemCount getCartItemCount(String cartUuid) {
        Cart cart = cartRepository.findByUuid(cartUuid);
        CartDto.CartItemCount cartItemCount = new CartDto.CartItemCount();
        if (cart == null) {
            return cartItemCount;
        }
        cartItemCount.setItemCount(cart.getItemDetailList().size());
        cartItemCount.setItemTotal(cart.getSubTotal());
        return cartItemCount;
    }

    private Cart getCartByDeviceId(String authorizationToken, String deviceId) throws BadRequestException {
        Customer loggedInCustomer = null;
        Cart cart = null;
        try {
            if (!TextUtils.isEmpty(authorizationToken)) {
                String username = SpringBeanContext.getBean(JwtTokenUtil.class).validateToken(authorizationToken);
                loggedInCustomer = customerRepository.findFirstByUsername(username);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (loggedInCustomer != null) {
            cart = cartRepository.findByCustomerId(loggedInCustomer.getId());
        }
//        if (cart == null && !TextUtils.isEmpty(deviceId)) {
//            cart = cartRepository.findByDeviceId(deviceId);
//        }
        if (cart == null) {
            cart = createNewCart(authorizationToken, deviceId);
        }
        return cart;
    }

    public CartDto.DetailCart get(String cartUuid) throws BadRequestException {
        Cart cart = cartRepository.findByUuid(cartUuid);
        if (cart == null) {
            throw new BadRequestException("Invalid cart id provided");
        }
        return CartMapper.MAPPER.mapToDetailCartDto(cart);
    }

    public void applyCouponCode(String cartUuid, String couponCodeUuid) throws BadRequestException {
        if (TextUtils.isEmpty(cartUuid)) {
            throw new BadRequestException("Invalid cart id provided.");
        }
        Cart cart = cartRepository.findByUuid(cartUuid);
        if (!TextUtils.isEmpty(couponCodeUuid)) {
            CouponCode couponCode = couponCodeRepository.findByUuid(couponCodeUuid);
            if (couponCode == null) {
                throw new BadRequestException("Invalid coupon code selection");
            }
            cart.setCouponCodeId(couponCode.getId());
            cart.setCouponCodeRefDetail(new CouponCode.CouponCodeRef(
                    couponCode.getUuid(), couponCode.getTitle(),
                    couponCode.getCouponCode(), couponCode.getDiscountType()));

        }
        cart = calculateCouponCodeDiscountAmount(cart);
        cart = cartPriceCalculation(cart);
        cartRepository.save(cart);
    }

    public void removeCouponCode(String cartUuid) throws BadRequestException {
        if (TextUtils.isEmpty(cartUuid)) {
            throw new BadRequestException("Invalid cart id provided.");
        }
        Cart cart = cartRepository.findByUuid(cartUuid);
        if (cart.getCouponCodeId() == null) {
            throw new BadRequestException("Invalid Request No Coupon Code Applied");
        }
        cart.setCouponCodeId(null);
        cart.setCouponCodeRefDetail(null);
        cart.setCouponDiscountAmount(0.0);
        cart = calculateCouponCodeDiscountAmount(cart);
        cart = cartPriceCalculation(cart);
        cartRepository.save(cart);
    }
}
