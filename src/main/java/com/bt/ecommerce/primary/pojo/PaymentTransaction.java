package com.bt.ecommerce.primary.pojo;


import com.bt.ecommerce.primary.pojo.enums.PaymentGateWayEnum;
import com.bt.ecommerce.primary.pojo.enums.PaymentGatewayStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "payment_transaction")
public class PaymentTransaction extends _BasicEntity{
    private Long recordId;
    // Our System Generated ID
    private String paymentTransactionRefId;

    @Enumerated(EnumType.STRING)
    private PaymentGateWayEnum paymentGateway;

    private String paymentGatewayRefId;

    @Enumerated(EnumType.STRING)
    private PaymentGatewayStatusEnum paymentStatus;

    @Lob
    private String paymentRequestData;

    @Lob
    private String paymentResponseData;
    @Lob
    private String paymentWebHookData;

    private String orderId;

    private ObjectId loggedInCustomerId;

    private Boolean paymentCaptured;

    private Double amount;

}

