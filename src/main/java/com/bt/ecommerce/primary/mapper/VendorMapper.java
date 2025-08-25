package com.bt.ecommerce.primary.mapper;

import com.bt.ecommerce.bean.KeyValueDto;
import com.bt.ecommerce.primary.dto.VendorDto;
import com.bt.ecommerce.primary.pojo.Vendor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface VendorMapper {
    VendorMapper MAPPER = Mappers.getMapper(VendorMapper.class);

    Vendor mapToPojo(VendorDto.SaveVendor saveVendor);

    Vendor mapToPojo(@MappingTarget Vendor vendor, VendorDto.UpdateVendor update);
    VendorDto.DetailVendor mapToVendorDetailDto(Vendor vendor);

    @Mapping(expression = "java(vendor.getUuid())", target = "key")
    @Mapping(expression = "java(vendor.getUuid())", target = "value")
    @Mapping(expression = "java(vendor.getTitle())", target = "label")
    KeyValueDto mapToKeyPairDto(Vendor vendor);
}
