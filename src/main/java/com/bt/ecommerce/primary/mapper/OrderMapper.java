package com.bt.ecommerce.primary.mapper;

import com.bt.ecommerce.primary.dto.OrderDto;
import com.bt.ecommerce.primary.pojo.Order;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderMapper MAPPER = Mappers.getMapper(OrderMapper.class);

    OrderDto.DetailOrder mapToDetailOrderDto(Order order);

}
