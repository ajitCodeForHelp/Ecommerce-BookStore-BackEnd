package com.bt.ecommerce.primary.mapper;

import com.bt.ecommerce.primary.dto.AbstractDto;
import com.bt.ecommerce.primary.dto.DynamicFieldDto;
import com.bt.ecommerce.primary.pojo.DynamicField;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface DynamicFieldMapper {
    DynamicFieldMapper MAPPER = Mappers.getMapper(DynamicFieldMapper.class);

    DynamicField mapToPojo(DynamicFieldDto.SaveDynamicField saveDynamicField);

    DynamicFieldDto.DetailDynamicField mapToCouponCodeDetailDto(DynamicField dynamicField);
}
