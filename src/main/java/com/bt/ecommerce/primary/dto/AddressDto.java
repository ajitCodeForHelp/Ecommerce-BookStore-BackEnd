package com.bt.ecommerce.primary.dto;

import com.bt.ecommerce.primary.pojo.common.BasicParent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AddressDto extends AbstractDto{

    @Getter
    @Setter
    public static class SaveAddress extends Save {
        // In Case Order Is Delivery To Other Person
        private String firstName;
        private String lastName;
        private String mobileNumber;

        private Double latitude;
        private Double longitude;
        private String addressLine1;
        private String addressLine2;
        private String landMark;
        private String countryTitle;
        private String stateTitle;
        private String cityTitle;
        private String pinCode;
    }
    @Getter
    @Setter
    public static class UpdateAddress extends Update{
        // In Case Order Is Delivery To Other Person
        private String firstName;
        private String lastName;
        private String mobileNumber;

        private Double latitude;
        private Double longitude;
        private String addressLine1;
        private String addressLine2;
        private String landMark;
        private String countryTitle;
        private String stateTitle;
        private String cityTitle;
        private String pinCode;
    }
    @Getter
    @Setter
    public static class DetailAddress extends Detail {
        private BasicParent customerDetail;
        private String firstName;
        private String lastName;
        private String mobileNumber;

        private Double latitude;
        private Double longitude;
        private String addressLine1;
        private String addressLine2;
        private String landMark;
        private String countryTitle;
        private String stateTitle;
        private String cityTitle;
        private String pinCode;
    }
}
