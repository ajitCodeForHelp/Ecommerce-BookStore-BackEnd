package com.bt.ecommerce.primary.mapper;

import com.bt.ecommerce.primary.dto.OrderDto;
import com.bt.ecommerce.primary.pojo.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderMapper MAPPER = Mappers.getMapper(OrderMapper.class);


    @Mapping(expression = "java(com.bt.ecommerce.utils.DateUtils.getTimeStamp(order.getCreatedAt()))", target = "createdAt")
    @Mapping(expression = "java(com.bt.ecommerce.utils.DateUtils.getTimeStamp(order.getOrderAt()))", target = "orderAt")
    OrderDto.DetailOrder mapToDetailOrderDto(Order order);


}
