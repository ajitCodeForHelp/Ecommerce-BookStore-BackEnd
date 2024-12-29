package com.bt.ecommerce.primary.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

public class CommonDto {

    @Setter
    @Getter
    public static class ChangePassword {
        @NotNull private String oldPassword;
        @NotNull private String newPassword;
    }

    @Setter
    @Getter
    public static class UpdateDeviceDetail {
        @NotNull private String deviceType;
        @NotNull private String fcmDeviceToken;
        private String deviceId;
    }
}
