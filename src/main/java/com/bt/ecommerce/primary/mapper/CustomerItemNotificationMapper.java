package com.bt.ecommerce.primary.mapper;

import com.bt.ecommerce.primary.dto.CustomerItemNotificationDto;
import com.bt.ecommerce.primary.dto.DynamicFieldDto;
import com.bt.ecommerce.primary.pojo.CustomerItemNotification;
import com.bt.ecommerce.primary.pojo.DynamicField;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CustomerItemNotificationMapper {
    CustomerItemNotificationMapper MAPPER = Mappers.getMapper(CustomerItemNotificationMapper.class);

    CustomerItemNotification mapToPojo(CustomerItemNotificationDto.SaveCustomerItemNotification saveCustomerItemNotification);

    CustomerItemNotificationDto.DetailCustomerItemNotification mapToDetailDto(CustomerItemNotification customerItemNotification);
}
