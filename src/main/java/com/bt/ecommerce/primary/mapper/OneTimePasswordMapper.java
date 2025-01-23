package com.bt.ecommerce.primary.mapper;

import com.bt.ecommerce.primary.dto.ItemDto;
import com.bt.ecommerce.primary.dto.OneTimePasswordDto;
import com.bt.ecommerce.primary.pojo.Item;
import com.bt.ecommerce.primary.pojo.OneTimePassword;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface OneTimePasswordMapper {

    OneTimePasswordMapper MAPPER = Mappers.getMapper(OneTimePasswordMapper.class);
    OneTimePasswordDto mapToOneTimePasswordDto(OneTimePassword oneTimePassword);


}
