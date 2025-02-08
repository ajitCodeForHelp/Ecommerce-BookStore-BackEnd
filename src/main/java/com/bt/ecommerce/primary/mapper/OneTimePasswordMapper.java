package com.bt.ecommerce.primary.mapper;

import com.bt.ecommerce.primary.dto.ItemDto;
import com.bt.ecommerce.primary.dto.OneTimePasswordDto;
import com.bt.ecommerce.primary.pojo.Item;
import com.bt.ecommerce.primary.pojo.OneTimePassword;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface OneTimePasswordMapper {

    OneTimePasswordMapper MAPPER = Mappers.getMapper(OneTimePasswordMapper.class);


    @Mapping(expression = "java(com.bt.ecommerce.utils.DateUtils.getTimeStamp(oneTimePassword.getCreatedAt()))", target = "createdAt")
    @Mapping(expression = "java(com.bt.ecommerce.utils.DateUtils.getTimeStamp(oneTimePassword.getExpiredAt()))", target = "expiredAt")
    OneTimePasswordDto mapToOneTimePasswordDto(OneTimePassword oneTimePassword);


}
