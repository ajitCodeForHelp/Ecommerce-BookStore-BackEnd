package com.bt.ecommerce.primary.controller.admin;

import com.bt.ecommerce.annotation.TranslateResponseMessage;
import com.bt.ecommerce.bean.ResponsePacket;
import com.bt.ecommerce.configuration.SpringBeanContext;
import com.bt.ecommerce.exception.BadRequestException;
import com.bt.ecommerce.primary.controller._BaseController;
import com.bt.ecommerce.primary.pojo.user.SystemUser;
import com.bt.ecommerce.primary.userAccess.dto.ModuleDto;
import com.bt.ecommerce.primary.userAccess.dto.UrlDto;
import com.bt.ecommerce.security.JwtUserDetailsService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin/v1/module")
public class AdminModuleController extends _BaseController {

    @TranslateResponseMessage
    @PostMapping("/save")
    protected ResponseEntity<ResponsePacket> save(@Valid @RequestBody ModuleDto.SaveModule create) throws BadRequestException {
        SystemUser loggedInUser = (SystemUser) SpringBeanContext.getBean(JwtUserDetailsService.class).getLoggedInUser();
        return new ResponseEntity<>(ResponsePacket.builder()
                .errorCode(0)
                .message("ecommerce.common.message.save")
                .responsePacket(moduleService.save(create))
                .build(), HttpStatus.OK);
    }

