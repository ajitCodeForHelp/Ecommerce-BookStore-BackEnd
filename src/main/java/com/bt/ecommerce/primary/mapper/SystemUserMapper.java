package com.bt.ecommerce.primary.mapper;

import com.bt.ecommerce.bean.KeyValueDto;
import com.bt.ecommerce.primary.dto.StaffDto;
import com.bt.ecommerce.primary.pojo.Cart;
import com.bt.ecommerce.primary.pojo.user.SystemUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface SystemUserMapper {
    SystemUserMapper MAPPER = Mappers.getMapper(SystemUserMapper.class);

    @Mapping(expression = "java(com.bt.ecommerce.utils.DateUtils.getTimeStamp(staff.getCreatedAt()))", target = "createdAtTimeStamp")
    @Mapping(expression = "java(com.bt.ecommerce.utils.DateUtils.getTimeStamp(staff.getModifiedAt()))", target = "modifiedAtTimeStamp")
    StaffDto.DetailStaff mapToDetailDto(SystemUser staff);

    SystemUser mapToPojo(StaffDto.SaveStaff create);

    SystemUser mapToPojo(@MappingTarget SystemUser staff, StaffDto.UpdateStaff update);

    @Mapping(expression = "java(staff.getUuid())", target = "key")
    @Mapping(expression = "java(staff.getUuid())", target = "value")
    @Mapping(expression = "java(staff.fullName())", target = "label")
    KeyValueDto mapToKeyPairDto(SystemUser staff);

    Cart.CustomerDetail mapToCartCustomer(SystemUser systemUser);
}
