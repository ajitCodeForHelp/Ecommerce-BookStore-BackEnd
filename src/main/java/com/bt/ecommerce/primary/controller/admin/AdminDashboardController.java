package com.bt.ecommerce.primary.controller.admin;

import com.bt.ecommerce.bean.ResponsePacket;
import com.bt.ecommerce.exception.BadRequestException;
import com.bt.ecommerce.primary.controller._BaseController;
import com.bt.ecommerce.primary.dto.DashboardDto;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("/admin/v1/dashboard")
public class AdminDashboardController extends _BaseController {

    @PostMapping("/report")
    public ResponseEntity<ResponsePacket> dashboardReport(@Valid @RequestBody DashboardDto.DashboardDataRequest request) throws BadRequestException {
        return new ResponseEntity<>(ResponsePacket.builder()
                .errorCode(0)
                .message("Get Dashboard Data Report Successfully.")
                .responsePacket(dashboardService.getRestaurantDashboardReport(request.getStartTimestamp(), request.getEndTimestamp()))
                .build(), HttpStatus.OK);
    }

    @PostMapping("/salesAndOrderStats")
    public ResponseEntity<ResponsePacket> salesAndOrderStats(@Valid @RequestBody DashboardDto.DashboardDataRequest request) throws BadRequestException {
        return new ResponseEntity<>(ResponsePacket.builder()
                .errorCode(0)
                .message("Get Dashboard Data Report Successfully.")
                .responsePacket(dashboardService.dashboardSalesAndOrderStats(request.getStartTimestamp(), request.getEndTimestamp()))
                .build(), HttpStatus.OK);
    }



}