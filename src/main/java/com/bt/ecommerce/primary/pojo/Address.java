package com.bt.ecommerce.primary.pojo;


import com.bt.ecommerce.utils.TextUtils;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Field;

@Setter
@Getter
public class Address extends _BasicEntity {

    @Field("fk_parent_uuid")
    private String parentUuid;

    @Field("fk_parent_table")
    private String parentTable;

    @Field("latitude")
    private Double latitude;
    @Field("longitude")
    private Double longitude;

    @Field("address_line_1")
    private String addressLine1;

    @Field("address_line_2")
    private String addressLine2;

    @Field("address_line_3")
    private String addressLine3;

    @Field("fk_country_id")
    private Long countryId;
    @Field("fk_country_title")
    private String countryTitle;

    @Field("fk_state_id")
    private Long stateId;
    @Field("fk_state_title")
    private String stateTitle;

    @Field("fk_city_id")
    private Long cityId;
    @Field("fk_city_title")
    private String cityTitle;

    @Field("pin_code")
    private String pinCode;

    @Field("address_type")
    private String addressType; // Office / Home / Other

    @Override
    public String toString() {
        return (!TextUtils.isEmpty(addressLine1) ? addressLine1 + ", " : "") +
                (!TextUtils.isEmpty(addressLine2) ? addressLine2 + ", " : "") +
                (!TextUtils.isEmpty(addressLine3) ? addressLine3 + ", " : "") +
                (!TextUtils.isEmpty(cityTitle) ? cityTitle + ", " : "") +
                (!TextUtils.isEmpty(stateTitle) ? stateTitle + ", " : "") +
                (!TextUtils.isEmpty(countryTitle) ? countryTitle + ", " : "") +
                (!TextUtils.isEmpty(pinCode) ? "(" + pinCode + ")" : "");
    }

//    @Transient
//    private LocalCountry localCountry;
//    @Transient
//    private LocalState localState;
//    @Transient
//    private LocalCity localCity;
}
