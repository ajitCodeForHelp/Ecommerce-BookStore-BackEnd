package com.bt.ecommerce.primary.mapper;

import com.bt.ecommerce.primary.dto.InventoryCartDto;
import com.bt.ecommerce.primary.pojo.InventoryCart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface InventoryCartMapper {
    InventoryCartMapper MAPPER = Mappers.getMapper(InventoryCartMapper.class);
    @Mapping(expression = "java(com.bt.ecommerce.utils.DateUtils.getTimeStamp(cart.getCreatedAt()))", target = "createdAtTimeStamp")
    @Mapping(expression = "java(com.bt.ecommerce.utils.DateUtils.getTimeStamp(cart.getModifiedAt()))", target = "modifiedAtTimeStamp")
    InventoryCartDto.DetailInventoryCart mapToDetailInventoryCartDto(InventoryCart cart);

}
