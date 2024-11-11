package com.bt.ecommerce.primary.dto;

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
    public static class GenerateOtp{
        @NotNull private String isdCode;
        @NotNull private String mobile;
    }

    @Setter
    @Getter
    public static class LoginCustomer extends Save {
        @NotNull private String isdCode;
        @NotNull private String mobile;
        private String otp;
        private String password;
    }
    @Setter
    @Getter
    public static class UpdateCustomer extends Update {
        @NotNull private String firstName;
        @NotNull private String lastName;
//        @NotNull private String isdCode;
//        @NotNull private String mobile;
        @NotNull private String email;
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
