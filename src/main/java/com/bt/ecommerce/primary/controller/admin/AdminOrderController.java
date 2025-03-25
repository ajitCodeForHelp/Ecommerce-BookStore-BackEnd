package com.bt.ecommerce.primary.controller.admin;

import com.bt.ecommerce.annotation.TranslateResponseMessage;
import com.bt.ecommerce.bean.ResponsePacket;
import com.bt.ecommerce.exception.BadRequestException;
import com.bt.ecommerce.primary.controller._BaseController;
import com.bt.ecommerce.primary.dto.OrderDto;
import com.bt.ecommerce.primary.dto.OrderDto.UpdateOrdersTrackingIds;
import com.bt.ecommerce.primary.pojo.enums.OrderStatusEnum;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
                .responsePacket(cartService.get(cartUuid))
                .build(), HttpStatus.OK);
    }

    @TranslateResponseMessage
    @PostMapping("/getCartCount")
    public ResponseEntity<ResponsePacket> getCartCount() throws BadRequestException {
        return new ResponseEntity<>(ResponsePacket.builder()
                .errorCode(0)
                .message("ecommerce.common.message.get")
                .responsePacket(cartService.getCartCount())
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

    // ###############################ORDER API##############################

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
    @PutMapping("/updateOrderTrackingId/{orderId}/{orderTrackingId}")
    public ResponseEntity<ResponsePacket> updateOrderTrackingId(
            @PathVariable(value = "orderId") String orderId,
            @PathVariable(value = "orderTrackingId") String orderTrackingId) throws BadRequestException {
        orderService.updateOrderTrackingId(orderId, orderTrackingId);
        return new ResponseEntity<>(ResponsePacket.builder()
                .errorCode(0)
                .message("ecommerce.common.message.update")
                .build(), HttpStatus.OK);
    }

    @TranslateResponseMessage
    @PutMapping("/updateOrdersTrackingId")
    public ResponseEntity<ResponsePacket> updateOrdersTrackingId(
            @Valid @RequestBody List<UpdateOrdersTrackingIds> request) throws BadRequestException {
        orderService.updateOrdersTrackingId(request);
        return new ResponseEntity<>(ResponsePacket.builder()
                .errorCode(0)
                .message("ecommerce.common.message.update")
                .build(), HttpStatus.OK);
    }

    @TranslateResponseMessage
    @PutMapping("/updateOrderStatus/{orderId}/{orderStatus}")
    public ResponseEntity<ResponsePacket> updateOrderStatus(
            @PathVariable(value = "orderId") String orderId,
            @PathVariable(value = "orderStatus") OrderStatusEnum orderStatus) throws BadRequestException {
        orderService.updateOrderStatus(orderId, orderStatus);
        return new ResponseEntity<>(ResponsePacket.builder()
                .errorCode(0)
                .message("ecommerce.common.message.update")
                .build(), HttpStatus.OK);
    }


    @TranslateResponseMessage
    @PostMapping("/getOrderCount")
    public ResponseEntity<ResponsePacket> getOrderCount() throws BadRequestException {
        return new ResponseEntity<>(ResponsePacket.builder()
                .errorCode(0)
                .message("ecommerce.common.message.get")
                .responsePacket(orderService.getOrderCount())
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

    //  Admin > Order History
    @TranslateResponseMessage
    @PostMapping("/getOrderHistoryCount")
    public ResponseEntity<ResponsePacket> getOrderHistoryCount() {
        return new ResponseEntity<>(ResponsePacket.builder()
                .errorCode(0)
                .message("ecommerce.common.message.get")
                .responsePacket(orderHistoryService.getOrderHistoryCount())
                .build(), HttpStatus.OK);
    }

    @TranslateResponseMessage
    @PostMapping("/getOrderHistoryList")
    public ResponseEntity<ResponsePacket> getOrderHistoryList() {
        return new ResponseEntity<>(ResponsePacket.builder()
                .errorCode(0)
                .message("ecommerce.common.message.get_all")
                .responsePacket(orderHistoryService.orderHistoryList())
                .build(), HttpStatus.OK);
    }


    @TranslateResponseMessage
    @PostMapping("/cancelOrder/{orderId}")
    public ResponseEntity<ResponsePacket> cancelOrder(@PathVariable(value = "orderId") String orderId ,@RequestBody OrderDto.CancelOrder cancelOrder) throws BadRequestException, JsonProcessingException {
        orderService.cancelOrder(orderId,cancelOrder);
        return new ResponseEntity<>(ResponsePacket.builder()
                .errorCode(0)
                .message("ecommerce.common.message.get")
                .build(), HttpStatus.OK);
    }

}
