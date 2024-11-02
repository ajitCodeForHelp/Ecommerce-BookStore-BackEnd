package com.bt.ecommerce.primary.controller.admin;

import com.bt.ecommerce.annotation.TranslateResponseMessage;
import com.bt.ecommerce.bean.ResponsePacket;
import com.bt.ecommerce.configuration.SpringBeanContext;
import com.bt.ecommerce.exception.BadRequestException;
import com.bt.ecommerce.primary.controller._BaseController;
import com.bt.ecommerce.primary.dto.CategoryDto;
import com.bt.ecommerce.primary.pojo.user.SystemUser;
import com.bt.ecommerce.security.JwtUserDetailsService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/admin/v1/category")
public class AdminCategoryController extends _BaseController {

    @TranslateResponseMessage
    @PostMapping("/save")
    protected ResponseEntity<ResponsePacket> save(@Valid @RequestBody CategoryDto.SaveCategory create) throws BadRequestException {
        SystemUser loggedInUser = (SystemUser) SpringBeanContext.getBean(JwtUserDetailsService.class).getLoggedInUser();
        return new ResponseEntity<>(ResponsePacket.builder()
                .errorCode(0)
                .message("ecommerce.common.message.save")
                .responsePacket(categoryService.save(create))
                .build(), HttpStatus.OK);
    }

    @TranslateResponseMessage
    @PutMapping("/update/{uuid}")
    protected ResponseEntity<ResponsePacket> edit(@PathVariable("uuid") String uuid,
                                                  @Valid @RequestBody CategoryDto.UpdateCategory update) throws BadRequestException {
        SystemUser loggedInUser = (SystemUser) SpringBeanContext.getBean(JwtUserDetailsService.class).getLoggedInUser();
        categoryService.update(uuid, update);
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
                .responsePacket(categoryService.get(uuid))
                .build(), HttpStatus.OK);
    }

    @TranslateResponseMessage
    @GetMapping("/list-data")
    protected ResponseEntity<ResponsePacket> listData() throws BadRequestException {
        SystemUser loggedInUser = (SystemUser) SpringBeanContext.getBean(JwtUserDetailsService.class).getLoggedInUser();
        return new ResponseEntity<>(ResponsePacket.builder()
                .errorCode(0)
                .message("ecommerce.common.message.get_all")
                .responsePacket(categoryService.list())
                .build(), HttpStatus.OK);
    }

    @TranslateResponseMessage
    @PostMapping("/list")
    protected ResponseEntity<ResponsePacket> list(@Valid @RequestBody CategoryDto.GetList list) throws BadRequestException {
        SystemUser loggedInUser = (SystemUser) SpringBeanContext.getBean(JwtUserDetailsService.class).getLoggedInUser();
        return new ResponseEntity<>(ResponsePacket.builder()
                .errorCode(0)
                .message("ecommerce.common.message.get_all")
                .responsePacket(categoryService.list(false, list.getPageNumber(), list.getPageSize(), list.getSearch()))
                .build(), HttpStatus.OK);
    }

    @TranslateResponseMessage
    @PostMapping("/deleted-list")
    protected ResponseEntity<ResponsePacket> deletedList(@Valid @RequestBody CategoryDto.GetList list) throws BadRequestException {
        SystemUser loggedInUser = (SystemUser) SpringBeanContext.getBean(JwtUserDetailsService.class).getLoggedInUser();
        return new ResponseEntity<>(ResponsePacket.builder()
                .errorCode(0)
                .message("ecommerce.common.message.get_all_deleted_records")
                .responsePacket(categoryService.list(true, list.getPageNumber(), list.getPageSize(), list.getSearch()))
                .build(), HttpStatus.OK);
    }

    @TranslateResponseMessage
    @PutMapping("/activate/{uuid}")
    protected ResponseEntity<ResponsePacket> activate(@PathVariable("uuid") String uuid) throws BadRequestException {
        SystemUser loggedInUser = (SystemUser) SpringBeanContext.getBean(JwtUserDetailsService.class).getLoggedInUser();
        categoryService.activate(uuid);
        return new ResponseEntity<>(ResponsePacket.builder()
                .errorCode(0)
                .message("ecommerce.common.message.active")
                .build(), HttpStatus.OK);
    }

    @TranslateResponseMessage
    @PutMapping("/inactivate/{uuid}")
    protected ResponseEntity<ResponsePacket> inactivate(@PathVariable("uuid") String uuid) throws BadRequestException {
        SystemUser loggedInUser = (SystemUser) SpringBeanContext.getBean(JwtUserDetailsService.class).getLoggedInUser();
        categoryService.inactivate(uuid);
        return new ResponseEntity<>(ResponsePacket.builder()
                .errorCode(0)
                .message("ecommerce.common.message.inactive")
                .build(), HttpStatus.OK);
    }

    @TranslateResponseMessage
    @DeleteMapping("/delete/{uuid}")
    protected ResponseEntity<ResponsePacket> delete(@PathVariable("uuid") String uuid) throws BadRequestException {
        SystemUser loggedInUser = (SystemUser) SpringBeanContext.getBean(JwtUserDetailsService.class).getLoggedInUser();
        categoryService.delete(uuid);
        return new ResponseEntity<>(ResponsePacket.builder()
                .errorCode(0)
                .message("ecommerce.common.message.deleted")
                .build(), HttpStatus.OK);
    }

    @TranslateResponseMessage
    @PutMapping("/revive/{uuid}")
    protected ResponseEntity<ResponsePacket> revive(@PathVariable("uuid") String uuid) throws BadRequestException {
        SystemUser loggedInUser = (SystemUser) SpringBeanContext.getBean(JwtUserDetailsService.class).getLoggedInUser();
        categoryService.revive(uuid);
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
                .responsePacket(categoryService.listInKeyValue())
                .build(), HttpStatus.OK);
    }

    @TranslateResponseMessage
    @PostMapping("/assign-category/{uuid}")
    public ResponseEntity<ResponsePacket> assignCategory(@PathVariable("uuid") String uuid,
                                                         @Valid @RequestBody CategoryDto.AssignCategory updateDto) throws BadRequestException {
        categoryService.assignCategory(uuid, updateDto.getItemUuids());
        return new ResponseEntity<>(ResponsePacket.builder()
                .errorCode(0)
                .message("ecommerce.common.message.update")
                .build(), HttpStatus.OK);
    }
}