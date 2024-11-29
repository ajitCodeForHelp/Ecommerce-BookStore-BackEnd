package com.bt.ecommerce.primary.service;


import com.bt.ecommerce.exception.BadRequestException;
import com.bt.ecommerce.primary.pojo.PaymentTransaction;
import com.bt.ecommerce.primary.pojo.enums.PaymentGateWayEnum;
import com.bt.ecommerce.primary.pojo.enums.PaymentGatewayStatusEnum;
import com.bt.ecommerce.primary.repository.PaymentTransactionRepository;
import com.bt.ecommerce.utils.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentTransactionService extends _BaseService {
    @Autowired
    PaymentTransactionRepository paymentTransactionRepository;

    public PaymentTransaction generatePaymentTransaction(String player, Double transactionAmount, PaymentGateWayEnum paymentGateway) throws BadRequestException {

        PaymentTransaction paymentTransaction = new PaymentTransaction();
        paymentTransaction.setLoggedInCustomerId(12L);
        paymentTransaction.setRecordId(System.currentTimeMillis());
//        paymentTransaction.setRecordId( getBean(PrimarySequenceRepository.class).getNextSequenceId(Brand.class.getSimpleName())));
        paymentTransaction.setPaymentGateway(paymentGateway);
        paymentTransaction.setPaymentStatus(PaymentGatewayStatusEnum.created);
        paymentTransaction.setPaymentRequestData(null);
        paymentTransaction.setPaymentResponseData(null);
        paymentTransaction.setPaymentGatewayRefId(null);
        paymentTransaction.setPaymentWebHookData(null);
        paymentTransaction.setPaymentCaptured(Boolean.FALSE);
        paymentTransaction.setAmount(transactionAmount);
        paymentTransactionRepository.save(paymentTransaction);
        paymentTransaction.setPaymentTransactionRefId(TextUtils.getPaymentRequestTransactionId(paymentTransaction.getRecordId()));
        paymentTransactionRepository.save(paymentTransaction);
        return paymentTransaction;
    }
}