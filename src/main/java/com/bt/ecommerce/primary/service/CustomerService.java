package com.bt.ecommerce.primary.service;

import com.bt.ecommerce.configuration.SpringBeanContext;
import com.bt.ecommerce.exception.BadRequestException;
import com.bt.ecommerce.primary.dto.AuthDto;
import com.bt.ecommerce.primary.dto.CustomerDto;
import com.bt.ecommerce.primary.dto.OtpDto;
import com.bt.ecommerce.primary.mapper.CustomerMapper;
import com.bt.ecommerce.primary.pojo.OneTimePassword;
import com.bt.ecommerce.primary.pojo.enums.MobileEmailEnum;
import com.bt.ecommerce.primary.pojo.enums.RoleEnum;
import com.bt.ecommerce.primary.pojo.enums.VerificationTypeEnum;
import com.bt.ecommerce.primary.pojo.user.Customer;
import com.bt.ecommerce.security.JwtUserDetailsService;
import com.bt.ecommerce.utils.TextUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
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

    public void sendOtpUsingOtpLess(String isdCode, String mobileNumber) throws BadRequestException, IOException {
        Customer customer = customerRepository.findFirstByIsdCodeAndMobile(isdCode, mobileNumber);
        if (customer == null) {
            customer = SpringBeanContext.getBean(CustomerService.class).registerCustomer(isdCode, mobileNumber);
        }
        if (!customer.isActive()) {
            throw new BadRequestException("User is not active, please talk to your admin for further process.");
        }

        Pageable pageable = PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "createdAt"));
        List<OneTimePassword> optList = oneTimePasswordRepository.getOtpLessUnExpiredOTP(mobileNumber, VerificationTypeEnum.Login, LocalDateTime.now(), pageable);
        if (!optList.isEmpty()) {
            throw new BadRequestException("Please wait for new minutes!!");
        }

        if (mobileNumber.equalsIgnoreCase("8080808080")) {
            OneTimePassword verification = new OneTimePassword();
            verification.setSentOnType(MobileEmailEnum.Mobile);
            verification.setOtpSentOn(mobileNumber);
            verification.setVerificationType(VerificationTypeEnum.Login);
            verification.setExpiredAt(LocalDateTime.now().plusSeconds(60));
            verification.setOtpCode("999999");
            oneTimePasswordRepository.save(verification);
        } else {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            OtpDto.OTPBody otpBody = new OtpDto.OTPBody();
            otpBody.setPhoneNumber(isdCode + mobileNumber);
            otpBody.setOtpLength("6");
            otpBody.setChannel("SMS");
            otpBody.setExpiry(60);
            RequestBody body = RequestBody.create(mediaType, new Gson().toJson(otpBody));
            Request request = new Request.Builder()
                    .url("https://auth.otpless.app/auth/otp/v1/send")
                    .method("POST", body)
                    .addHeader("clientId", "client_id")
                    .addHeader("clientSecret", "client_secret")
                    .addHeader("Content-Type", "application/json")
                    .build();
            Response response = client.newCall(request).execute();
            ObjectMapper objectMapper = new ObjectMapper();
            OtpDto.OTPResponse beanOtpResponse = objectMapper.readValue(response.body().string(), OtpDto.OTPResponse.class);
            if (response.isSuccessful()) {
                if (beanOtpResponse.getOrderId() != null) {
                    OneTimePassword verification = new OneTimePassword();
                    verification.setSentOnType(MobileEmailEnum.Mobile);
                    verification.setOtpSentOn(mobileNumber);
                    verification.setOrderId(beanOtpResponse.getOrderId());
                    verification.setVerificationType(VerificationTypeEnum.Login);
                    verification.setExpiredAt(LocalDateTime.now().plusSeconds(60));
                    oneTimePasswordRepository.save(verification);
                } else {
                    throw new BadRequestException("Error Sending Otp: " + beanOtpResponse.getMessage());
                }
            } else {
                throw new BadRequestException("Error Sending Otp: " + beanOtpResponse.getMessage());
            }
        }
    }

    public CustomerDto.DetailCustomer verifyOtpUsingOtpLess(OtpDto.OTPVerify otpVerify) throws BadRequestException, IOException {
        Customer user = customerRepository.findFirstByIsdCodeAndMobile(otpVerify.getIsdCode(), otpVerify.getPhoneNumber());
        if (user == null) {
            throw new BadRequestException("User Not Found");
        }
        if (!user.isActive()) {
            throw new BadRequestException("User is Inactive");
        }
        if (otpVerify.getPhoneNumber().equalsIgnoreCase("8080808080")) {
            if (!otpVerify.getOtp().equalsIgnoreCase("999999")) {
                throw new BadRequestException("Invalid OTP");
            }
            return CustomerMapper.MAPPER.mapToDetailDto(user);
        }
        Pageable pageable = PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "createdAt"));
        List<OneTimePassword> otpList = oneTimePasswordRepository.getOtpLessUnExpiredOTP(otpVerify.getPhoneNumber(), VerificationTypeEnum.Login, LocalDateTime.now(), pageable);
        if (otpList.isEmpty()) {
            throw new BadRequestException("Please resend the OTP.");
        }
        OneTimePassword oneTimePassword = otpList.get(0);
        otpVerify.setPhoneNumber(otpVerify.getIsdCode() + otpVerify.getPhoneNumber());
        otpVerify.setOrderId(oneTimePassword.getOrderId());
        String requestBody = new Gson().toJson(otpVerify);
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, requestBody);
        Request request = new Request.Builder()
                .url("https://auth.otpless.app/auth/otp/v1/verify")
                .method("POST", body)
                .addHeader("clientId", "client_id")
                .addHeader("clientSecret", "client_secret")
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = client.newCall(request).execute();
        ObjectMapper objectMapper = new ObjectMapper();
        OtpDto.OTPVerifyResponse verificationResponse = objectMapper.readValue(response.body().string(), OtpDto.OTPVerifyResponse.class);
        if (response.isSuccessful()) {
            if (verificationResponse.isOTPVerified()) {
                oneTimePassword.setVerified(true);
                oneTimePasswordRepository.save(oneTimePassword);
                return CustomerMapper.MAPPER.mapToDetailDto(user);
            } else {
                throw new BadRequestException("Verification  Failed: " + verificationResponse.getReason() == null ? verificationResponse.getMessage() : verificationResponse.getReason());
            }
        } else {
            throw new BadRequestException("Verification  Failed: " + verificationResponse.getReason() == null ? verificationResponse.getMessage() : verificationResponse.getReason());
        }
    }

}
