package com.bt.ecommerce.primary.repository;


import com.bt.ecommerce.primary.pojo.PaymentTransaction;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PaymentTransactionRepository extends MongoRepository<PaymentTransaction, String> {

    PaymentTransaction findByRecordId(String recordId);
    //system generated Id
    PaymentTransaction findByPaymentTransactionRefId(String paymentTransactionRefId);
    PaymentTransaction findByPaymentGatewayRefId(String paymentGatewayRefId);

}

