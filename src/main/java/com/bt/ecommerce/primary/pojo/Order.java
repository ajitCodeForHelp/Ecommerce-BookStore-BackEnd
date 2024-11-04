package com.bt.ecommerce.primary.pojo;

import com.bt.ecommerce.primary.pojo.enums.DeliveryStatusEnum;
import com.bt.ecommerce.primary.pojo.enums.PaymentStatusEnum;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@Document(value = "order")
public class Order extends Cart {

    private Long invoiceNumber;
    private String orderId;
    private DeliveryStatusEnum deliveryStatus = DeliveryStatusEnum.ORDER;
    private PaymentStatusEnum paymentStatus = PaymentStatusEnum.CASH_ON_DELIVERY;
    // TODO >  PaymentStatus > and more payment related details > txn

}
