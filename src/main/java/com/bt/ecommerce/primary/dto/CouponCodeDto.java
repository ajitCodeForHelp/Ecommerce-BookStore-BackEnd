package com.bt.ecommerce.primary.dto;

import com.bt.ecommerce.primary.pojo.enums.DiscountTypeEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class CouponCodeDto extends AbstractDto{

    @Getter
    @Setter
    public static class SaveCoupon extends Save{
        private String title;
        private String couponCode;
        private String description;
        private LocalDate startDate;
        private LocalDate endDate;
        private DiscountTypeEnum discountType;
        private Long minOrderValue;
        private Double discountValue;
        private long maxDiscountAmount;
        private Integer maxUsePerUser;
        private Integer usedCount;
        private Boolean referralCoupon;

    }

    @Getter
    @Setter
    public static class UpdateCoupon extends Update{
        private String title;
        private String couponCode;
        private String description;
        private LocalDate startDate;
        private LocalDate endDate;
        private DiscountTypeEnum discountType;
        private Long minOrderValue;
        private Double discountValue;
        private long maxDiscountAmount;
        private Integer maxUsePerUser;
        private Integer usedCount;
        private Boolean referralCoupon;
    }

    @Getter
    @Setter
    public static class DetailCoupon extends Detail{
        private String title;
        private String couponCode;
        private String description;
        private LocalDate startDate;
        private LocalDate endDate;
        private DiscountTypeEnum discountType;
        private Long minOrderValue;
        private Double discountValue;
        private long maxDiscountAmount;
        private Integer maxUsePerUser;
        private Integer usedCount;
        private Boolean referralCoupon;
    }
}
