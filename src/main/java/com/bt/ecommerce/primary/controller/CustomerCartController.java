package com.bt.ecommerce.primary.controller;

import com.bt.ecommerce.annotation.TranslateResponseMessage;
import com.bt.ecommerce.bean.ResponsePacket;
import com.bt.ecommerce.configuration.SpringBeanContext;
import com.bt.ecommerce.exception.BadRequestException;
import com.bt.ecommerce.primary.dto.CartDto;
import com.bt.ecommerce.primary.pojo.user.SystemUser;
import com.bt.ecommerce.primary.pojo.user._BaseUser;
import com.bt.ecommerce.security.JwtUserDetailsService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/admin/v1/cart")
public class CustomerCartController extends _BaseController{

    @TranslateResponseMessage
    @PostMapping("/createCart")
    public ResponseEntity<ResponsePacket> createCart(@Valid @RequestBody CartDto.createCart cart) throws BadRequestException {
        SystemUser loggedInUser = (SystemUser) SpringBeanContext.getBean(JwtUserDetailsService.class).getLoggedInUser();
        return new ResponseEntity<>(ResponsePacket.builder()
                .errorCode(0)
                .message("ecommerce.common.message.save")
                .responsePacket(cartService.save(cart))
                .build(), HttpStatus.OK);
    }
}
