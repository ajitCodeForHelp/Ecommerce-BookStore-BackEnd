package com.bt.ecommerce.primary.mapper;

import com.bt.ecommerce.primary.dto.StockInNotificationDto;
import com.bt.ecommerce.primary.pojo.StockInNotification;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface StockInNotificationMapper {
    StockInNotificationMapper MAPPER = Mappers.getMapper(StockInNotificationMapper.class);

    StockInNotification mapToPojo(StockInNotificationDto.SaveCustomerItemNotification saveCustomerItemNotification);

    StockInNotificationDto.DetailCustomerItemNotification mapToDetailDto(StockInNotification customerItemNotification);
}
