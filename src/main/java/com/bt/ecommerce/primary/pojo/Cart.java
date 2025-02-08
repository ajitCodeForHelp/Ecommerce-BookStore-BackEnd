package com.bt.ecommerce.primary.pojo;

import com.bt.ecommerce.primary.pojo.common.BasicParent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Document(value = "cart")
public class Cart extends _BasicEntity {

    private String deviceId;
    private ObjectId customerId;
    private CustomerRefDetail customerDetail;
    private CustomerAddressDetail customerAddressDetail;

    private List<ObjectId> itemIds = new ArrayList<>();
    private List<ItemDetail> itemDetailList = new ArrayList<>();

    private boolean standardDelivery = true;
    private double subTotal = 0.0;

    private ObjectId couponCodeId;
    private CouponCode.CouponCodeRef couponCodeRefDetail;
    private double couponDiscountAmount = 0.0;
    private double packingCharges = 0.0;
    private double deliveryCharges = 0.0;
    private boolean cashOnDelivery;
    private double codCharges = 0.0;
    private double orderTotal = 0.0;
    private boolean codAvailable;


    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CustomerRefDetail {
        private String userCustomerUuid;
        private String userCustomerFirstName;
        private String userCustomerLastName;
        private String userCustomerIsdCode;
        private String userCustomerMobile;
        private String userCustomerEmail;

    }

    @Setter
    @Getter
    public static class ItemDetail {
        private BasicParent categoryDetail;

        private String itemUuid;
        private String title;
        private String customerDescription;
        private double Mrp;
        private double sellingPrice; /// use for calculation
        private List<String> itemImageUrls;
        private Boolean offerApplicable;
        private Long quantity = 1L;
        private double itemTotal;
        private BasicParent publisherDetails;
        private BasicParent taxDetails;
    }

    @Setter
    @Getter
    public static class CustomerAddressDetail {
        private String addressUuid;
        private Double latitude;
        private Double longitude;
        private String addressLine1;
        private String addressLine2;
        private String landMark;
        private String countryTitle;
        private String stateTitle;
        private String cityTitle;
        private String pinCode;

        private String firstName;
        private String lastName;
        private String mobileNumber;
        private String email;
    }


}
