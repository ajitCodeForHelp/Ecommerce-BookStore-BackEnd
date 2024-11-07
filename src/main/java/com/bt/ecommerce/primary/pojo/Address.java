package com.bt.ecommerce.primary.pojo;


import com.bt.ecommerce.primary.pojo.common.BasicParent;
import com.bt.ecommerce.utils.TextUtils;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@Document(value = "address")
public class Address extends _BasicEntity {

    private ObjectId customerId;
    private BasicParent customerDetail;

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

}
