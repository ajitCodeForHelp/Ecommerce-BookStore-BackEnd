package com.bt.ecommerce.primary.mapper;

import com.bt.ecommerce.primary.dto.CouponCodeDto;
import com.bt.ecommerce.primary.pojo.CouponCode;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CouponCodeMapper {
    CouponCodeMapper MAPPER = Mappers.getMapper(CouponCodeMapper.class);

    CouponCode mapToSaveCouponCode(CouponCodeDto.SaveCoupon saveCoupon);

    CouponCode mapToUpdateCouponCode(CouponCodeDto.UpdateCoupon updateCoupon);

    CouponCodeDto.DetailCoupon mapToCouponCodeDetailDto(CouponCode couponCode);
}
