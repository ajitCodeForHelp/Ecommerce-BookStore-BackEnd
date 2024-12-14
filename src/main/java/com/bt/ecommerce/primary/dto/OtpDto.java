package com.bt.ecommerce.primary.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OtpDto {
    private Long recordId;
    private Date createdAt;
    private String sentOn;
    private String sentOnEmailMobile;
    private String otpCode;
    private String verificationType;
    private String uuidKey;
    private Date expiredAt;

    @Getter
    @Setter
    public static class OTPLess {
        private String isdCode;
        private String phoneNumber;
    }

    @Getter
    @Setter
    public static class OTPBody {
        private String phoneNumber;
        private String otpLength;
        private String channel;
        private int expiry;

    }

    @Getter
    @Setter
    public static class OTPResponse {
        private String orderId;
        private String message;
    }

    @Getter
    @Setter
    public static class OTPVerify {
        private String isdCode;
        private String phoneNumber;
        private String otp;
        private String orderId;
    }

    @Getter
    @Setter
    public static class OTPVerifyResponse {
        @JsonProperty("isOTPVerified")
        private boolean isOTPVerified;
        private String message;
        private String reason;
    }

}
