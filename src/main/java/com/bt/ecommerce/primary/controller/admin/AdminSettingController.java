package com.bt.ecommerce.primary.controller.admin;

import com.bt.ecommerce.annotation.TranslateResponseMessage;
import com.bt.ecommerce.bean.ResponsePacket;
import com.bt.ecommerce.exception.BadRequestException;
import com.bt.ecommerce.primary.controller._BaseController;
import com.bt.ecommerce.primary.dto.SettingDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/admin/v1/setting")
public class AdminSettingController extends _BaseController {


    @TranslateResponseMessage
    @GetMapping("/setting-list")
    public ResponseEntity<ResponsePacket> settingList() throws BadRequestException {
        return new ResponseEntity<>(ResponsePacket.builder()
                .errorCode(0)
                .message("Setting details.")
                .responsePacket(settingService.settingList())
                .build(), HttpStatus.OK);
    }

    @TranslateResponseMessage
    @PutMapping("/update-settings")
    public ResponseEntity<ResponsePacket> updateSettings(
            @RequestBody SettingDto.UpdateSettings updateSettings) throws BadRequestException {
        settingService.updateSettings(updateSettings);
        return new ResponseEntity<>(ResponsePacket.builder()
                .errorCode(0)
                .message("Setting updated.")
                .build(), HttpStatus.OK);
    }

}