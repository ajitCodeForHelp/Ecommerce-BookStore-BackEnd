package com.bt.ecommerce.primary.controller.customer;

import com.bt.ecommerce.annotation.TranslateResponseMessage;
import com.bt.ecommerce.bean.ResponsePacket;
import com.bt.ecommerce.configuration.SpringBeanContext;
import com.bt.ecommerce.exception.BadRequestException;
import com.bt.ecommerce.primary.controller._BaseController;
import com.bt.ecommerce.primary.dto.CustomerDto;
import com.bt.ecommerce.primary.pojo.user.Customer;
import com.bt.ecommerce.security.JwtUserDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/customer/v1")
public class CustomerController extends _BaseController {

    @TranslateResponseMessage
    @PostMapping("/generateOtp")
    protected ResponseEntity<ResponsePacket> generateOtp(@Valid @RequestBody CustomerDto.GenerateOtp generateOtp) throws BadRequestException {
        customerService.generateOtp(generateOtp);
        return new ResponseEntity<>(ResponsePacket.builder()
                .errorCode(0)
                .message("ecommerce.common.message.otp_sent")
                .build(), HttpStatus.OK);
    }

    @TranslateResponseMessage
    @PostMapping("/loginCustomerWithOtp")
    protected ResponseEntity<ResponsePacket> loginWithOtp(HttpServletRequest request, @Valid @RequestBody CustomerDto.LoginCustomer login) throws BadRequestException {
        return new ResponseEntity<>(ResponsePacket.builder()
                .errorCode(0)
                .message("ecommerce.common.message.login")
                .responsePacket(customerService.loginWithOtp(login))
                .build(), HttpStatus.OK);
    }

    @TranslateResponseMessage
    @PostMapping("/loginCustomerWithPassword")
    protected ResponseEntity<ResponsePacket> loginWithPassword(HttpServletRequest request,@Valid @RequestBody CustomerDto.LoginCustomer login) throws BadRequestException {
        return new ResponseEntity<>(ResponsePacket.builder()
                .errorCode(0)
                .message("ecommerce.common.message.login")
                .responsePacket(customerService.loginWithPassword(login))
                .build(), HttpStatus.OK);
    }

    @TranslateResponseMessage
    @PutMapping("/update/{uuid}")
    protected ResponseEntity<ResponsePacket> edit(@PathVariable("uuid") String uuid,
                                                  @Valid @RequestBody CustomerDto.UpdateCustomer update) throws BadRequestException {
        Customer loggedInUser = (Customer) SpringBeanContext.getBean(JwtUserDetailsService.class).getLoggedInUser();
        customerService.update(uuid, update);
        return new ResponseEntity<>(ResponsePacket.builder()
                .errorCode(0)
                .message("ecommerce.common.message.update")
                .build(), HttpStatus.OK);
    }

    @TranslateResponseMessage
    @GetMapping("/get/{uuid}")
    protected ResponseEntity<ResponsePacket> get(@PathVariable("uuid") String uuid) throws BadRequestException {
        Customer loggedInUser = (Customer) SpringBeanContext.getBean(JwtUserDetailsService.class).getLoggedInUser();
        return new ResponseEntity<>(ResponsePacket.builder()
                .errorCode(0)
                .message("ecommerce.common.message.get")
                .responsePacket(customerService.get(uuid))
                .build(), HttpStatus.OK);
    }
}
