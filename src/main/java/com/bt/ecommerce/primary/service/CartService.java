package com.bt.ecommerce.primary.service;

import com.bt.ecommerce.configuration.SpringBeanContext;
import com.bt.ecommerce.exception.BadRequestException;
import com.bt.ecommerce.primary.dto.CartDto;
import com.bt.ecommerce.primary.mapper.AddressMapper;
import com.bt.ecommerce.primary.mapper.CartMapper;
import com.bt.ecommerce.primary.mapper.ItemMapper;
import com.bt.ecommerce.primary.pojo.*;
import com.bt.ecommerce.primary.pojo.enums.DiscountTypeEnum;
import com.bt.ecommerce.primary.pojo.user.SystemUser;
import com.bt.ecommerce.primary.repository.SequenceRepository;
import com.bt.ecommerce.utils.ProjectConst;
import com.bt.ecommerce.utils.TextUtils;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CartService extends _BaseService {

    public CartDto.DetailCart getCartDetail(String deviceId) throws BadRequestException {
        Cart cart = getCartByDeviceId(deviceId);
        cart = cartPriceCalculation(cart);
        return CartMapper.MAPPER.mapToDetailCartDto(cart);
    }

    public void updateCart(String deviceId, String uuid, CartDto.UpdateCart cartDto) throws BadRequestException {
        Cart cart = cartRepository.findByUuid(uuid);
        if (cart == null) {
            cart = createNewCart(deviceId);
        }
        cart = mapToCartItem(cart, cartDto);
        cart = cartPriceCalculation(cart);
        cartRepository.save(cart);
    }

    public void clearCart(String cartUuid) {
        Cart cart = cartRepository.findByUuid(cartUuid);
        if (cart == null) {
            return;
        }
        cartRepository.delete(cart);
    }

    public void removeItemFromCart(String cartUuid, String itemUuid) throws BadRequestException {
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
    }

    public void updateItemQuantity(String cartUuid, String itemUuid, long quantity) throws BadRequestException {
        Cart cart = cartRepository.findByUuid(cartUuid);
        if (cart == null) {
            throw new BadRequestException("Please provide a valid cart");
        }
        cart.getItemDetailList().stream()
                .filter(x -> x.getItemUuid().equalsIgnoreCase(itemUuid)).findFirst().ifPresent(x -> x.setQuantity(quantity));
        cart = cartPriceCalculation(cart);
        cartRepository.save(cart);
    }

    private Cart mapToCartItem(Cart cart, CartDto.UpdateCart cartDto) throws BadRequestException {
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
        // TODO > Delivery charges calculation is based on item weight
        double deliveryCharges = ProjectConst.deliveryCharges;
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
            cartSubTotal += itemDetail.getItemTotal() * itemDetail.getQuantity();
            packingCharges += ProjectConst.packingCharges * itemDetail.getQuantity();
        }
        cart.setSubTotal(cartSubTotal);
        cart.setPackingCharges(packingCharges);
        cart.setDeliveryCharges(deliveryCharges);
        // Cart Order Does not include packing charges and delivery charges
        cart.setOrderTotal(cartSubTotal + packingCharges + deliveryCharges);
        return cart;
    }

    private Cart calculateCouponCodeDiscountAmount(Cart cart) {
        if (cart.getCouponCodeId() == null) return cart;
        CouponCode couponCode = couponCodeRepository.findById(cart.getCouponCodeId()).orElse(null);
        if (couponCode.getEndDate().isAfter(LocalDate.now())) {
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
        // implement coupon code
        double couponCodeDiscountAmount = 0.0;
        if (couponCode.getDiscountType().equals(DiscountTypeEnum.Percentage)) {
            if (!couponCode.getDiscountValue().equals(0.0)) {
                double couponDiscount = (couponCode.getDiscountValue() / 100.0) * cart.getSubTotal();
                if (couponDiscount > couponCode.getMaxDiscountAmount()) {
                    couponDiscount = couponCode.getMaxDiscountAmount();
                }
                couponCodeDiscountAmount = couponDiscount;
            }
        } else if (couponCode.getDiscountType().equals(DiscountTypeEnum.Amount)) {
            couponCodeDiscountAmount = couponCode.getDiscountValue();
        }
        // Increase Coupon Code Used Count
        couponCode.setUsedCount(couponCode.getUsedCount() + 1);
        // Update Coupon Detail To Cart
        cart.setCouponCodeRefDetail(new CouponCode.CouponCodeRef(
                couponCode.getUuid(), couponCode.getTitle(),
                couponCode.getCouponCode(), couponCode.getDiscountType()));
        cart.setCouponDiscountAmount(couponCodeDiscountAmount);
        return cart;
    }

    private Cart createNewCart(String deviceId) throws BadRequestException {
        SystemUser loggedInCustomer = null;
        Cart cart = null;
        try {
            // loggedInCustomer = (SystemUser) SpringBeanContext.getBean(JwtUserDetailsService.class).getLoggedInUser();
            loggedInCustomer = systemUserRepository.findAll().get(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!TextUtils.isEmpty(deviceId)) {
            cart = cartRepository.findByDeviceId(deviceId);
        }
        if (cart == null && loggedInCustomer != null) {
            cart = cartRepository.findByCustomerId(loggedInCustomer.getId());
        }
        if (cart == null) {
            cart = new Cart();
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
        }
        if (cart == null) {
            throw new BadRequestException("Invalid DeviceId Provided And Authorization");
        }
        return cart;
    }

    public String placeOrder(String cartUuid) throws BadRequestException {
        // TODO > InOrder To Place Order You Need To Login > Then Update > Customer Details Into It.
        Cart cart = cartRepository.findByUuid(cartUuid);
        if (cart == null) {
            throw new BadRequestException("There is no valid cart");
        }
        if (TextUtils.isEmpty(cart.getItemDetailList())) {
            throw new BadRequestException("there is no valid item in your cart");
        }
        cart = cartPriceCalculation(cart);
        Order order = CartMapper.MAPPER.mapToOrder(cart);
        // Generate > invoice number | order number
        order.setInvoiceNumber(SpringBeanContext.getBean(SequenceRepository.class).getNextSequenceId(Order.class.getSimpleName()));
        order.setOrderId(TextUtils.getOrderReferenceId(order.getInvoiceNumber()));
        orderRepository.save(order);
        // clear the cart
        clearCart(cart.getUuid());
        return order.getOrderId();
    }

    public long getCartCount() {
        return cartRepository.count();
    }

    public List<CartDto.DetailCart> getCartList() throws BadRequestException {
        List<Cart> cartList = cartRepository.findAll();
        return cartList.stream()
                .map(cart -> CartMapper.MAPPER.mapToDetailCartDto(cart))
                .collect(Collectors.toList());
    }

    public CartDto.CartItemCount getCartItemCount(String deviceId) throws BadRequestException {
        Cart cart = getCartByDeviceId(deviceId);
        CartDto.CartItemCount cartItemCount = new CartDto.CartItemCount();
        if (cart == null) return cartItemCount;
        cartItemCount.setItemCount(cart.getItemDetailList().size());
        cartItemCount.setItemTotal(cart.getSubTotal());
        return cartItemCount;
    }

    private Cart getCartByDeviceId(String deviceId) throws BadRequestException {
        SystemUser loggedInCustomer = null;
        Cart cart = null;
        try {
            // loggedInCustomer = (SystemUser) SpringBeanContext.getBean(JwtUserDetailsService.class).getLoggedInUser();
            loggedInCustomer = systemUserRepository.findAll().get(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!TextUtils.isEmpty(deviceId)) {
            cart = cartRepository.findByDeviceId(deviceId);
        }
        if (cart == null && loggedInCustomer != null) {
            cart = cartRepository.findByCustomerId(loggedInCustomer.getId());
        }
        if (cart == null) {
            cart = createNewCart(deviceId);
        }
        return cart;
    }

}
