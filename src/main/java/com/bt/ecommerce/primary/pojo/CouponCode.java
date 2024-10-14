package com.bt.ecommerce.primary.pojo;

import com.bt.ecommerce.primary.pojo.enums.DiscountTypeEnum;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;

@Getter
@Setter
@Document(collation = "couponCode")
public class CouponCode extends _BasicEntity{

    private String title;
    private String couponCode; // Must Be Unique
    private String description;

    private LocalDate startDate;
    private LocalDate endDate;

    private DiscountTypeEnum discountType;

    private Long minOrderValue;
    private Double discountValue;
    private long maxDiscountAmount = 0;

    private Integer maxUsePerUser;
    private Integer usedCount = 0;
}
