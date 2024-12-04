package com.bt.ecommerce.primary.razorpay;

import com.bt.ecommerce.exception.BadRequestException;
import com.bt.ecommerce.primary.pojo.PaymentTransaction;
import com.bt.ecommerce.primary.pojo.enums.PaymentGateWayEnum;
import com.bt.ecommerce.primary.service.PaymentTransactionService;
import com.bt.ecommerce.primary.service._BaseService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Base64;
import java.util.UUID;

@Service
public class RazorPayService extends _BaseService {
    @Autowired
    PaymentTransactionService paymentTransactionService;

    public BeanRazorPayResponse.Root createPayment(BeanRazorPayRequest.RazorPayRequest requestObj) throws BadRequestException {
        requestObj.setAmount(100);
        requestObj.setCurrency("INR");
        requestObj.setAccept_partial(false);
        requestObj.setExpire_by(17328161260L);
        requestObj.setDescription("Testing api");

        BeanRazorPayRequest.Customer customerDetails = new BeanRazorPayRequest.Customer();
        customerDetails.setContact("+918441042698");
        customerDetails.setEmail("gajendrasharma421@gmail.com");
        customerDetails.setName("Gajendra");

        requestObj.setCustomer(customerDetails);
        String uuid = UUID.randomUUID().toString();
        requestObj.setReference_id(uuid.substring(1, 8));

        BeanRazorPayRequest.Notify notify = new BeanRazorPayRequest.Notify();
        notify.setEmail(false);
        notify.setSms(true);
        requestObj.setNotify(notify);

        requestObj.setReminder_enable(true);
        requestObj.setCallback_url("https://example-callback-url.com/");
        requestObj.setCallback_method("get");


        PaymentTransaction paymentTransaction = paymentTransactionService.generatePaymentTransaction("Gajendra", Double.valueOf(requestObj.getAmount()), PaymentGateWayEnum.RazorPay);

        String url = "https://api.razorpay.com/v1/payment_links/";

//        String basicAuthHeader=generateBasicAuthHeader("rzp_test_gSYLawQRB8O3IF","P50rURr8paH8lTyXzujXpFfE");
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setBasicAuth("rzp_test_gSYLawQRB8O3IF", "P50rURr8paH8lTyXzujXpFfE");

        HttpEntity<BeanRazorPayRequest.RazorPayRequest> entity = new HttpEntity<>(requestObj, headers);
        //set all data here
        RestTemplate restTemplate = new RestTemplate();
        String responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, String.class).getBody();
        Type razorPayResponse = new TypeToken<BeanRazorPayResponse.Root>() {
        }.getType();
        BeanRazorPayResponse.Root authenticationResponseBean = new Gson().fromJson(responseEntity, razorPayResponse);
        System.out.println(responseEntity);

        paymentTransaction.setPaymentRequestData(new Gson().toJson(requestObj));
        paymentTransaction.setPaymentResponseData(responseEntity);
        paymentTransactionRepository.save(paymentTransaction);

        return authenticationResponseBean;
    }

    public static String generateBasicAuthHeader(String publicKey, String secretKey) {
        // Combine keys with a colon
        String combinedKeys = publicKey + ":" + secretKey;

        // Encode using Base64
        String encodedKeys = Base64.getEncoder().encodeToString(combinedKeys.getBytes());

        // Return the Authorization header value
        return "Basic " + encodedKeys;
    }

    public void webHookTransaction(BeanWebhookResponse.Transaction webHookTransaction) {

        String url = "";
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setBasicAuth("rzp_test_gSYLawQRB8O3IF", "P50rURr8paH8lTyXzujXpFfE");

        HttpEntity<BeanWebhookResponse.Transaction> entity = new HttpEntity<>(webHookTransaction, headers);
        //set all data here
        RestTemplate restTemplate = new RestTemplate();
        String responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, String.class).getBody();
        Type razorPayResponse = new TypeToken<BeanRazorPayResponse.Root>() {
        }.getType();
        BeanRazorPayResponse.Root authenticationResponseBean = new Gson().fromJson(responseEntity, razorPayResponse);
        System.out.println(responseEntity);
//        boolean isValid = RazorpaySignatureVerifier.verifySignature(payload, signature, razorpaySecret);


    }
}

