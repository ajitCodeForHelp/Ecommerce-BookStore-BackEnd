package com.bt.ecommerce.primary.controller.customer;

import com.bt.ecommerce.annotation.TranslateResponseMessage;
import com.bt.ecommerce.bean.ResponsePacket;
import com.bt.ecommerce.configuration.SpringBeanContext;
import com.bt.ecommerce.exception.BadRequestException;
import com.bt.ecommerce.primary.controller._BaseController;
import com.bt.ecommerce.primary.dto.CustomerDto;
import com.bt.ecommerce.primary.pojo.user.Customer;
import com.bt.ecommerce.security.JwtUserDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/guest-customer/v1")
public class OneTimePasswordController extends _BaseController {

    @TranslateResponseMessage
    @PostMapping("/generateOtp")
    protected ResponseEntity<ResponsePacket> generateOtp(@Valid @RequestBody CustomerDto.GenerateOtp generateOtp) throws BadRequestException {
        oneTimePasswordService.generateOtp(generateOtp);
        return new ResponseEntity<>(ResponsePacket.builder()
                .errorCode(0)
                .message("ecommerce.common.message.otp_sent")
                .build(), HttpStatus.OK);
    }

}
