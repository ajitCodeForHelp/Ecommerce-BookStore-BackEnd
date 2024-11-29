package com.bt.ecommerce.primary.dto;

import com.bt.ecommerce.primary.pojo.Cart;
import com.bt.ecommerce.primary.pojo.CouponCode;
import com.bt.ecommerce.primary.pojo.enums.OrderStatusEnum;
import com.bt.ecommerce.primary.pojo.enums.PaymentStatusEnum;
import com.bt.ecommerce.primary.pojo.enums.PaymentTypeEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class OrderDto extends AbstractDto{

    @Getter
    @Setter
    public static class DetailOrder {
        private Long invoiceNumber;
        private String orderId;
        private String orderTrackingId;
        private OrderStatusEnum orderStatus;
        private PaymentStatusEnum paymentStatus;
        private PaymentTypeEnum paymentType;
        // TODO > ADD More Order Data Later On In It

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
    }

    @Setter
    @Getter
    @NoArgsConstructor
    public class UpdateOrdersTrackingIds {
        private String orderId;
        private String orderTrackingId;
    }
}
