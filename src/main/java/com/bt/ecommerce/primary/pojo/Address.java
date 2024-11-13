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

    // Order Delivery User Details
    private String firstName;
    private String lastName;
    private String mobileNumber;

    private Double latitude;
    private Double longitude;
    private String addressLine;
    private String countryTitle;
    private String stateTitle;
    private String cityTitle;

    private String pinCode;

    @Override
    public String toString() {
        return (!TextUtils.isEmpty(addressLine) ? addressLine + ", " : "") +
                (!TextUtils.isEmpty(cityTitle) ? cityTitle + ", " : "") +
                (!TextUtils.isEmpty(stateTitle) ? stateTitle + ", " : "") +
                (!TextUtils.isEmpty(countryTitle) ? countryTitle + ", " : "") +
                (!TextUtils.isEmpty(pinCode) ? "(" + pinCode + ")" : "");
    }

}
