package com.bt.ecommerce.primary.controller.admin;

import com.bt.ecommerce.annotation.TranslateResponseMessage;
import com.bt.ecommerce.bean.ResponsePacket;
import com.bt.ecommerce.configuration.SpringBeanContext;
import com.bt.ecommerce.exception.BadRequestException;
import com.bt.ecommerce.primary.controller._BaseController;
import com.bt.ecommerce.primary.pojo.user.SystemUser;
import com.bt.ecommerce.security.JwtUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/admin/v1/stock-in-notification")
public class StockInNotificationController extends _BaseController {

//    @TranslateResponseMessage
//    @PostMapping("/save")
//    protected ResponseEntity<ResponsePacket> save(@Valid @RequestBody StockInNotificationDto.SaveCustomerItemNotification create) throws BadRequestException {
//        return new ResponseEntity<>(ResponsePacket.builder()
//                .errorCode(0)
//                .message("ecommerce.common.message.save")
//                .responsePacket(customerItemNotificationService.save(create))
//                .build(), HttpStatus.OK);
//    }
//
//    @TranslateResponseMessage
//    @PostMapping("/saveLoggedInCustomerRequest")
//    protected ResponseEntity<ResponsePacket> saveLoggedInCustomerRequest(@Valid @RequestBody StockInNotificationDto.SaveCustomerItemNotification create) throws BadRequestException {
//        Customer loggedInUser = (Customer) SpringBeanContext.getBean(JwtUserDetailsService.class).getLoggedInUser();
//        return new ResponseEntity<>(ResponsePacket.builder()
//                .errorCode(0)
//                .message("ecommerce.common.message.save")
//                .responsePacket(customerItemNotificationService.save(loggedInUser, create))
//                .build(), HttpStatus.OK);
//    }

    @TranslateResponseMessage
    @GetMapping("/list-data/{data}")
    protected ResponseEntity<ResponsePacket> listData(@PathVariable("data") String data) throws BadRequestException {
        SystemUser loggedInUser = (SystemUser) SpringBeanContext.getBean(JwtUserDetailsService.class).getLoggedInUser();
        return new ResponseEntity<>(ResponsePacket.builder()
                .errorCode(0)
                .message("ecommerce.common.message.get_all")
                .responsePacket(stockInNotificationService.listData(data))
                .build(), HttpStatus.OK);
    }


    @TranslateResponseMessage
    @PutMapping("/notifyToCustomer/{uuid}")
    protected ResponseEntity<ResponsePacket> inactivate(@PathVariable("uuid") String uuid) throws BadRequestException {
        SystemUser loggedInUser = (SystemUser) SpringBeanContext.getBean(JwtUserDetailsService.class).getLoggedInUser();
        stockInNotificationService.notifyToCustomer(uuid);
        return new ResponseEntity<>(ResponsePacket.builder()
                .errorCode(0)
                .message("ecommerce.common.message.inactive")
                .build(), HttpStatus.OK);
    }

    @TranslateResponseMessage
    @DeleteMapping("/delete/{uuid}")
    protected ResponseEntity<ResponsePacket> delete(@PathVariable("uuid") String uuid) throws BadRequestException {
        SystemUser loggedInUser = (SystemUser) SpringBeanContext.getBean(JwtUserDetailsService.class).getLoggedInUser();
        stockInNotificationService.delete(uuid);
        return new ResponseEntity<>(ResponsePacket.builder()
                .errorCode(0)
                .message("ecommerce.common.message.deleted")
                .build(), HttpStatus.OK);
    }

}
