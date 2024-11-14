package com.bt.ecommerce.primary.service;

import com.bt.ecommerce.configuration.SpringBeanContext;
import com.bt.ecommerce.exception.BadRequestException;
import com.bt.ecommerce.primary.dto.AuthDto;
import com.bt.ecommerce.primary.dto.CustomerDto;
import com.bt.ecommerce.primary.mapper.CustomerMapper;
import com.bt.ecommerce.primary.pojo.enums.RoleEnum;
import com.bt.ecommerce.primary.pojo.enums.VerificationTypeEnum;
import com.bt.ecommerce.primary.pojo.user.Customer;
import com.bt.ecommerce.security.JwtUserDetailsService;
import com.bt.ecommerce.utils.TextUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CustomerService extends _BaseService {

    @Autowired
    private OneTimePasswordService oneTimePasswordService;

    public void validateLoginWithOtp(AuthDto.CustomerOtpLogin login) throws BadRequestException {
        boolean otpVerify = oneTimePasswordService.verifyOtp(login.getMobile(), login.getOtp(), VerificationTypeEnum.Login);
        if (!otpVerify) {
            throw new BadRequestException("Otp Verification Failed");
        }
        Customer customer = customerRepository.findFirstByIsdCodeAndMobile(login.getIsdCode(), login.getMobile());
        if (customer == null) {
            // Register This Customer
            customer = registerCustomer(login.getIsdCode(), login.getMobile());
        }
        if(customer == null){
            throw new BadRequestException("Invalid User Request");
        }
    }

    public void forgotPassword(AuthDto.ForgotPassword passwordDto) throws BadRequestException {
        boolean otpVerify = oneTimePasswordService.verifyOtp(passwordDto.getMobile(), passwordDto.getOtp(), VerificationTypeEnum.ForgotPassword);
        if (!otpVerify) {
            throw new BadRequestException("Password Reset Otp Verification Failed");
        }
        Customer customer = customerRepository.findFirstByIsdCodeAndMobile(passwordDto.getIsdCode(), passwordDto.getMobile());
        if (customer == null) {
            // Register This Customer
            customer = registerCustomer(passwordDto.getIsdCode(), passwordDto.getMobile());
        }
        if(customer == null){
            throw new BadRequestException("Invalid User Request");
        }
        customer.setPwdText(passwordDto.getNewPassword());
        customer.setPwdSecure(TextUtils.getEncodedPassword(customer.getPwdText()));
        customerRepository.save(customer);
    }

    private Customer registerCustomer(String isdCode , String mobile){
        Customer customer = new Customer();
        customer.setIsdCode(isdCode);
        customer.setMobile(mobile);
        customer.setUsername(mobile);
        customer.setUserType(RoleEnum.ROLE_CUSTOMER);
        customer.setUniqueKey(TextUtils.getUniqueKey());
        return customerRepository.save(customer);
    }


    public void updateProfile(CustomerDto.UpdateCustomer updateDto) throws BadRequestException {
        Customer customer = (Customer) SpringBeanContext.getBean(JwtUserDetailsService.class).getLoggedInUser();
        customer = CustomerMapper.MAPPER.mapToPojo(customer, updateDto);
        customerRepository.save(customer);
    }

    public CustomerDto.DetailCustomer get() throws BadRequestException {
        Customer customer = (Customer) SpringBeanContext.getBean(JwtUserDetailsService.class).getLoggedInUser();
        return CustomerMapper.MAPPER.mapToDetailDto(customer);
    }

    private Customer findByUuid(String uuid) throws BadRequestException {
        Customer customer = customerRepository.findByUuid(uuid);
        if (customer == null) {
            throw new BadRequestException("ecommerce.common.message.record_not_exist");
        }
        return customer;
    }


    //############################################### Admin Side Api ################################
    public List<CustomerDto.DetailCustomer> listData(String data) {
        // Data >  Active | Inactive | Deleted | All
        List<Customer> list = null;
        if (data.equals("Active")) {
            list = customerRepository.findByActiveAndDeleted(true, false);
        } else if (data.equals("Inactive")) {
            list = customerRepository.findByActiveAndDeleted(false, false);
        } else if (data.equals("Deleted")) {
            list = customerRepository.findByDeleted(true);
        } else {
            list = customerRepository.findAll();
        }
        return list.stream()
                .map(customer -> CustomerMapper.MAPPER.mapToDetailDto(customer))
                .collect(Collectors.toList());
    }

    public void activate(String uuid) throws BadRequestException {
        Customer customer = findByUuid(uuid);
        customer.setActive(true);
        customer.setModifiedAt(LocalDateTime.now());
        customerRepository.save(customer);
    }

    public void inactivate(String uuid) throws BadRequestException {
        Customer customer = findByUuid(uuid);
        customer.setActive(false);
        customer.setModifiedAt(LocalDateTime.now());
        customerRepository.save(customer);
    }

    public void delete(String uuid) throws BadRequestException {
        Customer customer = findByUuid(uuid);
        customer.setActive(false);
        customer.setDeleted(true);
        customer.setModifiedAt(LocalDateTime.now());
        customerRepository.save(customer);
    }

    public void revive(String uuid) throws BadRequestException {
        Customer customer = findByUuid(uuid);
        customer.setDeleted(false);
        customer.setModifiedAt(LocalDateTime.now());
        customerRepository.save(customer);
    }


}
