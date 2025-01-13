package com.bt.ecommerce.primary.service;


import com.bt.ecommerce.configuration.SpringBeanContext;
import com.bt.ecommerce.exception.BadRequestException;
import com.bt.ecommerce.primary.pojo.PaymentTransaction;
import com.bt.ecommerce.primary.pojo.enums.PaymentGateWayEnum;
import com.bt.ecommerce.primary.pojo.enums.PaymentGatewayStatusEnum;
import com.bt.ecommerce.primary.pojo.user.Customer;
import com.bt.ecommerce.primary.razorpay.BeanRazorPayResponse;
import com.bt.ecommerce.primary.repository.PaymentTransactionRepository;
import com.bt.ecommerce.primary.repository.SequenceRepository;
import com.bt.ecommerce.utils.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;

@Service
public class PaymentTransactionService extends _BaseService {
    @Autowired
    PaymentTransactionRepository paymentTransactionRepository;

    public PaymentTransaction generatePaymentTransaction(Customer customer, int transactionAmount, PaymentGateWayEnum paymentGateway, String cartUUid) throws BadRequestException {

        PaymentTransaction paymentTransaction = new PaymentTransaction();
        paymentTransaction.setLoggedInCustomerId(customer.getId());
        paymentTransaction.setRecordId(SpringBeanContext.getBean(SequenceRepository.class).getNextSequenceId(PaymentTransaction.class.getSimpleName()));
        paymentTransaction.setPaymentGateway(paymentGateway);
        paymentTransaction.setPaymentStatus(PaymentGatewayStatusEnum.created);
        paymentTransaction.setPaymentRequestData(null);
        paymentTransaction.setPaymentResponseData(null);
        paymentTransaction.setPaymentGatewayRefId(null);
        paymentTransaction.setPaymentWebHookData(null);
        paymentTransaction.setPaymentCaptured(Boolean.FALSE);
        paymentTransaction.setAmount((double) transactionAmount);
        paymentTransaction.setOrderId(cartUUid);
        paymentTransactionRepository.save(paymentTransaction);
        paymentTransaction.setPaymentTransactionRefId(TextUtils.getPaymentRequestTransactionId(paymentTransaction.getRecordId()));
        paymentTransactionRepository.save(paymentTransaction);
        return paymentTransaction;
    }


    public PaymentTransaction updatePaymentTransaction(String paymentTransactionRefId, PaymentGatewayStatusEnum paymentGatewayStatusEnum, String paymentRequestData, String paymentResponseData, BeanRazorPayResponse.Root razorPayResponse) throws BadRequestException {

        PaymentTransaction paymentTransaction = paymentTransactionRepository.findByPaymentTransactionRefId(paymentTransactionRefId);
        if (paymentTransaction == null) {
            throw new BadRequestException(paymentTransactionRefId + " not found");
        }
        paymentTransaction.setPaymentStatus(paymentGatewayStatusEnum);
        paymentTransaction.setPaymentRequestData(paymentRequestData);
        paymentTransaction.setPaymentResponseData(paymentResponseData);
        paymentTransaction.setPaymentGatewayRefId(razorPayResponse.getId());
        //i think no needed....
        paymentTransaction.setPaymentCaptured(Boolean.FALSE);

        paymentTransaction.setAmount((double) razorPayResponse.getAmount());
        paymentTransactionRepository.save(paymentTransaction);
        return paymentTransaction;
    }

    public PaymentTransaction updatePaymentTransactionWebhookAndStatus(String paymentGatewayRefId, String webhookData, PaymentGatewayStatusEnum paymentGatewayStatusEnum) throws BadRequestException {

        PaymentTransaction paymentTransaction = paymentTransactionRepository.findByPaymentGatewayRefId(paymentGatewayRefId);
        if (paymentTransaction == null) {
            throw new BadRequestException(paymentGatewayRefId + " not found");
        }
        paymentTransaction.setPaymentWebHookData(webhookData);
        paymentTransaction.setPaymentStatus(paymentGatewayStatusEnum);
        paymentTransaction.setPaymentCaptured(Boolean.TRUE);
        paymentTransactionRepository.save(paymentTransaction);
        return paymentTransaction;
    }
    public String updatePaymentStatusByPaymentGatewayId(String paymentGatewayRefId, PaymentGatewayStatusEnum paymentGatewayStatusEnum,String paymentResponseData) throws BadRequestException {

        PaymentTransaction paymentTransaction = paymentTransactionRepository.findByPaymentGatewayRefId(paymentGatewayRefId);
        if (paymentTransaction == null) {
            throw new BadRequestException(paymentGatewayRefId + " not found");
        }
        paymentTransaction.setPaymentStatus(paymentGatewayStatusEnum);
        paymentTransaction.setPaymentCaptured(Boolean.TRUE);
        paymentTransaction.setPaymentResponseData(paymentResponseData);
        paymentTransactionRepository.save(paymentTransaction);
        return paymentTransaction.getOrderId();
    }
}