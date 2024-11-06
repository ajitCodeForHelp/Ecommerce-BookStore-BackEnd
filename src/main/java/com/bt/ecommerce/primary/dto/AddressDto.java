package com.bt.ecommerce.primary.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AddressDto extends AbstractDto{

    @Getter
    @Setter
    public static class SaveAddress extends Save{
        private String customerUuid;
        private Double latitude;
        private Double longitude;
        private String addressLine1;
        private String addressLine2;
        private String addressLine3;
        private Long countryId;
        private String countryTitle;
        private Long stateId;
        private String stateTitle;
        private Long cityId;
        private String cityTitle;
        private String pinCode;
        private String addressType;
    }
    @Getter
    @Setter
    public static class UpdateAddress extends Update{
        private Double latitude;
        private Double longitude;
        private String addressLine1;
        private String addressLine2;
        private String addressLine3;
        private Long countryId;
        private String countryTitle;
        private Long stateId;
        private String stateTitle;
        private Long cityId;
        private String cityTitle;
        private String pinCode;
        private String addressType;
    }
    @Getter
    @Setter
    public static class DetailAddress extends Detail{
        private String customerUuid;
        private Double latitude;
        private Double longitude;
        private String addressLine1;
        private String addressLine2;
        private String addressLine3;
        private Long countryId;
        private String countryTitle;
        private Long stateId;
        private String stateTitle;
        private Long cityId;
        private String cityTitle;
        private String pinCode;
        private String addressType;
    }
}
