package com.bt.ecommerce.primary.controller.customer;

import com.bt.ecommerce.annotation.TranslateResponseMessage;
import com.bt.ecommerce.bean.ResponsePacket;
import com.bt.ecommerce.exception.BadRequestException;
import com.bt.ecommerce.primary.controller._BaseController;
import com.bt.ecommerce.primary.dto.CartDto;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/guest-customer/v1/cart")
public class CustomerCartController extends _BaseController {

    @TranslateResponseMessage
    @GetMapping("/getCartItemCount/{cartUuid}")
    public ResponseEntity<ResponsePacket> getCartItemCount(
            @PathVariable(value = "cartUuid") String cartUuid) throws BadRequestException {
        return new ResponseEntity<>(ResponsePacket.builder()
                .errorCode(0)
                .message("ecommerce.common.message.get")
                .responsePacket(cartService.getCartItemCount(cartUuid))
                .build(), HttpStatus.OK);
    }

    @TranslateResponseMessage
//    @GetMapping({"/getCartDetail", "/getCartDetail/{cartUuid}"})
    @GetMapping("/getCartDetail/{deviceId}")
    public ResponseEntity<ResponsePacket> getCartDetail(@RequestHeader(value = "Authorization", required = false) String authorizationToken,
                                                        @PathVariable(value = "deviceId" ,required = false) String deviceId) throws BadRequestException {
        return new ResponseEntity<>(ResponsePacket.builder()
                .errorCode(0)
                .message("ecommerce.common.message.get")
                .responsePacket(cartService.getCartDetail(authorizationToken, deviceId))
                .build(), HttpStatus.OK);
    }

    @TranslateResponseMessage
    @PutMapping("/updateCart/{deviceId}/{cartUuid}")
    public ResponseEntity<ResponsePacket> updateCart(@RequestHeader(value = "Authorization", required = false) String authorizationToken,
                                                     @PathVariable(value = "deviceId", required = false) String deviceId,
                                                     @PathVariable("cartUuid") String uuid,
                                                     @Valid @RequestBody CartDto.UpdateCart cart) throws BadRequestException {
        return new ResponseEntity<>(ResponsePacket.builder()
                .errorCode(0)
                .message("ecommerce.common.message.update")
                .responsePacket(cartService.updateCart(authorizationToken, deviceId, uuid, cart))
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
    @PutMapping("/applyCouponCode/{cartUuid}/{couponCodeUuid}")
    public ResponseEntity<ResponsePacket> applyCouponCode(@PathVariable("cartUuid") String cardUuid,
                                                             @PathVariable("couponCodeUuid") String couponCodeUuid) throws BadRequestException {
        cartService.applyCouponCode(cardUuid, couponCodeUuid);
        return new ResponseEntity<>(ResponsePacket.builder()
                .errorCode(0)
                .message("ecommerce.common.message.cart_coupon_code_apply")
                .build(), HttpStatus.OK);
    }

    @TranslateResponseMessage
    @PutMapping("/removeCouponCode/{cartUuid}")
    public ResponseEntity<ResponsePacket> removeCouponCode(@PathVariable("cartUuid") String cardUuid) throws BadRequestException {
        cartService.removeCouponCode(cardUuid);
        return new ResponseEntity<>(ResponsePacket.builder()
                .errorCode(0)
                .message("ecommerce.common.message.cart_coupon_code_remove")
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
        return new ResponseEntity<>(ResponsePacket.builder()
                .errorCode(0)
                .responsePacket(cartService.updateItemQuantity(cartUuid, itemUuid, itemQuantity))
                .message("ecommerce.common.message.update_item_quantity")
                .build(), HttpStatus.OK);
    }

}
