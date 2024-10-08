package com.bt.ecommerce.primary.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    AddressMapper MAPPER = Mappers.getMapper(AddressMapper.class);

}
