package com.bt.ecommerce.primary.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CustomerDto extends AbstractDto{

    @Setter
    @Getter
    public static class SaveCustomer extends Save {
        @NotNull private String firstName;
        @NotNull private String lastName;
        private String isdCode;
        private String mobile;
        private String email;
        @NotNull private String password;
        private String photoImageUrl;
    }
    @Setter
    @Getter
    public static class UpdateCustomer extends Update {
        @NotNull private String firstName;
        @NotNull private String lastName;
        private String isdCode;
        private String mobile;
        private String email;
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
