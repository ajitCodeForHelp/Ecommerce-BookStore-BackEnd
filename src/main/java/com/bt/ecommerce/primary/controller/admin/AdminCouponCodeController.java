package com.bt.ecommerce.primary.controller.admin;

import com.bt.ecommerce.annotation.TranslateResponseMessage;
import com.bt.ecommerce.bean.ResponsePacket;
import com.bt.ecommerce.configuration.SpringBeanContext;
import com.bt.ecommerce.exception.BadRequestException;
import com.bt.ecommerce.primary.controller._BaseController;
import com.bt.ecommerce.primary.dto.CouponCodeDto;
import com.bt.ecommerce.primary.pojo.user.SystemUser;
import com.bt.ecommerce.security.JwtUserDetailsService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/admin/v1/couponCode")
public class AdminCouponCodeController extends _BaseController {

    @TranslateResponseMessage
    @PostMapping("/save")
    protected ResponseEntity<ResponsePacket> save(@Valid @RequestBody CouponCodeDto.SaveCoupon create) throws BadRequestException {
        SystemUser loggedInUser = (SystemUser) SpringBeanContext.getBean(JwtUserDetailsService.class).getLoggedInUser();
        return new ResponseEntity<>(ResponsePacket.builder()
                .errorCode(0)
                .message("ecommerce.common.message.save")
                .responsePacket(couponCodeService.save(create))
                .build(), HttpStatus.OK);
    }

    @TranslateResponseMessage
    @PutMapping("/update/{uuid}")
    protected ResponseEntity<ResponsePacket> edit(@PathVariable("uuid") String uuid,
                                                  @Valid @RequestBody CouponCodeDto.UpdateCoupon update) throws BadRequestException {
        SystemUser loggedInUser = (SystemUser) SpringBeanContext.getBean(JwtUserDetailsService.class).getLoggedInUser();
        couponCodeService.update(uuid, update);
        return new ResponseEntity<>(ResponsePacket.builder()
                .errorCode(0)
                .message("ecommerce.common.message.update")
                .build(), HttpStatus.OK);
    }

    @TranslateResponseMessage
    @GetMapping("/get/{uuid}")
    protected ResponseEntity<ResponsePacket> get(@PathVariable("uuid") String uuid) throws BadRequestException {
        SystemUser loggedInUser = (SystemUser) SpringBeanContext.getBean(JwtUserDetailsService.class).getLoggedInUser();
        return new ResponseEntity<>(ResponsePacket.builder()
                .errorCode(0)
                .message("ecommerce.common.message.get")
                .responsePacket(couponCodeService.get(uuid))
                .build(), HttpStatus.OK);
    }

    @TranslateResponseMessage
    @GetMapping("/list")
    protected ResponseEntity<ResponsePacket> listData() throws BadRequestException {
        SystemUser loggedInUser = (SystemUser) SpringBeanContext.getBean(JwtUserDetailsService.class).getLoggedInUser();
        return new ResponseEntity<>(ResponsePacket.builder()
                .errorCode(0)
                .message("ecommerce.common.message.get_all")
                .responsePacket(couponCodeService.couponList())
                .build(), HttpStatus.OK);
    }

    @TranslateResponseMessage
    @PutMapping("/activate/{uuid}")
    protected ResponseEntity<ResponsePacket> activate(@PathVariable("uuid") String uuid) throws BadRequestException {
        SystemUser loggedInUser = (SystemUser) SpringBeanContext.getBean(JwtUserDetailsService.class).getLoggedInUser();
        couponCodeService.activate(uuid);
        return new ResponseEntity<>(ResponsePacket.builder()
                .errorCode(0)
                .message("ecommerce.common.message.active")
                .build(), HttpStatus.OK);
    }

    @TranslateResponseMessage
    @PutMapping("/inactivate/{uuid}")
    protected ResponseEntity<ResponsePacket> inactivate(@PathVariable("uuid") String uuid) throws BadRequestException {
        SystemUser loggedInUser = (SystemUser) SpringBeanContext.getBean(JwtUserDetailsService.class).getLoggedInUser();
        couponCodeService.inactivate(uuid);
        return new ResponseEntity<>(ResponsePacket.builder()
                .errorCode(0)
                .message("ecommerce.common.message.inactive")
                .build(), HttpStatus.OK);
    }

    @TranslateResponseMessage
    @DeleteMapping("/delete/{uuid}")
    protected ResponseEntity<ResponsePacket> delete(@PathVariable("uuid") String uuid) throws BadRequestException {
        SystemUser loggedInUser = (SystemUser) SpringBeanContext.getBean(JwtUserDetailsService.class).getLoggedInUser();
        couponCodeService.delete(uuid);
        return new ResponseEntity<>(ResponsePacket.builder()
                .errorCode(0)
                .message("ecommerce.common.message.deleted")
                .build(), HttpStatus.OK);
    }
}
