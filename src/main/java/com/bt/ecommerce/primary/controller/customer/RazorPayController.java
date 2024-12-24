package com.bt.ecommerce.primary.controller.customer;

import com.bt.ecommerce.bean.ResponsePacket;
import com.bt.ecommerce.primary.pojo.enums.PaymentGatewayStatusEnum;
import com.bt.ecommerce.primary.razorpay.BeanRazorPayCustomerRequest;
import com.bt.ecommerce.primary.razorpay.BeanRazorPayUpdateStatus;
import com.bt.ecommerce.primary.razorpay.RazorPayService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("customer/v1/razorPay")
public class RazorPayController {
    private static final Logger logger = LogManager.getLogger(RazorPayController.class);
    @Autowired
    protected RazorPayService razorPayService;

    @PostMapping(value = "/createPayment")
    public @ResponseBody
    ResponseEntity<ResponsePacket> generateToken( @RequestBody BeanRazorPayCustomerRequest requestPacket) {
        logger.info("RazorPayController.createPayment");
        ResponsePacket responsePacket;
        try {

            responsePacket = new ResponsePacket<>(0, "payment Created", razorPayService.createPayment( requestPacket));
        } catch (Exception e) {
            responsePacket = new ResponsePacket<>(1, e.getMessage(), null);
        }
        return new ResponseEntity<>(responsePacket, HttpStatus.OK);
    }

    @PostMapping(value = "/webHookTransaction")
    public ResponseEntity<ResponsePacket> handleWebhook(
            @RequestHeader("X-Razorpay-Signature") String signature,
            @RequestBody String payload) {
        logger.info("RazorPayController.webHookTransaction");
        ResponsePacket responsePacket;
        try {
            PaymentGatewayStatusEnum responseStatus = razorPayService.handleWebhook(signature, payload);
            responsePacket = new ResponsePacket<>(0, "webHook-Transaction Created", responseStatus);
        } catch (Exception e) {
            responsePacket = new ResponsePacket<>(1, e.getMessage(), null);
        }
        return new ResponseEntity<>(responsePacket, HttpStatus.OK);
    }
    @GetMapping(value ="/updateStatus/{plinkId}")
    //
    public ResponseEntity<ResponsePacket> updateStatus(@PathVariable(value = "plinkId") String plinkId) {
        ResponsePacket responsePacket;
        try {
            BeanRazorPayUpdateStatus.Root responseStatus= razorPayService.getStatusUpdate(plinkId);
            responsePacket = new ResponsePacket<>(0, "response ", responseStatus);
        } catch (Exception e) {
            responsePacket = new ResponsePacket<>(1, e.getMessage(), null);
        }
        return new ResponseEntity<>(responsePacket, HttpStatus.OK);
    }
}
