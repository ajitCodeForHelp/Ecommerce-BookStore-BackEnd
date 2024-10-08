//package com.kpis.kpisFood.primary.controller.admin;
//
//import com.kpis.kpisFood.annotation.TranslateResponseMessage;
//import com.kpis.kpisFood.bean.ResponsePacket;
//import com.kpis.kpisFood.exception.BadRequestException;
//import com.kpis.kpisFood.primary.controller._BaseController;
//import com.kpis.kpisFood.primary.dto.BrandDto;
//import jakarta.validation.Valid;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@Slf4j
//@RestController
//@RequestMapping("/admin/v1/brand/{clientUuid}")
//public class AdminBrandController extends _BaseController {
//
//    @TranslateResponseMessage
//    @PostMapping("/save")
//    protected ResponseEntity<ResponsePacket> save(@PathVariable("clientUuid") String clientUuid,
//                                                  @Valid @RequestBody BrandDto.SaveBrand create) throws BadRequestException {
//        return new ResponseEntity<>(ResponsePacket.builder()
//                .errorCode(0)
//                .message("kpis_food.common.message.save")
//                .responsePacket(brandTrade.save(clientUuid, create))
//                .build(), HttpStatus.OK);
//    }
//
//    @TranslateResponseMessage
//    @PutMapping("/update/{uuid}")
//    protected ResponseEntity<ResponsePacket> edit(@PathVariable("clientUuid") String clientUuid,
//                                                  @PathVariable("uuid") String uuid,
//                                                  @Valid @RequestBody BrandDto.UpdateBrand update) throws BadRequestException {
//        brandTrade.update(clientUuid, uuid, update);
//        return new ResponseEntity<>(ResponsePacket.builder()
//                .errorCode(0)
//                .message("kpis_food.common.message.update")
//                .build(), HttpStatus.OK);
//    }
//
//    @TranslateResponseMessage
//    @GetMapping("/get/{uuid}")
//    protected ResponseEntity<ResponsePacket> get(@PathVariable("clientUuid") String clientUuid,
//                                                 @PathVariable("uuid") String uuid) throws BadRequestException {
//        return new ResponseEntity<>(ResponsePacket.builder()
//                .errorCode(0)
//                .message("kpis_food.common.message.get")
//                .responsePacket(brandTrade.get(clientUuid, uuid))
//                .build(), HttpStatus.OK);
//    }
//
//    @TranslateResponseMessage
//    @PostMapping("/list")
//    protected ResponseEntity<ResponsePacket> list(@PathVariable("clientUuid") String clientUuid,
//                                                  @Valid @RequestBody BrandDto.GetList list) throws BadRequestException {
//        return new ResponseEntity<>(ResponsePacket.builder()
//                .errorCode(0)
//                .message("kpis_food.common.message.get_all")
//                .responsePacket(brandTrade.list(clientUuid, false, list.getPageNumber(), list.getPageSize(), list.getSearch()))
//                .build(), HttpStatus.OK);
//    }
//
//    @TranslateResponseMessage
//    @PostMapping("/deleted-list")
//    protected ResponseEntity<ResponsePacket> deletedList(@PathVariable("clientUuid") String clientUuid,
//                                                         @Valid @RequestBody BrandDto.GetList list) throws BadRequestException {
//        return new ResponseEntity<>(ResponsePacket.builder()
//                .errorCode(0)
//                .message("kpis_food.common.message.get_all_deleted_records")
//                .responsePacket(brandTrade.list(clientUuid, true, list.getPageNumber(), list.getPageSize(), list.getSearch()))
//                .build(), HttpStatus.OK);
//    }
//
//    @TranslateResponseMessage
//    @PutMapping("/activate/{uuid}")
//    protected ResponseEntity<ResponsePacket> activate(@PathVariable("clientUuid") String clientUuid,
//                                                      @PathVariable("uuid") String uuid) throws BadRequestException {
//        brandTrade.activate(clientUuid, uuid);
//        return new ResponseEntity<>(ResponsePacket.builder()
//                .errorCode(0)
//                .message("kpis_food.common.message.active")
//                .build(), HttpStatus.OK);
//    }
//
//    @TranslateResponseMessage
//    @PutMapping("/inactivate/{uuid}")
//    protected ResponseEntity<ResponsePacket> inactivate(@PathVariable("clientUuid") String clientUuid,
//                                                        @PathVariable("uuid") String uuid) throws BadRequestException {
//        brandTrade.inactivate(clientUuid, uuid);
//        return new ResponseEntity<>(ResponsePacket.builder()
//                .errorCode(0)
//                .message("kpis_food.common.message.inactive")
//                .build(), HttpStatus.OK);
//    }
//
//    @TranslateResponseMessage
//    @DeleteMapping("/delete/{uuid}")
//    protected ResponseEntity<ResponsePacket> delete(@PathVariable("clientUuid") String clientUuid,
//                                                    @PathVariable("uuid") String uuid) throws BadRequestException {
//        brandTrade.delete(clientUuid, uuid);
//        return new ResponseEntity<>(ResponsePacket.builder()
//                .errorCode(0)
//                .message("kpis_food.common.message.deleted")
//                .build(), HttpStatus.OK);
//    }
//
//    @TranslateResponseMessage
//    @PutMapping("/revive/{uuid}")
//    protected ResponseEntity<ResponsePacket> revive(@PathVariable("clientUuid") String clientUuid,
//                                                    @PathVariable("uuid") String uuid) throws BadRequestException {
//        brandTrade.revive(clientUuid, uuid);
//        return new ResponseEntity<>(ResponsePacket.builder()
//                .errorCode(0)
//                .message("kpis_food.common.message.revive")
//                .build(), HttpStatus.OK);
//    }
//
//    @TranslateResponseMessage
//    @PostMapping("/list-in-key-value")
//    public ResponseEntity<ResponsePacket> listInKeyValue(@PathVariable("clientUuid") String clientUuid) throws BadRequestException {
//        return new ResponseEntity<>(ResponsePacket.builder()
//                .errorCode(0)
//                .message("kpis_food.common.message.get_key_value_list")
//                .responsePacket(brandTrade.listInKeyValue(clientUuid))
//                .build(), HttpStatus.OK);
//    }
//}