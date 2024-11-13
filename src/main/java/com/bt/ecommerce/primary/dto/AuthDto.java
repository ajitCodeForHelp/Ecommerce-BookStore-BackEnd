package com.bt.ecommerce.primary.dto;

import com.bt.ecommerce.primary.pojo.enums.RoleEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

public class AuthDto {

    @Setter
    @Getter
    public static class AdminLogin {
        @NotNull private String userName;
        @NotNull private String password;
    }

    @Getter
    @Setter
    public static class CustomerLogin{
        @NotNull private String userName;
        @NotNull private String password;
    }

    @Setter
    @Getter
    public static class CustomerOtpLogin {
        @NotNull private String isdCode;
        @NotNull private String mobile;
        @NotNull private String otp;
    }

    @Setter
    @Getter
    public static class ForgotPassword {
        // First customer enter a mobile number and new password then otp sent to the that mobile number
        // if otp matched then new password will be updated
        @NotNull private String isdCode;
        @NotNull private String mobile;
        @NotNull private String newPassword;
        @NotNull private String otp;
    }


    @Setter
    @Getter
    public static class UserDetails {
        private String firstName;
        private String lastName;
        private String secretKey;
        private RoleEnum userType;
    }
}
