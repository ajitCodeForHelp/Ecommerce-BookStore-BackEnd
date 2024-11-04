package com.bt.ecommerce.primary.pojo;

import com.bt.ecommerce.primary.pojo.enums.DiscountTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Getter
@Setter
@Document(value = "couponCode")
public class CouponCode extends _BasicEntity {

    private String title;
    private String couponCode; // Must Be Unique
    private String description;

    private LocalDate startDate;
    private LocalDate endDate;

    private DiscountTypeEnum discountType;
    // discountType = Percentage || discountType = Amount
    private Double discountValue;

    private Long minOrderValue;
    private long maxDiscountAmount = 0;

    private Integer maxUsePerUser;
    private Integer usedCount = 0;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CouponCodeRef {
        private String uuid;
        private String title;
        private String couponCode;
        private DiscountTypeEnum discountType;
    }
}
