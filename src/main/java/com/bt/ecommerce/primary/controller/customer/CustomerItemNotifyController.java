package com.bt.ecommerce.primary.controller.customer;

import com.bt.ecommerce.annotation.TranslateResponseMessage;
import com.bt.ecommerce.bean.ResponsePacket;
import com.bt.ecommerce.exception.BadRequestException;
import com.bt.ecommerce.primary.controller._BaseController;
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
@RequestMapping("/guest-customer/v1/item-notify")
public class CustomerItemNotifyController extends _BaseController {

    @TranslateResponseMessage
    @PostMapping("/save-notify")
    public ResponseEntity<ResponsePacket> saveNotify(
            @Valid @RequestBody StockInNotificationDto.SaveItemNotification update) throws BadRequestException {
        stockInNotificationService.saveItemNotify(update);
        return new ResponseEntity<>(ResponsePacket.builder()
                .errorCode(0)
                .message("Your interested is noted.")
                .build(), HttpStatus.OK);
    }
}
