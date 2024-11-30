package com.bt.ecommerce.primary.mapper;

import com.bt.ecommerce.primary.dto.OrderDto;
import com.bt.ecommerce.primary.pojo.Order;
import com.bt.ecommerce.primary.pojo.OrderHistory;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface OrderHistoryMapper {
    OrderHistoryMapper MAPPER = Mappers.getMapper(OrderHistoryMapper.class);

    OrderDto.DetailOrder mapToDetailOrderDto(OrderHistory orderHistory);

    OrderHistory mapToOrderHistory(Order order);
}
