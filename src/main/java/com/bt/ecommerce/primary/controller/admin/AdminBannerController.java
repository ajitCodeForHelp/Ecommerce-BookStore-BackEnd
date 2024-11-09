package com.bt.ecommerce.primary.controller.admin;

import com.bt.ecommerce.annotation.TranslateResponseMessage;
import com.bt.ecommerce.bean.ResponsePacket;
import com.bt.ecommerce.configuration.SpringBeanContext;
import com.bt.ecommerce.exception.BadRequestException;
import com.bt.ecommerce.primary.controller._BaseController;
import com.bt.ecommerce.primary.dto.BannerDto;
import com.bt.ecommerce.primary.pojo.user.SystemUser;
import com.bt.ecommerce.security.JwtUserDetailsService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/admin/v1/banner")
public class AdminBannerController extends _BaseController {

    @TranslateResponseMessage
    @PostMapping("/create")
    public ResponseEntity<ResponsePacket> createBanner(@Valid @RequestBody BannerDto.CreateBanner banner) throws BadRequestException {
        SystemUser loggedInUser = (SystemUser) SpringBeanContext.getBean(JwtUserDetailsService.class).getLoggedInUser();
        return new ResponseEntity<>(ResponsePacket.builder()
                .errorCode(0)
                .message("ecommerce.common.message.save")
                .responsePacket(bannerService.save(banner))
                .build(), HttpStatus.OK);
    }

    @TranslateResponseMessage
    @PutMapping("/update/{bannerUuid}")
    public ResponseEntity<ResponsePacket> updateBanner(@PathVariable("bannerUuid") String uuid, @Valid @RequestBody BannerDto.UpdateDetail cart) throws BadRequestException {
        SystemUser loggedInUser = (SystemUser) SpringBeanContext.getBean(JwtUserDetailsService.class).getLoggedInUser();
        bannerService.update(uuid, cart);
        return new ResponseEntity<>(ResponsePacket.builder()
                .errorCode(0)
                .message("ecommerce.common.message.update")
                .build(), HttpStatus.OK);
    }

    @TranslateResponseMessage
    @GetMapping("/list-data/{data}")
    protected ResponseEntity<ResponsePacket> listData(@PathVariable("data") String data) throws BadRequestException {
        SystemUser loggedInUser = (SystemUser) SpringBeanContext.getBean(JwtUserDetailsService.class).getLoggedInUser();
        return new ResponseEntity<>(ResponsePacket.builder()
                .errorCode(0)
                .message("ecommerce.common.message.get_all")
                .responsePacket(bannerService.list(data))
                .build(), HttpStatus.OK);
    }

    @TranslateResponseMessage
    @PostMapping("/list")
    protected ResponseEntity<ResponsePacket> list(@Valid @RequestBody BannerDto.GetList list) throws BadRequestException {
        SystemUser loggedInUser = (SystemUser) SpringBeanContext.getBean(JwtUserDetailsService.class).getLoggedInUser();
        return new ResponseEntity<>(ResponsePacket.builder()
                .errorCode(0)
                .message("ecommerce.common.message.get_all")
                .responsePacket(bannerService.list(false, list.getPageNumber(), list.getPageSize(), list.getSearch()))
                .build(), HttpStatus.OK);
    }

    @TranslateResponseMessage
    @PostMapping("/deleted-list")
    protected ResponseEntity<ResponsePacket> deletedList(@Valid @RequestBody BannerDto.GetList list) throws BadRequestException {
        SystemUser loggedInUser = (SystemUser) SpringBeanContext.getBean(JwtUserDetailsService.class).getLoggedInUser();
        return new ResponseEntity<>(ResponsePacket.builder()
                .errorCode(0)
                .message("ecommerce.common.message.get_all_deleted_records")
                .responsePacket(bannerService.list(true, list.getPageNumber(), list.getPageSize(), list.getSearch()))
                .build(), HttpStatus.OK);
    }

    @TranslateResponseMessage
    @PutMapping("/activate/{uuid}")
    protected ResponseEntity<ResponsePacket> activate(@PathVariable("uuid") String uuid) throws BadRequestException {
        SystemUser loggedInUser = (SystemUser) SpringBeanContext.getBean(JwtUserDetailsService.class).getLoggedInUser();
        bannerService.activate(uuid);
        return new ResponseEntity<>(ResponsePacket.builder()
                .errorCode(0)
                .message("ecommerce.common.message.active")
                .build(), HttpStatus.OK);
    }

    @TranslateResponseMessage
    @PutMapping("/inactivate/{uuid}")
    protected ResponseEntity<ResponsePacket> inactivate(@PathVariable("uuid") String uuid) throws BadRequestException {
        SystemUser loggedInUser = (SystemUser) SpringBeanContext.getBean(JwtUserDetailsService.class).getLoggedInUser();
        bannerService.inactivate(uuid);
        return new ResponseEntity<>(ResponsePacket.builder()
                .errorCode(0)
                .message("ecommerce.common.message.inactive")
                .build(), HttpStatus.OK);
    }

    @TranslateResponseMessage
    @DeleteMapping("/delete/{uuid}")
    protected ResponseEntity<ResponsePacket> delete(@PathVariable("uuid") String uuid) throws BadRequestException {
        SystemUser loggedInUser = (SystemUser) SpringBeanContext.getBean(JwtUserDetailsService.class).getLoggedInUser();
        bannerService.delete(uuid);
        return new ResponseEntity<>(ResponsePacket.builder()
                .errorCode(0)
                .message("ecommerce.common.message.deleted")
                .build(), HttpStatus.OK);
    }

    @TranslateResponseMessage
    @PutMapping("/revive/{uuid}")
    protected ResponseEntity<ResponsePacket> revive(@PathVariable("uuid") String uuid) throws BadRequestException {
        SystemUser loggedInUser = (SystemUser) SpringBeanContext.getBean(JwtUserDetailsService.class).getLoggedInUser();
        bannerService.revive(uuid);
        return new ResponseEntity<>(ResponsePacket.builder()
                .errorCode(0)
                .message("ecommerce.common.message.revive")
                .build(), HttpStatus.OK);
    }


}
