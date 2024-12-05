package com.bt.ecommerce.primary.controller.customer;

import com.bt.ecommerce.annotation.TranslateResponseMessage;
import com.bt.ecommerce.bean.ResponsePacket;
import com.bt.ecommerce.exception.BadRequestException;
import com.bt.ecommerce.primary.controller._BaseController;
import com.bt.ecommerce.primary.dto.OtpDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/customer/v1/otp")
public class CustomerOtpController extends _BaseController {
    @TranslateResponseMessage
    @PostMapping("/sendOtpUsingOtpLess")
    public ResponseEntity<ResponsePacket> sendOtpUsingOtpLess(@RequestBody OtpDto.OTPLess otpBean) throws BadRequestException, IOException {
        customerService.sendOtpUsingOtpLess(otpBean.getIsdCode(), otpBean.getPhoneNumber());
        return new ResponseEntity<>(ResponsePacket.builder()
                .errorCode(0)
                .message("ecommerce.common.message.")
                .build(), HttpStatus.OK);
    }

    @TranslateResponseMessage
    @PostMapping("/verifyOtpUsingOtpLess")
    public ResponseEntity<ResponsePacket> verifyOtpUsingOtpLess(@RequestBody OtpDto.OTPVerify otpVerify) throws BadRequestException, IOException {
        customerService.verifyOtpUsingOtpLess(otpVerify);
        return new ResponseEntity<>(ResponsePacket.builder()
                .errorCode(0)
                .message("ecommerce.common.message.")
                .build(), HttpStatus.OK);
    }
}
