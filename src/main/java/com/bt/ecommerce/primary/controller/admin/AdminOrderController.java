package com.bt.ecommerce.primary.controller.admin;

import com.bt.ecommerce.annotation.TranslateResponseMessage;
import com.bt.ecommerce.bean.ResponsePacket;
import com.bt.ecommerce.exception.BadRequestException;
import com.bt.ecommerce.primary.controller._BaseController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/admin/v1/order")
public class AdminOrderController extends _BaseController {

    @TranslateResponseMessage
    @GetMapping("/getCartDetail/{cartUuid}")
    public ResponseEntity<ResponsePacket> getCartDetail(@PathVariable(value = "cartUuid") String cartUuid) throws BadRequestException {
        return new ResponseEntity<>(ResponsePacket.builder()
                .errorCode(0)
                .message("ecommerce.common.message.get")
                .responsePacket(cartService.getCartDetail(cartUuid))
                .build(), HttpStatus.OK);
    }

    @TranslateResponseMessage
    @PostMapping("/getCartList")
    public ResponseEntity<ResponsePacket> getCartList() throws BadRequestException {
        return new ResponseEntity<>(ResponsePacket.builder()
                .errorCode(0)
                .message("ecommerce.common.message.get_all")
                .responsePacket(cartService.getCartList())
                .build(), HttpStatus.OK);
    }


    @TranslateResponseMessage
    @GetMapping("/getOrderDetail/{orderId}")
    public ResponseEntity<ResponsePacket> getOrderDetail(@PathVariable(value = "orderId") String orderId) throws BadRequestException {
        return new ResponseEntity<>(ResponsePacket.builder()
                .errorCode(0)
                .message("ecommerce.common.message.get")
                .responsePacket(orderService.getOrderDetail(orderId))
                .build(), HttpStatus.OK);
    }

    @TranslateResponseMessage
    @PostMapping("/getOrderList")
    public ResponseEntity<ResponsePacket> getOrderList() throws BadRequestException {
        return new ResponseEntity<>(ResponsePacket.builder()
                .errorCode(0)
                .message("ecommerce.common.message.get_all")
                .responsePacket(orderService.getOrderList())
                .build(), HttpStatus.OK);
    }


    // TODO > Order History 


}
