package com.bt.ecommerce.primary.razorpay;

import com.bt.ecommerce.bean.ResponsePacket;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
/*
curl --location 'localhost:8089/v1/api/razorPay/createPayment' \
--header 'Content-Type: application/json' \
--data '{

}'
 */
@RestController
@RequestMapping("v1/api/razorPay")
public class RazorPayController  {

    private static final Logger logger = LogManager.getLogger(RazorPayController.class);

    @Autowired
    protected RazorPayService razorPayService;

    @PostMapping(value = "/createPayment")
    public @ResponseBody
    ResponseEntity<ResponsePacket> generateToken(@RequestBody BeanRazorPayRequest.RazorPayRequest requestPacket) {
        logger.info("RazorPayController.createPayment");
        ResponsePacket responsePacket;
        try {

            responsePacket = new ResponsePacket<>(0, "payment Created", razorPayService.createPayment(requestPacket));
        } catch (Exception e) {
            responsePacket = new ResponsePacket<>(1, e.getMessage(), null);
        }
        return new ResponseEntity<>(responsePacket, HttpStatus.OK);
    }

//    @PostMapping(value = "/webHookTransaction")
//    public @ResponseBody
//    ResponseEntity<ResponsePacket> webHookTransaction(@RequestBody BeanWebhookResponse.Transaction webhookTransaction) {
//        logger.info("RazorPayController.webHookTransaction");
//        ResponsePacket responsePacket;
//        try {
//
//            responsePacket = new ResponsePacket<>(0, "web-Hook Transaction", razorPayService.webHookTransaction(webhookTransaction));
//        } catch (Exception e) {
//            responsePacket = new ResponsePacket<>(1, e.getMessage(), null);
//        }
//        return new ResponseEntity<>(responsePacket, HttpStatus.OK);
//    }

    @PostMapping(value = "/webHookTransaction")
    public ResponseEntity<String> handleWebhook(
            @RequestHeader("X-Razorpay-Signature") String signature,
            @RequestBody String payload
    ) {
        try {
            // Verify the signature
//            String razorpaySecret = "your_razorpay_secret";
//            boolean isValid = RazorpaySignatureVerifier.verifySignature(payload, signature, razorpaySecret);

//            if (!isValid) {
//                System.out.println("Invalid signature. Possible tampering detected.");
//                return ResponseEntity.status(400).body("Invalid signature");
//            }

            // Parse payload as JSON
            System.out.println(payload);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonPayload = mapper.readTree(payload);

            // Extract event type
            String eventType = jsonPayload.get("event").asText();
            JsonNode paymentLink = jsonPayload.get("payload").get("payment_link").get("entity");

            // Handle event based on type
            switch (eventType) {
                case "payment_link.cancelled":
                    processPaymentLinkCancelled(paymentLink);
                    break;
                case "payment_link.paid":
                    processPaymentLinkPaid(paymentLink);
                    break;
                case "payment_link.expired":
                    processPaymentLinkExpired(paymentLink);
                    break;
                default:
                    System.out.println("Unhandled event type: " + eventType);
            }

            return ResponseEntity.ok("Webhook received");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error handling webhook");
        }
    }

    private void processPaymentLinkCancelled(JsonNode paymentLink) {
        String id = paymentLink.get("id").asText();
        String status = paymentLink.get("status").asText();
        System.out.println("Payment Link Cancelled:");
        System.out.println("ID: " + id);
        System.out.println("Status: " + status);

        // Add your business logic here (e.g., update DB, notify user)
    }

    private void processPaymentLinkPaid(JsonNode paymentLink) {
        String id = paymentLink.get("id").asText();
        int amountPaid = paymentLink.get("amount_paid").asInt();
        String customerEmail = paymentLink.get("customer").get("email").asText();

        System.out.println("Payment Link Paid:");
        System.out.println("ID: " + id);
        System.out.println("Amount Paid: " + amountPaid);
        System.out.println("Customer Email: " + customerEmail);

        // Add your business logic here (e.g., mark as paid in DB, issue invoice)
    }

    private void processPaymentLinkExpired(JsonNode paymentLink) {
        String id = paymentLink.get("id").asText();
        String status = paymentLink.get("status").asText();

        System.out.println("Payment Link Expired:");
        System.out.println("ID: " + id);
        System.out.println("Status: " + status);

        // Add your business logic here (e.g., notify user about expiration)
    }
}
