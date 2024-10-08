package com.bt.ecommerce.primary.dto;

import com.bt.ecommerce.primary.pojo.common.BasicParent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class AddressDto extends AbstractDto {

    @Setter
    @Getter
    public static class DetailAddress extends Detail {
        private BasicParent parentDetail;

        private BasicParent countryDetail;
        private BasicParent stateDetail;
        private BasicParent cityDetail;

        private Double latitude;
        private Double longitude;

        private String addressLine1;
        private String addressLine2;
        private String addressLine3;

        private String pinCode;
        private String addressType;
    }

    @Setter
    @Getter
    public static class SaveAddress extends Save {

    }

    @Setter
    @Getter
    public static class UpdateAddress extends Update {

    }
}
