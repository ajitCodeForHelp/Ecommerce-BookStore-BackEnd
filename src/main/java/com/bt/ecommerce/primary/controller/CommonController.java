//package com.kpis.kpisFood.primary.controller;
//
//import com.kpis.kpisFood.annotation.TranslateResponseMessage;
//import com.kpis.kpisFood.bean.ResponsePacket;
//import com.kpis.kpisFood.configuration.SpringBeanContext;
//import com.kpis.kpisFood.exception.BadRequestException;
//import com.kpis.kpisFood.primary.dto.CommonUserDto;
//import com.kpis.kpisFood.primary.mapper.CommonUserMapper;
//import com.kpis.kpisFood.primary.pojo.user.UserAdmin;
//import com.kpis.kpisFood.primary.pojo.user.UserClient;
//import com.kpis.kpisFood.primary.pojo.user._BaseUser;
//import com.kpis.kpisFood.security.JwtUserDetailsService;
//import jakarta.validation.Valid;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@Slf4j
//@RestController
//@RequestMapping("/ops/v1/common-user")
//public class CommonController extends _BaseController {
//
//    @TranslateResponseMessage
//    @GetMapping("/profile-details")
//    public ResponseEntity<ResponsePacket> getProfileDetails() throws BadRequestException {
//        try {
//            _BaseUser loggedInUser = SpringBeanContext.getBean(JwtUserDetailsService.class).getLoggedInUser();
//            return new ResponseEntity<>(ResponsePacket.builder()
//                    .errorCode(0)
//                    .message("kpis_food.common.message.get")
//                    .responsePacket(CommonUserMapper.MAPPER.mapToProfileDto(loggedInUser))
//                    .build(), HttpStatus.OK);
//        } catch (Exception e) {
//            throw new BadRequestException("kpis_food.common.message.unauthorized_access");
//        }
//    }
//
//    @TranslateResponseMessage
//    @PutMapping("/change-password")
//    public ResponseEntity<ResponsePacket> changePassword(
//            @Valid @RequestBody CommonUserDto.ResetPasswordRequest request) throws BadRequestException {
//        _BaseUser loggedInUser = SpringBeanContext.getBean(JwtUserDetailsService.class).getLoggedInUser();
//        if(loggedInUser instanceof UserAdmin){
//            userAdminTrade.changePassword(request.getOldPassword(), request.getNewPassword());
//        } else if (loggedInUser instanceof UserClient) {
//            userClientTrade.changePassword(request.getOldPassword(), request.getNewPassword());
//        } else{
//            // TODO >> Implement It Later On
//        }
//        return new ResponseEntity<>(ResponsePacket.builder()
//                .errorCode(0)
//                .message("kpis_food.common.message.password_changed")
//                .build(), HttpStatus.OK);
//    }
//
//    @TranslateResponseMessage
//    @GetMapping("/logout")
//    public ResponseEntity<ResponsePacket> logout() throws BadRequestException {
//        _BaseUser loggedInUser = SpringBeanContext.getBean(JwtUserDetailsService.class).getLoggedInUser();
////        if(loggedInUser instanceof UserAdmin){
////
////        } else if (loggedInUser instanceof UserClient) {
////
////        } else{
////            // TODO >> Implement It Later On
////        }
//        return new ResponseEntity<>(ResponsePacket.builder()
//                .errorCode(0)
//                .message("kpis_food.common.message.logout")
//                .build(), HttpStatus.OK);
//    }
//
//}