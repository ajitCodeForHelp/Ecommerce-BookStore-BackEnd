package com.bt.ecommerce.primary.mapper;

import com.bt.ecommerce.bean.KeyValueDto;
import com.bt.ecommerce.primary.dto.CourierPartnerDto;
import com.bt.ecommerce.primary.pojo.CourierPartner;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CourierPartnerMapper {
    CourierPartnerMapper MAPPER = Mappers.getMapper(CourierPartnerMapper.class);

    CourierPartner mapToPojo(CourierPartnerDto.SaveCourierPartner saveCourierPartner);

    CourierPartner mapToPojo(@MappingTarget CourierPartner courierPartner, CourierPartnerDto.UpdateCourierPartner update);
    CourierPartnerDto.DetailCourierPartner mapToCourierPartnerDetailDto(CourierPartner courierPartner);

    @Mapping(expression = "java(courierPartner.getUuid())", target = "key")
    @Mapping(expression = "java(courierPartner.getUuid())", target = "value")
    @Mapping(expression = "java(courierPartner.getTitle())", target = "label")
    KeyValueDto mapToKeyPairDto(CourierPartner courierPartner);
}
