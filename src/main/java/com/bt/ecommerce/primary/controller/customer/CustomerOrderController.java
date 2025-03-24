package com.bt.ecommerce.primary.controller.customer;

import com.bt.ecommerce.annotation.TranslateResponseMessage;
import com.bt.ecommerce.bean.ResponsePacket;
import com.bt.ecommerce.exception.BadRequestException;
import com.bt.ecommerce.primary.controller._BaseController;
import com.bt.ecommerce.primary.dto.OrderDto;
import com.bt.ecommerce.primary.dto.StockInNotificationDto;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/customer/v1/order")
public class CustomerOrderController extends _BaseController {

    @TranslateResponseMessage
    @PostMapping("/placeOrder/{cartUuid}")
    public ResponseEntity<ResponsePacket> placeOrder(@PathVariable("cartUuid") String cartUuid, @Valid @RequestBody OrderDto.PlaceOrder placeOrder) throws BadRequestException {
        cartService.placeOrder(cartUuid,placeOrder);
        return new ResponseEntity<>(ResponsePacket.builder()
                .errorCode(0)
                .message("ecommerce.common.message.place_order")
                .build(), HttpStatus.OK);
    }

    @TranslateResponseMessage
    @GetMapping("/getPaymentStatus/{paymentGatewayId}")
    public ResponseEntity<ResponsePacket> getPaymentStatus(@PathVariable("paymentGatewayId") String paymentGatewayId) throws BadRequestException {
        return new ResponseEntity<>(ResponsePacket.builder()
                .errorCode(0)
                .message("ecommerce.common.message.payment_status")
                .responsePacket(paymentTransactionService.getPaymentStatus(paymentGatewayId))
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
                .responsePacket(orderService.getCustomerOrderList())
                .build(), HttpStatus.OK);
    }


}
