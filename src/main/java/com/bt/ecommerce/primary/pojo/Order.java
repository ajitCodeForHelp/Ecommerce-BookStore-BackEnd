package com.bt.ecommerce.primary.pojo;

import com.bt.ecommerce.primary.pojo.enums.OrderStatusEnum;
import com.bt.ecommerce.primary.pojo.enums.PaymentStatusEnum;
import com.bt.ecommerce.primary.pojo.enums.PaymentTypeEnum;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@Document(value = "order")
public class Order extends Cart {

    private Long invoiceNumber;
    private String orderId;
    private String orderTrackingId; // For Order Item Tracking
    private OrderStatusEnum orderStatus = OrderStatusEnum.ORDER;
    private PaymentStatusEnum paymentStatus = PaymentStatusEnum.PENDING;
    private PaymentTypeEnum paymentType = PaymentTypeEnum.ONLINE;
    // TODO >  PaymentStatus > and more payment related details > txn

}
