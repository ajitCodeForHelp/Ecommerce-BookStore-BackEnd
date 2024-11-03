package com.bt.ecommerce.primary.mapper;

import com.bt.ecommerce.primary.dto.AbstractDto;
import com.bt.ecommerce.primary.dto.AddressDto;
import com.bt.ecommerce.primary.pojo.Address;
import com.bt.ecommerce.primary.pojo.Cart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    AddressMapper MAPPER = Mappers.getMapper(AddressMapper.class);

    @Mapping(expression = "java(address.toString())", target = "address")
    Cart.CustomerAddressDetail mapToCartAddress(Address address);

    Address mapToSaveAddress(AddressDto.SaveAddress saveAddress);

    AddressDto.DetailAddress mapToDetailAddress(Address address);
}
