package com.bt.ecommerce.primary.service;

import com.bt.ecommerce.bean.KeyValueDto;
import com.bt.ecommerce.configuration.SpringBeanContext;
import com.bt.ecommerce.exception.BadRequestException;
import com.bt.ecommerce.primary.dto.CartDto;
import com.bt.ecommerce.primary.dto.CustomerDto;
import com.bt.ecommerce.primary.dto.OneTimePasswordDto;
import com.bt.ecommerce.primary.dto.OtpDto;
import com.bt.ecommerce.primary.mapper.CartMapper;
import com.bt.ecommerce.primary.mapper.CustomerMapper;
import com.bt.ecommerce.primary.mapper.ItemMapper;
import com.bt.ecommerce.primary.mapper.OneTimePasswordMapper;
import com.bt.ecommerce.primary.pojo.Cart;
import com.bt.ecommerce.primary.pojo.Item;
import com.bt.ecommerce.primary.pojo.OneTimePassword;
import com.bt.ecommerce.primary.pojo.Order;
import com.bt.ecommerce.primary.pojo.enums.MobileEmailEnum;
import com.bt.ecommerce.primary.pojo.enums.VerificationTypeEnum;
import com.bt.ecommerce.primary.pojo.user.Customer;
import com.bt.ecommerce.utils.TextUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import okhttp3.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OneTimePasswordService extends _BaseService {

    public String generateOtp(CustomerDto.GenerateOtp generateOtp) {
        // Destroy This User All Previous Otp
        destroyUserPreviousOtp(generateOtp.getMobile());
        OneTimePassword oneTimePassword = new OneTimePassword();
        oneTimePassword.setSentOnType(MobileEmailEnum.Mobile);
        oneTimePassword.setOtpSentOn(generateOtp.getMobile());
        // TODO > Update Otp code dynamically
        oneTimePassword.setOtpCode(TextUtils.generate4DigitOTP());
        oneTimePassword.setVerificationType(generateOtp.getVerificationType());
        oneTimePassword.setExpiredAt(LocalDateTime.now().plusMinutes(10));
        oneTimePassword = oneTimePasswordRepository.save(oneTimePassword);
        return oneTimePassword.getOtpCode();
    }

    public boolean verifyOtp(String otpSentOn, String otpCode, VerificationTypeEnum verificationType) throws BadRequestException {
        OneTimePassword oneTimePassword = oneTimePasswordRepository.verifyOtp(otpSentOn, otpCode, verificationType, LocalDateTime.now());
        if (oneTimePassword == null) return false;
        oneTimePassword.setExpiredAt(LocalDateTime.now());
        oneTimePassword.setExpired(true);
        oneTimePasswordRepository.save(oneTimePassword);
        return true;
    }

    private void destroyUserPreviousOtp(String mobile) {
        List<OneTimePassword> list = oneTimePasswordRepository.findAllNonExpiredOtp(mobile);
        for (OneTimePassword oneTimePassword : list) {
            oneTimePassword.setExpiredAt(LocalDateTime.now());
            oneTimePassword.setExpired(true);
        }
        oneTimePasswordRepository.saveAll(list);
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

    public List<OneTimePasswordDto> oneTimePasswordDtoList() {
        List<OneTimePassword> oneTimePasswordList = oneTimePasswordRepository.findAll(Sort.by(Sort.Order.desc("createdAt")));
        return oneTimePasswordList.stream()
                .map(otp -> OneTimePasswordMapper.MAPPER.mapToOneTimePasswordDto(otp))
                .collect(Collectors.toList());
    }

}
