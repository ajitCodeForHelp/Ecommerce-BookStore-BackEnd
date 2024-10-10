//package com.bt.ecommerce.primary.controller.admin;
//
//import com.bt.ecommerce.annotation.TranslateResponseMessage;
//import com.bt.ecommerce.bean.ResponsePacket;
//import com.bt.ecommerce.exception.BadRequestException;
//import com.bt.ecommerce.primary.controller._BaseController;
//import com.bt.ecommerce.primary.pojo.enums.SettingEnum;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@Slf4j
//@RestController
//@RequestMapping("/admin/v1/setting")
//public class AdminSettingController extends _BaseController {
//
//    /**
//     * [PUT ] /admin/v1/setting/{settingKey}
//     * [GET ] /admin/v1/setting/{settingKey}
//     * [GET ] /admin/v1/setting/list-keys
//     * [POST] /admin/v1/setting/list-in-key-value
//     */
//
//    @TranslateResponseMessage
//    @PutMapping("/{settingKey}")
//    public ResponseEntity<ResponsePacket> update(
//            @PathVariable("settingKey") SettingEnum settingEnumKey,
//            @RequestBody SettingDto.UpdateSetting update) throws BadRequestException {
//        settingTrade.update(settingEnumKey, update);
//        return new ResponseEntity<>(ResponsePacket.builder()
//                .errorCode(0)
//                .message("kpis_food.common.message.update")
//                .build(), HttpStatus.OK);
//    }
//
//    @TranslateResponseMessage
//    @GetMapping("/{settingKey}")
//    public ResponseEntity<ResponsePacket> get(
//            @PathVariable("settingKey") SettingEnum settingKey) throws BadRequestException {
//        return new ResponseEntity<>(ResponsePacket.builder()
//                .errorCode(0)
//                .message("kpis_food.common.message.get")
//                .responsePacket(settingTrade.get(settingKey))
//                .build(), HttpStatus.OK);
//    }
//
//    @TranslateResponseMessage
//    @GetMapping("/list-keys")
//    public ResponseEntity<ResponsePacket> listKeys() {
//        return new ResponseEntity<>(ResponsePacket.builder()
//                .errorCode(0)
//                .message("kpis_food.common.message.get_all")
//                .responsePacket(SettingEnum.values())
//                .build(), HttpStatus.OK);
//    }
//
//    @TranslateResponseMessage
//    @PostMapping("/list-in-key-value")
//    public ResponseEntity<ResponsePacket> listInKeyValue() {
//        return new ResponseEntity<>(ResponsePacket.builder()
//                .errorCode(0)
//                .message("kpis_food.common.message.get_key_value_list")
//                .responsePacket(settingTrade.listInKeyValue())
//                .build(), HttpStatus.OK);
//    }
//
//}
