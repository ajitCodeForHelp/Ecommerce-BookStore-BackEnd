package com.bt.ecommerce.primary.controller;

import com.bt.ecommerce.annotation.TranslateResponseMessage;
import com.bt.ecommerce.bean.ResponsePacket;
import com.bt.ecommerce.exception.BadRequestException;
import com.bt.ecommerce.primary.dto.AuthDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController extends _BaseController {

    @TranslateResponseMessage
    @PostMapping("/admin-login")
    public ResponseEntity<ResponsePacket> adminLogin(HttpServletRequest request, @Valid @RequestBody AuthDto.AdminLogin login)
            throws BadRequestException {
        AuthDto.UserDetails userAdminAdminDetail = authService.loginAdmin(login.getUserName(), login.getPassword(), request.getRemoteAddr());
        return new ResponseEntity<>(ResponsePacket.builder()
                .errorCode(0)
                .message("ecommerce.common.message.login")
                .responsePacket(userAdminAdminDetail)
                .build(), HttpStatus.OK);
    }

    @TranslateResponseMessage
    @PostMapping("/customer-login")
    public ResponseEntity<ResponsePacket> customerLogin(HttpServletRequest request, @Valid @RequestBody AuthDto.CustomerLogin login)
            throws BadRequestException {
        AuthDto.UserDetails userCustomerDetails = authService.loginCustomer(login.getUserName(), login.getPassword(), request.getRemoteAddr());
        return new ResponseEntity<>(ResponsePacket.builder()
                .errorCode(0)
                .message("ecommerce.common.message.login")
                .responsePacket(userCustomerDetails)
                .build(), HttpStatus.OK);
    }

}
