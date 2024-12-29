package com.bt.ecommerce.primary.controller;

import com.bt.ecommerce.annotation.TranslateResponseMessage;
import com.bt.ecommerce.bean.ResponsePacket;
import com.bt.ecommerce.configuration.SpringBeanContext;
import com.bt.ecommerce.exception.BadRequestException;
import com.bt.ecommerce.primary.dto.CommonDto;
import com.bt.ecommerce.primary.mapper.SystemUserMapper;
import com.bt.ecommerce.primary.pojo.user.SystemUser;
import com.bt.ecommerce.security.JwtUserDetailsService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/ops/v1/common-user")
public class CommonController extends _BaseController {

    /**
     * Only Use For > Admin | Sub-Admin
    * */
    @TranslateResponseMessage
    @GetMapping("/profile-detail")
    public ResponseEntity<ResponsePacket> getProfileDetail() throws BadRequestException {
        try {
            SystemUser loggedInUser = (SystemUser) SpringBeanContext.getBean(JwtUserDetailsService.class).getLoggedInUser();
            return new ResponseEntity<>(ResponsePacket.builder()
                    .errorCode(0)
                    .message("ecommerce.common.message.get")
                    .responsePacket(SystemUserMapper.MAPPER.mapToProfileDto(loggedInUser))
                    .build(), HttpStatus.OK);
        } catch (Exception e) {
            throw new BadRequestException("UnAuthorize Access.");
        }
    }

    @TranslateResponseMessage
    @GetMapping("/get-assign-urls")
    public ResponseEntity<ResponsePacket> getAssignUrls() throws BadRequestException {
        try {
            SystemUser loggedInUser = (SystemUser) SpringBeanContext.getBean(JwtUserDetailsService.class).getLoggedInUser();
            return new ResponseEntity<>(ResponsePacket.builder()
                    .errorCode(0)
                    .message("ecommerce.common.message.get")
                    .responsePacket(systemUserService.getAssignUrls(loggedInUser))
                    .build(), HttpStatus.OK);
        } catch (Exception e) {
            throw new BadRequestException("UnAuthorize Access.");
        }
    }

    @TranslateResponseMessage
    @PutMapping("/change-password")
    public ResponseEntity<ResponsePacket> changePassword(
            @Valid @RequestBody CommonDto.ChangePassword request) throws BadRequestException {
        systemUserService.changePassword(request.getOldPassword(), request.getNewPassword());
        return new ResponseEntity<>(ResponsePacket.builder()
                .errorCode(0)
                .message("Password Changed Successfully.")
                .build(), HttpStatus.OK);
    }

    @TranslateResponseMessage
    @PostMapping(value = "/updateDeviceDetail")
    public ResponseEntity<ResponsePacket> updateDeviceDetail(
            @Valid @RequestBody CommonDto.UpdateDeviceDetail request) throws BadRequestException {
        SystemUser loggedInUser = (SystemUser) SpringBeanContext.getBean(JwtUserDetailsService.class).getLoggedInUser();
        systemUserService.updateDeviceDetail(loggedInUser,
                request.getDeviceType(),
                request.getFcmDeviceToken(),
                request.getDeviceId());
        return new ResponseEntity<>(ResponsePacket.builder()
                .errorCode(0)
                .message("Device details has been updated successfully.")
                .build(), HttpStatus.OK);
    }

}