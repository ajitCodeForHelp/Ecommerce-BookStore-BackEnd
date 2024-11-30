package com.bt.ecommerce.primary.mapper;

import com.bt.ecommerce.bean.KeyValueDto;
import com.bt.ecommerce.primary.dto.PublisherDto;
import com.bt.ecommerce.primary.pojo.Publisher;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PublisherMapper {
    PublisherMapper MAPPER = Mappers.getMapper(PublisherMapper.class);

    Publisher mapToPojo(PublisherDto.SavePublisher savePublisher);

    Publisher mapToPojo(@MappingTarget Publisher publisher, PublisherDto.UpdatePublisher update);
    PublisherDto.DetailPublisher mapToPublisherDetailDto(Publisher publisher);

    @Mapping(expression = "java(publisher.getUuid())", target = "key")
    @Mapping(expression = "java(publisher.getUuid())", target = "value")
    @Mapping(expression = "java(publisher.getTitle())", target = "label")
    KeyValueDto mapToKeyPairDto(Publisher publisher);
}
