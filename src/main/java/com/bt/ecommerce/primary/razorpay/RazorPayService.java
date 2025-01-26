package com.bt.ecommerce.primary.razorpay;

import com.bt.ecommerce.configuration.SpringBeanContext;
import com.bt.ecommerce.exception.BadRequestException;
import com.bt.ecommerce.primary.dto.OrderDto;
import com.bt.ecommerce.primary.pojo.PaymentTransaction;
import com.bt.ecommerce.primary.pojo.enums.PaymentGateWayEnum;
import com.bt.ecommerce.primary.pojo.enums.PaymentGatewayStatusEnum;
import com.bt.ecommerce.primary.pojo.user.Customer;
import com.bt.ecommerce.primary.service.PaymentTransactionService;
import com.bt.ecommerce.primary.service._BaseService;
import com.bt.ecommerce.security.JwtTokenUtil;
import com.bt.ecommerce.security.JwtUserDetailsService;
import com.bt.ecommerce.utils.Const;
import com.bt.ecommerce.utils.TextUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class RazorPayService extends _BaseService {

    @Value("${razor-pay.payment-links-url}")
    private String razorPayPaymentLinksUrl;

    @Value("${razor-pay.user-name}")
    private String razorPayUserName;

    @Value("${razor-pay.password}")
    private String razorPayPassword;

    @Value("${razor-pay.user-name_test}")
    private String razorPayUserNameTest;

    @Value("${razor-pay.password_test}")
    private String razorPayPasswordTest;

    @Autowired
    private PaymentTransactionService paymentTransactionService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    RestTemplate restTemplate;

    private static final String CALLBACK_URL_LIVE = "https://thebooks24.com/paymentStatus/";
    private static final String CALLBACK_URL_TEST = "http://localhost:3000/paymentStatus/";
    private static final long EXPIRY_DURATION_MS = 10 * 60 * 1000; // 10 minutes
    private static final String CURRENCY = "INR";

    public BeanRazorPayResponse.Root createPayment(BeanRazorPayCustomerRequest customerRequest) throws BadRequestException {
        // Validate and fetch logged-in customer
        Customer loggedInCustomer = (Customer) SpringBeanContext.getBean(JwtUserDetailsService.class).getLoggedInUser();

        // Generate Payment Transaction
        PaymentTransaction paymentTransaction = paymentTransactionService.generatePaymentTransaction(
                loggedInCustomer, customerRequest.getAmount(), PaymentGateWayEnum.RazorPay, customerRequest.getOrderId());

        // Build RazorPay Request
        BeanRazorPayRequest.RazorPayRequest requestObj = buildRazorPayRequest(customerRequest, loggedInCustomer, paymentTransaction);

        // Call RazorPay API and fetch response
        BeanRazorPayResponse.Root razorPayResponse = callRazorPayAPI(requestObj);

        // Update Payment Transaction
        paymentTransactionService.updatePaymentTransaction(
                paymentTransaction.getPaymentTransactionRefId(),
                PaymentGatewayStatusEnum.created,
                requestObj.toString(),
                razorPayResponse.toString(),
                razorPayResponse);

        return razorPayResponse;
    }

    /**
     * Validate and fetch customer based on the authorization token.
     */
    private Customer validateAndFetchCustomer(String authorizationToken) throws BadRequestException {
        try {
            if (!TextUtils.isEmpty(authorizationToken)) {
                String username = jwtTokenUtil.validateToken(authorizationToken);
                return customerRepository.findFirstByUsername(username);
            }
        } catch (Exception e) {
            throw new BadRequestException("Invalid authorization token.", e);
        }
        throw new BadRequestException("Authorization token is required.");
    }

    /**
     * Build RazorPayRequest object with necessary details.
     */
    private BeanRazorPayRequest.RazorPayRequest buildRazorPayRequest(BeanRazorPayCustomerRequest customerRequest,
                                                                     Customer loggedInCustomer, PaymentTransaction paymentTransaction) {
        BeanRazorPayRequest.RazorPayRequest requestObj = new BeanRazorPayRequest.RazorPayRequest();
        long currentTimeMillis = Instant.now().toEpochMilli();

        requestObj.setAmount(customerRequest.getAmount() * 100);
        requestObj.setCurrency(CURRENCY);
        requestObj.setAccept_partial(false);
        requestObj.setExpire_by(currentTimeMillis + EXPIRY_DURATION_MS);
        requestObj.setDescription("Payment For : " + paymentTransaction.getPaymentTransactionRefId());

        BeanRazorPayRequest.Customer customerDetails = new BeanRazorPayRequest.Customer();
        customerDetails.setContact("+91" + loggedInCustomer.getMobile());
        customerDetails.setEmail(loggedInCustomer.getEmail());
        customerDetails.setName(loggedInCustomer.fullName());

        requestObj.setCustomer(customerDetails);
        requestObj.setReference_id(paymentTransaction.getPaymentTransactionRefId());

        BeanRazorPayRequest.Notify notify = new BeanRazorPayRequest.Notify();
        notify.setEmail(false);
        notify.setSms(true);

        requestObj.setNotify(notify);
        requestObj.setReminder_enable(true);

        if (Const.SystemSetting.TestMode)
            requestObj.setCallback_url(CALLBACK_URL_TEST);
        else {
            requestObj.setCallback_url(CALLBACK_URL_LIVE);
        }
        requestObj.setCallback_method("get");

        return requestObj;
    }

    /**
     * Call RazorPay API to create a payment link.
     */
    private BeanRazorPayResponse.Root callRazorPayAPI(BeanRazorPayRequest.RazorPayRequest requestObj) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        if (Const.SystemSetting.TestMode)
            headers.setBasicAuth(razorPayUserNameTest, razorPayPasswordTest);
        else {
            headers.setBasicAuth(razorPayUserName, razorPayPassword);
        }

        HttpEntity<BeanRazorPayRequest.RazorPayRequest> entity = new HttpEntity<>(requestObj, headers);

        try {
            String responseBody = restTemplate.exchange(
                    razorPayPaymentLinksUrl, HttpMethod.POST, entity, String.class).getBody();

            return new Gson().fromJson(responseBody, new TypeToken<BeanRazorPayResponse.Root>() {
            }.getType());
        } catch (RestClientException e) {
            throw new RuntimeException("Failed to connect to RazorPay API: " + e.getMessage(), e);
        }
    }


    public PaymentGatewayStatusEnum handleWebhook(String signature, String payload) throws JsonProcessingException, BadRequestException {

        System.out.println(payload);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonPayload = mapper.readTree(payload);

        // Extract event type
        String eventType = jsonPayload.get("event").asText();
        JsonNode paymentLink = jsonPayload.get("payload").get("payment_link").get("entity");
        JsonNode order = jsonPayload.get("payload").get("order").get("entity");
        String razorPayOrderId = order.get("order_id").asText();

        PaymentTransaction paymentTransaction = paymentTransactionRepository.findByPaymentTransactionRefId(razorPayOrderId);
        if (paymentTransaction == null) {
            throw new BadRequestException(razorPayOrderId + " not found");
        }


        // Log incoming webhook event
        PaymentGatewayStatusEnum statusEnum = PaymentGatewayStatusEnum.created;
        switch (eventType) {
            case "payment_link.cancelled":
                statusEnum = processPaymentLinkCancelled(paymentLink, paymentTransaction.getPaymentGatewayRefId(), payload);
                System.out.println("**************payment_link.cancelled *********************************** ");

                break;
            case "payment_link.paid":
                System.out.println("**************payment_link.paid *********************************** ");
                statusEnum = processPaymentLinkPaid(paymentLink, paymentTransaction.getPaymentGatewayRefId(), payload);
                break;
            case "payment_link.expired":
                statusEnum = processPaymentLinkExpired(paymentLink, paymentTransaction.getPaymentGatewayRefId(), payload);
                System.out.println("**************payment_link.expired *********************************** ");
                break;
            default:
                System.out.println("Unhandled event type: {} " + eventType);
                break;
        }
        return statusEnum;


    }

    private PaymentGatewayStatusEnum processPaymentLinkCancelled(JsonNode paymentLink, String paymentGatewayRefId, String payload) throws BadRequestException {
        String id = paymentLink.get("id").asText();
        String status = paymentLink.get("status").asText();
        System.out.println("Payment Link Cancelled:");
        System.out.println("ID: " + id);
        System.out.println("Status: " + status);
        paymentTransactionService.updatePaymentTransactionWebhookAndStatus(paymentGatewayRefId, payload, PaymentGatewayStatusEnum.cancelled);
        return PaymentGatewayStatusEnum.cancelled;
    }

    private PaymentGatewayStatusEnum processPaymentLinkPaid(JsonNode paymentLink, String paymentGatewayRefId, String payload) throws BadRequestException {
        String id = paymentLink.get("id").asText();
        int amountPaid = paymentLink.get("amount_paid").asInt();
        String customerEmail = paymentLink.get("customer").get("email").asText();

        System.out.println("Payment Link Paid:");
        System.out.println("ID: " + id);
        System.out.println("Amount Paid: " + amountPaid);
        System.out.println("Customer Email: " + customerEmail);
        paymentTransactionService.updatePaymentTransactionWebhookAndStatus(paymentGatewayRefId, payload, PaymentGatewayStatusEnum.paid);
        return PaymentGatewayStatusEnum.paid;
    }

    private PaymentGatewayStatusEnum processPaymentLinkExpired(JsonNode paymentLink, String paymentGatewayRefId, String payload) throws BadRequestException {
        String id = paymentLink.get("id").asText();
        String status = paymentLink.get("status").asText();
        System.out.println("Payment Link Expired:");
        System.out.println("ID: " + id);
        System.out.println("Status: " + status);
        paymentTransactionService.updatePaymentTransactionWebhookAndStatus(paymentGatewayRefId, payload, PaymentGatewayStatusEnum.expired);
        return PaymentGatewayStatusEnum.expired;
    }


    public BeanRazorPayUpdateStatus.Root getStatusUpdate(String plinkId) {
        BeanRazorPayUpdateStatus.Root root = null;

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        if (Const.SystemSetting.TestMode)
            headers.setBasicAuth(razorPayUserNameTest, razorPayPasswordTest);
        else {
            headers.setBasicAuth(razorPayUserName, razorPayPassword);
        }

        HttpEntity<BeanRazorPayUpdateStatus.Root> entity = new HttpEntity<>(null, headers);
        String cartUuid = "";
        try {
            String responseBody = restTemplate.exchange(
                    "https://api.razorpay.com/v1/payment_links/" + plinkId, HttpMethod.GET, entity, String.class).getBody();
            root = new Gson().fromJson(responseBody, new TypeToken<BeanRazorPayUpdateStatus.Root>() {
            }.getType());
            PaymentGatewayStatusEnum paymentStatusEnum = PaymentGatewayStatusEnum.created;
            if (root.getStatus().equalsIgnoreCase("created")) {
                paymentStatusEnum = PaymentGatewayStatusEnum.created;
            } else if (root.getStatus().equalsIgnoreCase("paid")) {
                paymentStatusEnum = PaymentGatewayStatusEnum.paid;
            } else if (root.getStatus().equalsIgnoreCase("opened")) {
                paymentStatusEnum = PaymentGatewayStatusEnum.opened;
            } else if (root.getStatus().equalsIgnoreCase("expired")) {
                paymentStatusEnum = PaymentGatewayStatusEnum.expired;
            } else if (root.getStatus().equalsIgnoreCase("failed")) {
                paymentStatusEnum = PaymentGatewayStatusEnum.failed;
            } else if (root.getStatus().equalsIgnoreCase("under_verification")) {
                paymentStatusEnum = PaymentGatewayStatusEnum.under_verification;
            } else if (root.getStatus().equalsIgnoreCase("cancelled")) {
                paymentStatusEnum = PaymentGatewayStatusEnum.cancelled;
            }
            cartUuid = paymentTransactionService.updatePaymentStatusByPaymentGatewayId(root.getId(), paymentStatusEnum, root.toString());
        } catch (RestClientException | BadRequestException e) {
            throw new RuntimeException("Failed to connect to RazorPay API: " + e.getMessage(), e);
        }
        root.setCartUuid(cartUuid);
        return root;

    }

    public BeanRazorPayUpdateStatus.Root getCancelUpdate(String plinkId) {
        BeanRazorPayUpdateStatus.Root root = null;

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBasicAuth(razorPayUserName, razorPayPassword);

        HttpEntity<BeanRazorPayUpdateStatus.Root> entity = new HttpEntity<>(null, headers);

        try {
            String responseBody = restTemplate.exchange(
                    "https://api.razorpay.com/v1/payment_links/" + plinkId + "/cancel", HttpMethod.POST, entity, String.class).getBody();
            root = new Gson().fromJson(responseBody, new TypeToken<BeanRazorPayUpdateStatus.Root>() {
            }.getType());
            PaymentGatewayStatusEnum paymentStatusEnum = PaymentGatewayStatusEnum.created;
            if (root.getStatus().equalsIgnoreCase("created")) {
                paymentStatusEnum = PaymentGatewayStatusEnum.created;
            } else if (root.getStatus().equalsIgnoreCase("paid")) {
                paymentStatusEnum = PaymentGatewayStatusEnum.paid;
            } else {
                paymentStatusEnum = PaymentGatewayStatusEnum.cancelled;
            }
            paymentTransactionService.updatePaymentStatusByPaymentGatewayId(root.getId(), paymentStatusEnum, root.toString());
        } catch (RestClientException | BadRequestException e) {
            throw new RuntimeException("Failed to connect to RazorPay API: " + e.getMessage(), e);
        }
        return root;

    }

    public void refundOrder(String orderId, OrderDto.CancelOrder cancelOrder) throws BadRequestException {
        List<PaymentTransaction> paymentTransactionList = paymentTransactionRepository.findByOrderId(orderId);
        paymentTransactionList.sort((pt1, pt2) -> pt2.getCreatedAt().compareTo(pt1.getCreatedAt()));
        PaymentTransaction paymentTransaction = paymentTransactionList.get(0);
//        Gson gson = new Gson();
//        JsonObject responseJson = gson.fromJson(paymentTransaction.getPaymentResponseData(), JsonObject.class);
//        JsonArray paymentsArray = responseJson.getAsJsonArray("payments");
//        JsonObject payment = paymentsArray.get(0).getAsJsonObject();

        // Retrieve the payment_id
//        String paymentId = payment.get("payment_id").getAsString();
        String paymentId = null;
        String paymentResponseData = paymentTransaction.getPaymentResponseData();
        Pattern pattern = Pattern.compile("payment_id=([\\w-]+)");
        Matcher matcher = pattern.matcher(paymentResponseData);

        if (matcher.find()) {
             paymentId = matcher.group(1);  // Extract the payment_id
        } else {
            throw new BadRequestException("Payment ID not Found");
        }

        BeanRazorPayUpdateStatus.RefundResponse root = null;
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        if (Const.SystemSetting.TestMode)
            headers.setBasicAuth(razorPayUserNameTest, razorPayPasswordTest);
        else {
            headers.setBasicAuth(razorPayUserName, razorPayPassword);
        }
        String refundRequestBody = "{ \"amount\": " + paymentTransaction.getAmount() + " }"; // Amount in paise

        HttpEntity<String> entity = new HttpEntity<>(refundRequestBody, headers);

        String responseBody = restTemplate.exchange(
                "https://api.razorpay.com/v1/payments/" + paymentId + "/refund",
                HttpMethod.POST,
                entity,
                String.class).getBody();
        root = new Gson().fromJson(responseBody, new TypeToken<BeanRazorPayUpdateStatus.RefundResponse>() {
        }.getType());

        PaymentGatewayStatusEnum paymentStatusEnum = PaymentGatewayStatusEnum.created;
        if (root.getStatus().equalsIgnoreCase("processed")) {
            paymentStatusEnum = PaymentGatewayStatusEnum.Refunded;
        } else if (root.getStatus().equalsIgnoreCase("pending")) {
            paymentStatusEnum = PaymentGatewayStatusEnum.RefundPending;
        } else {
            paymentStatusEnum = PaymentGatewayStatusEnum.RefundFailed;
        }
        paymentTransaction.setPaymentStatus(paymentStatusEnum);
        paymentTransactionRepository.save(paymentTransaction);
    }
}

