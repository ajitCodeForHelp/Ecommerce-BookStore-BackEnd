package com.bt.ecommerce.primary.repository;


import com.bt.ecommerce.primary.pojo.PaymentTransaction;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PaymentTransactionRepository extends MongoRepository<PaymentTransaction, String> {

    PaymentTransaction findByRecordId(String recordId);
    //system generated Id
    PaymentTransaction findByPaymentTransactionRefId(String paymentTransactionRefId);
    PaymentTransaction findByPaymentGatewayRefId(String paymentGatewayRefId);
    List<PaymentTransaction> findByOrderId(String orderId);

}

