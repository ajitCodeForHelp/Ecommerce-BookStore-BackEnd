package com.bt.ecommerce.primary.controller.customer;

import com.bt.ecommerce.annotation.TranslateResponseMessage;
import com.bt.ecommerce.bean.ResponsePacket;
import com.bt.ecommerce.primary.controller._BaseController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/guest-customer/v1/homePage")
public class EcommerceController extends _BaseController {

    @TranslateResponseMessage
    @GetMapping("/homepage-data")
    protected ResponseEntity<ResponsePacket> homepageData() {
        return new ResponseEntity<>(ResponsePacket.builder()
                .errorCode(0)
                .message("ecommerce.common.message.homepage_data")
                .responsePacket(ecommerceService.getHomepageData())
                .build(), HttpStatus.OK);
    }

    @TranslateResponseMessage
    @GetMapping("/get-category-item-details/{categoryUuid}")
    protected ResponseEntity<ResponsePacket> getCategoryItemDetails(@PathVariable("categoryUuid") String categoryUuid) {
        return new ResponseEntity<>(ResponsePacket.builder()
                .errorCode(0)
                .message("ecommerce.common.message.category_item_details")
                .responsePacket(ecommerceService.getCategoryItemDetails(categoryUuid))
                .build(), HttpStatus.OK);
    }

    @TranslateResponseMessage
    @GetMapping("/item-search")
    protected ResponseEntity<ResponsePacket> itemSearch(String search) {
        return new ResponseEntity<>(ResponsePacket.builder()
                .errorCode(0)
                .message("Item Search Data")
                .responsePacket(itemService.itemSearch(search))
                .build(), HttpStatus.OK);
    }

    @TranslateResponseMessage
    @GetMapping("/banner-data")
    protected ResponseEntity<ResponsePacket> bannerData() {
        return new ResponseEntity<>(ResponsePacket.builder()
                .errorCode(0)
                .message("ecommerce.common.message.bannerData")
                .responsePacket(bannerService.list("Active"))
                .build(), HttpStatus.OK);
    }

}