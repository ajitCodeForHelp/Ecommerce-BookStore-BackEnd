package com.bt.ecommerce.primary.mapper;

import com.bt.ecommerce.primary.dto.CartDto;
import com.bt.ecommerce.primary.pojo.Cart;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CartMapper {
    CartMapper MAPPER = Mappers.getMapper(CartMapper.class);
}
