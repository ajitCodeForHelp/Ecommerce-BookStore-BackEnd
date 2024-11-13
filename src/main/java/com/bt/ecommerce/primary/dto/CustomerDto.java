package com.bt.ecommerce.primary.dto;

import com.bt.ecommerce.primary.pojo.enums.VerificationTypeEnum;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CustomerDto extends AbstractDto{

    @Getter
    @Setter
    public static class GenerateOtp {
        @NotNull private String isdCode;
        @NotNull private String mobile;
        @NotNull private VerificationTypeEnum verificationType;
    }

    @Setter
    @Getter
    public static class UpdateCustomer extends Update {
        @NotNull private String firstName;
        @NotNull private String lastName;
        private String photoImageUrl;
    }
    @Setter
    @Getter
    public static class DetailCustomer extends Detail {
        private String firstName;
        private String lastName;
        private String isdCode;
        private String mobile;
        private String email;
        private String photoImageUrl;
    }
}
