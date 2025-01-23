package com.bt.ecommerce.primary.dto;

import com.bt.ecommerce.primary.pojo.enums.MobileEmailEnum;
import com.bt.ecommerce.primary.pojo.enums.VerificationTypeEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
public class OneTimePasswordDto {

    private String otpSentOn;

    private String otpCode;

    private LocalDateTime expiredAt;

    private boolean expired = false;

    protected LocalDateTime createdAt;


}
