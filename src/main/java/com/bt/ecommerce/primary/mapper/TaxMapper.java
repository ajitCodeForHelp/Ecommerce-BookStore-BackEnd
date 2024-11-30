package com.bt.ecommerce.primary.mapper;

import com.bt.ecommerce.bean.KeyValueDto;
import com.bt.ecommerce.primary.dto.DynamicFieldDto;
import com.bt.ecommerce.primary.dto.PublisherDto;
import com.bt.ecommerce.primary.dto.TaxDto;
import com.bt.ecommerce.primary.pojo.DynamicField;
import com.bt.ecommerce.primary.pojo.Publisher;
import com.bt.ecommerce.primary.pojo.Tax;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TaxMapper {
    TaxMapper MAPPER = Mappers.getMapper(TaxMapper.class);

    Tax mapToPojo(TaxDto.SaveTax saveTax);
    TaxDto.DetailTax mapToTaxDetailDto(Tax tax);

    Tax mapToPojo(@MappingTarget Tax tax, TaxDto.UpdateTax update);

    @Mapping(expression = "java(tax.getUuid())", target = "key")
    @Mapping(expression = "java(tax.getUuid())", target = "value")
    @Mapping(expression = "java(tax.getTitle())", target = "label")
    KeyValueDto mapToKeyPairDto(Tax tax);

}
