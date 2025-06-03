package com.bt.ecommerce.primary.dto;

import com.bt.ecommerce.primary.pojo.Cart;
import com.bt.ecommerce.primary.pojo.CouponCode;
import com.bt.ecommerce.primary.pojo.Order;
import com.bt.ecommerce.primary.pojo.common.BasicParent;
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
    public static class DetailOrder extends _BasicDto {
        private Long invoiceNumber;
        private String orderId;
        private String orderTrackingId;
        private OrderStatusEnum orderStatus;
        private List<Order.OrderStatusLog> orderStatusLogList;
        private PaymentStatusEnum paymentStatus;
        private PaymentTypeEnum paymentType;
        // TODO > ADD More Order Data Later On In It
        private Cart.CustomerRefDetail customerDetail;
        private Cart.CustomerAddressDetail customerAddressDetail;
        private List<Cart.ItemDetail> itemDetailList = new ArrayList<>();

        private boolean standardDelivery;
        private double subTotal = 0.0;
        // Coupon Detail
        private CouponCode.CouponCodeRef couponCodeRefDetail;
        private double couponDiscountAmount = 0.0;
        private double packingCharges = 0.0;
        private double deliveryCharges = 0.0;
        private double orderTotal = 0.0;
        protected Long createdAt;
        private   Long orderAt;
        private BasicParent courierPartnerDetail;
        private String cancelReason;
        private double codCharges = 0.0;
        private String notes;
        private double shippingAmountRefund;
        private double itemAmountRefund;
    }

    @Setter
    @Getter
    @NoArgsConstructor
    public static class UpdateOrdersTrackingIds {
        private String orderId;
        private UpdateOrderDetails updateOrderDetails;
    }


    @Setter
    @Getter
    @NoArgsConstructor
    public static class PlaceOrder {
        private PaymentTypeEnum paymentTypeEnum;
    }

    @Setter
    @Getter
    @NoArgsConstructor
    public static class UpdateOrderDetails {
        private String orderTrackingId;
        private String courierPartnerId;
    }

    @Setter
    @Getter
    public static class CancelOrder {
        private String cancelReason;
    }

    @Setter
    @Getter
    public static class PartialCancelOrder {
        private List<String> itemIds;
        private String cancelReason;
        private double shippingRefundAmount;
    }

    @Setter
    @Getter
    public static class SaveOrderNotes {
        private String orderNotes;
    }
}