    @TranslateResponseMessage
    @PutMapping("/update/{uuid}")
    protected ResponseEntity<ResponsePacket> edit(@PathVariable("uuid") String uuid,
                                                  @Valid @RequestBody ModuleDto.UpdateModule update) throws BadRequestException {
        SystemUser loggedInUser = (SystemUser) SpringBeanContext.getBean(JwtUserDetailsService.class).getLoggedInUser();
        moduleService.update(uuid, update);
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
                .responsePacket(moduleService.get(uuid))
                .build(), HttpStatus.OK);
    }

    @TranslateResponseMessage
    @GetMapping("/list-data/{data}")
    protected ResponseEntity<ResponsePacket> listData(@PathVariable("data") String data) throws BadRequestException {
        return new ResponseEntity<>(ResponsePacket.builder()
                .errorCode(0)
                .message("ecommerce.common.message.get_all")
                .responsePacket(moduleService.listData(data))
                .build(), HttpStatus.OK);
    }

    @TranslateResponseMessage
    @PutMapping("/activate/{uuid}")
    protected ResponseEntity<ResponsePacket> activate(@PathVariable("uuid") String uuid) throws BadRequestException {
        SystemUser loggedInUser = (SystemUser) SpringBeanContext.getBean(JwtUserDetailsService.class).getLoggedInUser();
        moduleService.activate(uuid);
        return new ResponseEntity<>(ResponsePacket.builder()
                .errorCode(0)
                .message("ecommerce.common.message.active")
                .build(), HttpStatus.OK);
    }

    @TranslateResponseMessage
    @PutMapping("/inactivate/{uuid}")
    protected ResponseEntity<ResponsePacket> inactivate(@PathVariable("uuid") String uuid) throws BadRequestException {
        SystemUser loggedInUser = (SystemUser) SpringBeanContext.getBean(JwtUserDetailsService.class).getLoggedInUser();
        moduleService.inactivate(uuid);
        return new ResponseEntity<>(ResponsePacket.builder()
                .errorCode(0)
                .message("ecommerce.common.message.inactive")
                .build(), HttpStatus.OK);
    }

    @TranslateResponseMessage
    @DeleteMapping("/delete/{uuid}")
    protected ResponseEntity<ResponsePacket> delete(@PathVariable("uuid") String uuid) throws BadRequestException {
        SystemUser loggedInUser = (SystemUser) SpringBeanContext.getBean(JwtUserDetailsService.class).getLoggedInUser();
        moduleService.delete(uuid);
        return new ResponseEntity<>(ResponsePacket.builder()
                .errorCode(0)
                .message("ecommerce.common.message.deleted")
                .build(), HttpStatus.OK);
    }

    @TranslateResponseMessage
    @PutMapping("/revive/{uuid}")
    protected ResponseEntity<ResponsePacket> revive(@PathVariable("uuid") String uuid) throws BadRequestException {
        SystemUser loggedInUser = (SystemUser) SpringBeanContext.getBean(JwtUserDetailsService.class).getLoggedInUser();
        moduleService.revive(uuid);
        return new ResponseEntity<>(ResponsePacket.builder()
                .errorCode(0)
                .message("ecommerce.common.message.revive")
                .build(), HttpStatus.OK);
    }

    @TranslateResponseMessage
    @PostMapping("/list-in-key-value")
    public ResponseEntity<ResponsePacket> listInKeyValue() throws BadRequestException {
        return new ResponseEntity<>(ResponsePacket.builder()
                .errorCode(0)
                .message("ecommerce.common.message.get_key_value_list")
                .responsePacket(moduleService.listInKeyValue())
                .build(), HttpStatus.OK);
    }


    @TranslateResponseMessage
    @GetMapping("/module-url-list")
    public ResponseEntity<ResponsePacket> moduleUrlList() throws BadRequestException {
        /**
         * Use For Urls Assignment To User
         * /module-url-list > Only Give Active Non-Deleted Module And Url
         * **/
        return new ResponseEntity<>(ResponsePacket.builder()
                .errorCode(0)
                .message("Module Url List.")
                .responsePacket(moduleService.getModuleUrlList())
                .build(), HttpStatus.OK);
    }

    // ################################ Module Url Operation ############################################################

    @TranslateResponseMessage
    @PostMapping("/{moduleUuid}/save-module-urls")
    protected ResponseEntity<ResponsePacket> saveModuleUrls(@PathVariable("moduleUuid") String moduleUuid,
                                                            @Valid @RequestBody List<UrlDto.SaveUrl> urlList) throws BadRequestException {
        moduleService.saveModuleUrls(moduleUuid, urlList);
        return new ResponseEntity<>(ResponsePacket.builder()
                .errorCode(0)
                .message("ecommerce.common.message.save")
                .build(), HttpStatus.OK);
    }

    @TranslateResponseMessage
    @PutMapping("/{moduleUuid}/update-module-url/{urlUuid}")
    protected ResponseEntity<ResponsePacket> updateModuleUrl(
            @PathVariable("moduleUuid") String moduleUuid,
            @PathVariable("urlUuid") String urlUuid,
            @Valid @RequestBody UrlDto.UpdateUrl updateUrl) throws BadRequestException {
        moduleService.updateModuleUrl(moduleUuid, urlUuid, updateUrl);
        return new ResponseEntity<>(ResponsePacket.builder()
                .errorCode(0)
                .message("ecommerce.common.message.active")
                .build(), HttpStatus.OK);
    }

    @TranslateResponseMessage
    @PutMapping("/{moduleUuid}/activate-module-url/{urlUuid}")
    protected ResponseEntity<ResponsePacket> activateModuleUrl(@PathVariable("moduleUuid") String moduleUuid,
                                                               @PathVariable("urlUuid") String urlUuid) throws BadRequestException {
        moduleService.activateModuleUrl(moduleUuid, urlUuid);
        return new ResponseEntity<>(ResponsePacket.builder()
                .errorCode(0)
                .message("ecommerce.common.message.active")
                .build(), HttpStatus.OK);
    }

    @TranslateResponseMessage
    @PutMapping("/{moduleUuid}/deactivate-module-url/{urlUuid}")
    protected ResponseEntity<ResponsePacket> deactivateModuleUrl(@PathVariable("moduleUuid") String moduleUuid,
                                                                 @PathVariable("urlUuid") String urlUuid) throws BadRequestException {
        moduleService.deactivateModuleUrl(moduleUuid, urlUuid);
        return new ResponseEntity<>(ResponsePacket.builder()
                .errorCode(0)
                .message("ecommerce.common.message.active")
                .build(), HttpStatus.OK);
    }

    @TranslateResponseMessage
    @DeleteMapping("/{moduleUuid}/delete-module-url/{urlUuid}")
    protected ResponseEntity<ResponsePacket> deleteModuleUrl(@PathVariable("moduleUuid") String moduleUuid,
                                                             @PathVariable("urlUuid") String urlUuid) throws BadRequestException {
        moduleService.deactivateModuleUrl(moduleUuid, urlUuid);
        return new ResponseEntity<>(ResponsePacket.builder()
                .errorCode(0)
                .message("ecommerce.common.message.deleted")
                .build(), HttpStatus.OK);
    }

}