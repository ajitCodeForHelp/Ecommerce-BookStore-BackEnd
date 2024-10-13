package com.bt.ecommerce.primary.pojo;

import com.bt.ecommerce.primary.pojo.common.BasicParent;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Setter
@Getter
@Document(value = "cart")
public class Cart extends _BasicEntity {

    private CustomerDetail customerDetail;
    private CustomerAddressDetail customerAddressDetail;

    private List<ItemDetail> itemDetailList;

    private double subTotal;
    private double couponDiscountAmount;
    private double orderTotal;


    @Setter
    @Getter
    public static class CustomerDetail {
        // TODO > modify this later on
        private Long userCustomerId;
        private String userCustomerFirstName;
        private String userCustomerLastName;
        private String userCustomerIsdCode;
        private String userCustomerMobile;
    }

    @Setter
    @Getter
    public static class ItemDetail {
        private BasicParent categoryDetail;

        private String itemUuid;
        private String title;
        private String description;
        private double Mrp;
        private double sellingPrice; /// use for calculation
        private List<String> itemImageUrls;

        private Long quantity;
        private double itemTotal;
    }

    @Setter
    @Getter
    public static class CustomerAddressDetail {
        private String addressUuid;

        private Double latitude;
        private Double longitude;
        private String address;
//        private String addressLine2;
//        private String addressLine3;
        private String countryTitle;
        private String stateTitle;
        private String cityTitle;
        private String pinCode;

        private String addressType; // Office / Home / Other
    }


}
