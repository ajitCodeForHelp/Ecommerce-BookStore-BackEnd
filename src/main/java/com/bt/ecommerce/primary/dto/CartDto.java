package com.bt.ecommerce.primary.dto;

import com.bt.ecommerce.primary.pojo.Cart;
import com.bt.ecommerce.primary.pojo.CouponCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CartDto extends AbstractDto{

    @Getter
    @Setter
    public static class UpdateCart {
        private String addressUuid;
        private String itemUuid;
        private String couponCodeUuid;
    }

    @Getter
    @Setter
    public static class DetailCart extends Detail{
        private Cart.CustomerRefDetail customerDetail;
        private Cart.CustomerAddressDetail customerAddressDetail;
        private List<Cart.ItemDetail> itemDetailList = new ArrayList<>();

        private double subTotal = 0.0;
        // Coupon Detail
        private CouponCode.CouponCodeRef couponCodeRefDetail;
        private double couponDiscountAmount = 0.0;
        private double packingCharges = 0.0;
        private double deliveryCharges = 0.0;
        private double orderTotal = 0.0;
        private String cartUuid;
    }

    @Getter
    @Setter
    public static class CartItemCount {
        private int itemCount = 0;
        private double itemTotal = 0.0;
    }
}
