package com.bt.ecommerce.primary.mapper;

import com.bt.ecommerce.primary.dto.ShopDto;
import com.bt.ecommerce.primary.pojo.Shop;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ShopMapper {
    ShopMapper MAPPER = Mappers.getMapper(ShopMapper.class);
    Shop mapToPojo(ShopDto.Save create);

}
