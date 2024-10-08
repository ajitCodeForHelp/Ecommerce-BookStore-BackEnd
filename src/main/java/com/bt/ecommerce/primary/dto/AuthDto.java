package com.bt.ecommerce.primary.dto;

import com.bt.ecommerce.primary.pojo.enums.RoleEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

public class AuthDto {

    @Setter
    @Getter
    public static class AdminLogin {
        @NotNull
        private String userName;
        @NotNull
        private String password;
    }

    @Setter
    @Getter
    public static class ClientLogin {
        @NotNull
        private String userName;
        @NotNull
        private String password;
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
