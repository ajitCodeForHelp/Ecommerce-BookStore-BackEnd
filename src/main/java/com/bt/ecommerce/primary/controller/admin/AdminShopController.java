package com.bt.ecommerce.primary.controller.admin;
import com.bt.ecommerce.annotation.TranslateResponseMessage;
import com.bt.ecommerce.bean.ResponsePacket;
import com.bt.ecommerce.exception.BadRequestException;
import com.bt.ecommerce.primary.controller._BaseController;
import com.bt.ecommerce.primary.dto.ShopDto;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/admin/v1/shop")
public class AdminShopController extends _BaseController {

    @TranslateResponseMessage
    @PostMapping("/save")
    protected ResponseEntity<ResponsePacket> save(@Valid @RequestBody ShopDto.Save create) throws BadRequestException {
        return new ResponseEntity<>(ResponsePacket.builder()
                .errorCode(0)
                .message("ecommerce.common.message.save")
                .responsePacket(shopService.save(create))
                .build(), HttpStatus.OK);
    }
}