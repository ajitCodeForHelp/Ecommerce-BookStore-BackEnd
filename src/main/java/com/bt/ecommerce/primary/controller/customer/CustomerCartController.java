package com.bt.ecommerce.primary.controller.customer;

import com.bt.ecommerce.annotation.TranslateResponseMessage;
import com.bt.ecommerce.bean.ResponsePacket;
import com.bt.ecommerce.configuration.SpringBeanContext;
import com.bt.ecommerce.exception.BadRequestException;
import com.bt.ecommerce.primary.controller._BaseController;
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
@RequestMapping("/customer/v1/cart")
public class CustomerCartController extends _BaseController {


    @TranslateResponseMessage
    @GetMapping({"/getCartDetail", "/getCartDetail/{cartUuid}"})
    public ResponseEntity<ResponsePacket> getCartDetail(@PathVariable(value = "cartUuid", required = false) String cartUuid) throws BadRequestException {
        return new ResponseEntity<>(ResponsePacket.builder()
                .errorCode(0)
                .message("ecommerce.common.message.get")
                .responsePacket(cartService.getCartDetail(cartUuid))
                .build(), HttpStatus.OK);
    }

//    @TranslateResponseMessage
//    @PostMapping("/createCart")
//    public ResponseEntity<ResponsePacket> createCart(@Valid @RequestBody CartDto.UpdateCart cart) throws BadRequestException {
//        return new ResponseEntity<>(ResponsePacket.builder()
//                .errorCode(0)
//                .message("ecommerce.common.message.save")
//                .responsePacket(cartService.createCart(cart))
//                .build(), HttpStatus.OK);
//    }

    @TranslateResponseMessage
    @PutMapping("/updateCart/{cartUuid}")
    public ResponseEntity<ResponsePacket> updateCart(@PathVariable("cartUuid") String uuid, @Valid @RequestBody CartDto.UpdateCart cart) throws BadRequestException {
        cartService.updateCart(uuid, cart);
        return new ResponseEntity<>(ResponsePacket.builder()
                .errorCode(0)
                .message("ecommerce.common.message.update")
                .build(), HttpStatus.OK);
    }

    @TranslateResponseMessage
    @PutMapping("/removeItemFromCart/{cartUuid}/{itemUuid}")
    public ResponseEntity<ResponsePacket> removeItemFromCart(@PathVariable("cartUuid") String cardUuid,
                                                             @PathVariable("itemUuid") String itemUuid) throws BadRequestException {
        cartService.removeItemFromCart(cardUuid, itemUuid);
        return new ResponseEntity<>(ResponsePacket.builder()
                .errorCode(0)
                .message("ecommerce.common.message.cart_item_removed")
                .build(), HttpStatus.OK);
    }

    @TranslateResponseMessage
    @PutMapping("/clearCart/{cartUuid}")
    public ResponseEntity<ResponsePacket> clearCart(@PathVariable("cartUuid") String cartUuid) {
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
                                                             @PathVariable("itemQuantity") long itemQuantity) throws BadRequestException {
        cartService.updateItemQuantity(cartUuid, itemUuid, itemQuantity);
        return new ResponseEntity<>(ResponsePacket.builder()
                .errorCode(0)
                .message("ecommerce.common.message.update_item_quantity")
                .build(), HttpStatus.OK);
    }
}
