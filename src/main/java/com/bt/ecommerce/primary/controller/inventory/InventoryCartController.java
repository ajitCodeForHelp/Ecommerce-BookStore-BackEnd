package com.bt.ecommerce.primary.controller.inventory;

import com.bt.ecommerce.annotation.TranslateResponseMessage;
import com.bt.ecommerce.bean.ResponsePacket;
import com.bt.ecommerce.exception.BadRequestException;
import com.bt.ecommerce.primary.controller._BaseController;
import com.bt.ecommerce.primary.dto.CartDto;
import com.bt.ecommerce.primary.dto.InventoryCartDto;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/store/v1/inventory")
public class InventoryCartController extends _BaseController {
    @TranslateResponseMessage
    @GetMapping("/getInventoryCartDetail")
    public ResponseEntity<ResponsePacket> getCartDetail(@RequestHeader(value = "Authorization", required = false) String authorizationToken) throws BadRequestException {
        return new ResponseEntity<>(ResponsePacket.builder()
                .errorCode(0)
                .message("ecommerce.common.message.get")
                .responsePacket(inventoryCartService.getInventoryCartDetail(authorizationToken))
                .build(), HttpStatus.OK);
    }


    @TranslateResponseMessage
    @GetMapping("/getAllInventoryCartForOrder")
    public ResponseEntity<ResponsePacket> getAllInventoryCartForOrder(@RequestHeader(value = "Authorization", required = false) String authorizationToken) throws BadRequestException {
        return new ResponseEntity<>(ResponsePacket.builder()
                .errorCode(0)
                .message("ecommerce.common.message.get")
                .responsePacket(inventoryCartService.getInventoryCartListForOrder(authorizationToken))
                .build(), HttpStatus.OK);
    }
    @TranslateResponseMessage
    @PutMapping("/updateInventoryCart/{cartUuid}")
    public ResponseEntity<ResponsePacket> updateCart(@RequestHeader(value = "Authorization", required = false) String authorizationToken,
                                                     @PathVariable("cartUuid") String cartUuid,
                                                     @Valid @RequestBody InventoryCartDto.UpdateInventoryCart cart) throws BadRequestException {
        return new ResponseEntity<>(ResponsePacket.builder()
                .errorCode(0)
                .message("ecommerce.common.message.update")
                .responsePacket(inventoryCartService.updateCart(authorizationToken, cartUuid, cart))
                .build(), HttpStatus.OK);
    }

    @TranslateResponseMessage
    @PutMapping("/removeItemFromInventoryCart/{cartUuid}/{itemUuid}")
    public ResponseEntity<ResponsePacket> removeItemFromCart(@PathVariable("cartUuid") String cardUuid,
                                                             @PathVariable("itemUuid") String itemUuid) throws BadRequestException {
        return new ResponseEntity<>(ResponsePacket.builder()
                .errorCode(0)
                .responsePacket(inventoryCartService.removeItemFromCart(cardUuid, itemUuid))
                .message("ecommerce.common.message.cart_item_removed")
                .build(), HttpStatus.OK);
    }



    @TranslateResponseMessage
    @PutMapping("/clearInventoryCart/{cartUuid}")
    public ResponseEntity<ResponsePacket> clearCart(@PathVariable("cartUuid") String cartUuid) {
        inventoryCartService.clearCart(cartUuid);
        return new ResponseEntity<>(ResponsePacket.builder()
                .errorCode(0)
                .message("ecommerce.common.message.clear_cart")
                .build(), HttpStatus.OK);
    }

    @TranslateResponseMessage
    @PutMapping("/updateInventoryItemQuantity/{cartUuid}/{itemUuid}/{itemQuantity}")
    public ResponseEntity<ResponsePacket> updateItemQuantity(@PathVariable("cartUuid") String cartUuid,
                                                             @PathVariable("itemUuid") String itemUuid,
                                                             @PathVariable("itemQuantity") long itemQuantity) throws BadRequestException {
        return new ResponseEntity<>(ResponsePacket.builder()
                .errorCode(0)
                .responsePacket(inventoryCartService.updateItemQuantity(cartUuid, itemUuid, itemQuantity))
                .message("ecommerce.common.message.update_item_quantity")
                .build(), HttpStatus.OK);
    }

}
