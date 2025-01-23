package com.bt.ecommerce.primary.controller.admin;


import com.bt.ecommerce.annotation.TranslateResponseMessage;
import com.bt.ecommerce.bean.ResponsePacket;
import com.bt.ecommerce.exception.BadRequestException;
import com.bt.ecommerce.primary.controller._BaseController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/admin/v1/otp")
public class AdminOTPController extends _BaseController {
    @TranslateResponseMessage
    @PostMapping("/getOTPList")
    public ResponseEntity<ResponsePacket> getOTPList() throws BadRequestException {
        return new ResponseEntity<>(ResponsePacket.builder()
                .errorCode(0)
                .message("ecommerce.common.message.get_all")
                .responsePacket(oneTimePasswordService.oneTimePasswordDtoList())
                .build(), HttpStatus.OK);
    }


}
