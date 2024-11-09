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
}
