package com.bt.ecommerce.primary.controller;

import com.bt.ecommerce.annotation.TranslateResponseMessage;
import com.bt.ecommerce.bean.ResponsePacket;
import com.bt.ecommerce.configuration.SpringBeanContext;
import com.bt.ecommerce.exception.BadRequestException;
import com.bt.ecommerce.primary.dto.CartDto;
import com.bt.ecommerce.primary.pojo.user.SystemUser;
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
    public ResponseEntity<ResponsePacket> createCart(@Valid @RequestBody CartDto.CreateCart cart) throws BadRequestException {
        SystemUser loggedInUser = (SystemUser) SpringBeanContext.getBean(JwtUserDetailsService.class).getLoggedInUser();
        return new ResponseEntity<>(ResponsePacket.builder()
                .errorCode(0)
                .message("ecommerce.common.message.save")
                .responsePacket(cartService.save(cart))
                .build(), HttpStatus.OK);
    }

    @TranslateResponseMessage
    @PutMapping("/updateCart/{cartUuid}")
    public ResponseEntity<ResponsePacket> updateCart(@PathVariable("cartUuid") String uuid,@Valid @RequestBody CartDto.UpdateCart cart) throws BadRequestException {
        SystemUser loggedInUser = (SystemUser) SpringBeanContext.getBean(JwtUserDetailsService.class).getLoggedInUser();
        cartService.update(uuid, cart);
        return new ResponseEntity<>(ResponsePacket.builder()
                .errorCode(0)
                .message("ecommerce.common.message.update")
                .build(), HttpStatus.OK);
    }

    @TranslateResponseMessage
    @PutMapping("/removeItemFromCart/{cartUuid}/{itemUuid}")
    public ResponseEntity<ResponsePacket> removeItemFromCart(@PathVariable("cartUuid") String cardUuid,
                                                             @PathVariable("itemUuid") String itemUuid){
        SystemUser loggedInUser = (SystemUser) SpringBeanContext.getBean(JwtUserDetailsService.class).getLoggedInUser();
        cartService.removeItemFromCart(cardUuid, itemUuid);
        return new ResponseEntity<>(ResponsePacket.builder()
                .errorCode(0)
                .message("ecommerce.common.message.cart_item_removed")
                .build(), HttpStatus.OK);
    }

    @TranslateResponseMessage
    @PutMapping("/clearCart/{cartUuid}")
    public ResponseEntity<ResponsePacket> clearCart(@PathVariable("cartUuid") String cartUuid){
        SystemUser loggedInUser = (SystemUser) SpringBeanContext.getBean(JwtUserDetailsService.class).getLoggedInUser();
        cartService.clearCart(cartUuid);
        return new ResponseEntity<>(ResponsePacket.builder()
                .errorCode(0)
                .message("ecommerce.common.message.clear_cart")
                .build(), HttpStatus.OK);
    }

    @TranslateResponseMessage
    @PutMapping("/updateItemQuantity/{cartUuid}/{itemUuid}/{itemQuantity}")
    public ResponseEntity<ResponsePacket> updateItemQuantity(@PathVariable("cartUuid") String cartUuid,
                                                             @PathVariable("itemUuid") String itemUuid,
                                                             @PathVariable("itemQuantity") long itemQuantity){
        SystemUser loggedInUser = (SystemUser) SpringBeanContext.getBean(JwtUserDetailsService.class).getLoggedInUser();
        cartService.updateItemQuantity(cartUuid,itemUuid,itemQuantity);
        return new ResponseEntity<>(ResponsePacket.builder()
                .errorCode(0)
                .message("ecommerce.common.message.update_item_quantity")
                .build(), HttpStatus.OK);
    }
}
