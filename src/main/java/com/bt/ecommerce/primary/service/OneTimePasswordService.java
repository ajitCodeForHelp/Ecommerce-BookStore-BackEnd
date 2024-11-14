package com.bt.ecommerce.primary.service;

import com.bt.ecommerce.exception.BadRequestException;
import com.bt.ecommerce.primary.dto.CustomerDto;
import com.bt.ecommerce.primary.pojo.OneTimePassword;
import com.bt.ecommerce.primary.pojo.enums.MobileEmailEnum;
import com.bt.ecommerce.primary.pojo.enums.VerificationTypeEnum;
import com.bt.ecommerce.utils.TextUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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

}
