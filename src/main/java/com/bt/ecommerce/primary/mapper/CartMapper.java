package com.bt.ecommerce.primary.mapper;

import com.bt.ecommerce.primary.dto.CartDto;
import com.bt.ecommerce.primary.pojo.Cart;
import com.bt.ecommerce.primary.pojo.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CartMapper {
    CartMapper MAPPER = Mappers.getMapper(CartMapper.class);

    @Mapping(expression = "java(com.bt.ecommerce.utils.DateUtils.getTimeStamp(cart.getCreatedAt()))", target = "createdAtTimeStamp")
    @Mapping(expression = "java(com.bt.ecommerce.utils.DateUtils.getTimeStamp(cart.getModifiedAt()))", target = "modifiedAtTimeStamp")
    @Mapping(expression = "java(cart.getUuid())", target = "cartUuid")
    CartDto.DetailCart mapToDetailCartDto(Cart cart);

    Order mapToOrder(Cart cart);
}
