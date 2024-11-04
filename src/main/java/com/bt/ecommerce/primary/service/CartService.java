package com.bt.ecommerce.primary.service;

import com.bt.ecommerce.exception.BadRequestException;
import com.bt.ecommerce.primary.dto.CartDto;
import com.bt.ecommerce.primary.mapper.AddressMapper;
import com.bt.ecommerce.primary.mapper.CartMapper;
import com.bt.ecommerce.primary.mapper.ItemMapper;
import com.bt.ecommerce.primary.pojo.Address;
import com.bt.ecommerce.primary.pojo.Cart;
import com.bt.ecommerce.primary.pojo.CouponCode;
import com.bt.ecommerce.primary.pojo.Item;
import com.bt.ecommerce.primary.pojo.enums.DiscountTypeEnum;
import com.bt.ecommerce.primary.pojo.user.SystemUser;
import com.bt.ecommerce.utils.ProjectConst;
import com.bt.ecommerce.utils.TextUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Slf4j
@Service
public class CartService extends _BaseService {

    public CartDto.DetailCart getCartDetail(String cartUuid) throws BadRequestException {
        Cart cart = null;
        if (!TextUtils.isEmpty(cartUuid)) {
            cart = cartRepository.findByUuid(cartUuid);
        }
        if (cart == null) {
            cart = createNewCart();
        }
        cart = cartPriceCalculation(cart);
        return CartMapper.MAPPER.mapToDetailCartDto(cart);
    }

//    public String createCart(CartDto.UpdateCart cartDto) throws BadRequestException {
//        Cart cart = createNewCart();
//        cart = mapToCartItem(cart, cartDto);
//        cartPriceCalculation(cart);
//        cartRepository.save(cart);
//        return cart.getUuid();
//    }

    public void updateCart(String uuid, CartDto.UpdateCart cartDto) throws BadRequestException {
        Cart cart = cartRepository.findByUuid(uuid);
        if (cart == null) {
            cart = createNewCart();
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
        Item item = itemRepository.findByUuid(cartDto.getItemUuid());
        if (item == null) {
            throw new BadRequestException("Invalid item selection");
        }
        if (item.isDeleted() || !item.isActive()) {
            throw new BadRequestException("Invalid item selection, Please select a active item");
        }
        Cart.ItemDetail itemDetail = ItemMapper.MAPPER.mapToCartItem(item);
        if (!cart.getItemIds().contains(item.getId())) {
            cart.getItemIds().add(item.getId());
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
        double deliveryCharges = ProjectConst.deliveryCharges;
        // Set And Update Coupon Code Details
        cart = calculateCouponCodeDiscountAmount(cart);
        for (Cart.ItemDetail itemDetail : cart.getItemDetailList()) {
            cartSubTotal += itemDetail.getItemTotal() * itemDetail.getQuantity();
            packingCharges += ProjectConst.packingCharges * itemDetail.getQuantity();
        }
        // TODO // add > PackingCharges > DeliveryCharges > couponCodeDiscount
        cart.setSubTotal(cartSubTotal);
        cart.setPackingCharges(packingCharges);
        cart.setDeliveryCharges(deliveryCharges);
        cart.setOrderTotal(cartSubTotal + packingCharges);
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
        if(couponCode.getDiscountType().equals(DiscountTypeEnum.Percentage)) {
            if(!couponCode.getDiscountValue().equals(0.0)){
                double couponDiscount = (couponCode.getDiscountValue() / 100.0) * cart.getSubTotal();
                if(couponDiscount > couponCode.getMaxDiscountAmount()){
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

    private Cart createNewCart() {
//        SystemUser loggedInCustomer = (SystemUser) SpringBeanContext.getBean(JwtUserDetailsService.class).getLoggedInUser();
        // TODO > Remove this > loggedInCustomer
        SystemUser loggedInCustomer = systemUserRepository.findAll().get(0);
        Cart cart = cartRepository.findByCustomerId(loggedInCustomer.getId());
        if (cart == null) {
            cart = new Cart();
            cart.setCustomerId(loggedInCustomer.getId());
        }
        cart.setCustomerDetail(new Cart.CustomerRefDetail(
                loggedInCustomer.getUuid(), loggedInCustomer.getFirstName(),
                loggedInCustomer.getLastName(), loggedInCustomer.getIsdCode(),
                loggedInCustomer.getMobile()));
        return cart;
    }
}
