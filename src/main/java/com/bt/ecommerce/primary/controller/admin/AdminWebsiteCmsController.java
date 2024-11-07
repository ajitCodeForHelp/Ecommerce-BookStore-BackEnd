package com.bt.ecommerce.primary.controller.admin;

import com.bt.ecommerce.annotation.TranslateResponseMessage;
import com.bt.ecommerce.bean.ResponsePacket;
import com.bt.ecommerce.exception.BadRequestException;
import com.bt.ecommerce.primary.controller._BaseController;
import com.bt.ecommerce.primary.dto.WebsiteCmsSettingDto;
import com.bt.ecommerce.primary.pojo.enums.WebsiteCmsSettingEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/admin/v1/website-cms")
public class AdminWebsiteCmsController extends _BaseController {

    /**
     * [PUT ] /admin/v1/setting/{settingKey}
     * [GET ] /admin/v1/setting/{settingKey}
     * [GET ] /admin/v1/setting/list-keys
     * [POST] /admin/v1/setting/list-in-key-value
     */

    @TranslateResponseMessage
    @PutMapping("/{settingKey}")
    public ResponseEntity<ResponsePacket> update(
            @PathVariable("settingKey") WebsiteCmsSettingEnum settingEnumKey,
            @RequestBody WebsiteCmsSettingDto.UpdateSetting update) throws BadRequestException {
        websiteCmsSettingService.update(settingEnumKey, update);
        return new ResponseEntity<>(ResponsePacket.builder()
                .errorCode(0)
                .message("ecommerce.common.message.update")
                .build(), HttpStatus.OK);
    }

    @TranslateResponseMessage
    @GetMapping("/{settingKey}")
    public ResponseEntity<ResponsePacket> get(
            @PathVariable("settingKey") WebsiteCmsSettingEnum settingKey) throws BadRequestException {
        return new ResponseEntity<>(ResponsePacket.builder()
                .errorCode(0)
                .message("ecommerce.common.message.get")
                .responsePacket(websiteCmsSettingService.get(settingKey))
                .build(), HttpStatus.OK);
    }

    @TranslateResponseMessage
    @GetMapping("/list-keys")
    public ResponseEntity<ResponsePacket> listKeys() {
        return new ResponseEntity<>(ResponsePacket.builder()
                .errorCode(0)
                .message("ecommerce.common.message.get_all")
                .responsePacket(WebsiteCmsSettingEnum.values())
                .build(), HttpStatus.OK);
    }

    @TranslateResponseMessage
    @PostMapping("/list-in-key-value")
    public ResponseEntity<ResponsePacket> listInKeyValue() {
        return new ResponseEntity<>(ResponsePacket.builder()
                .errorCode(0)
                .message("ecommerce.common.message.get_key_value_list")
                .responsePacket(websiteCmsSettingService.listInKeyValue())
                .build(), HttpStatus.OK);
    }


}