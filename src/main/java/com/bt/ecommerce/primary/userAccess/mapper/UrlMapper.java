package com.bt.ecommerce.primary.userAccess.mapper;

import com.bt.ecommerce.bean.KeyValueDto;
import com.bt.ecommerce.primary.userAccess.dto.UrlDto;
import com.bt.ecommerce.primary.userAccess.pojo.Url;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UrlMapper {

    UrlMapper MAPPER = Mappers.getMapper(UrlMapper.class);

    @Mapping(expression = "java(com.bt.ecommerce.utils.DateUtils.getTimeStamp(url.getCreatedAt()))", target = "createdAtTimeStamp")
    @Mapping(expression = "java(com.bt.ecommerce.utils.DateUtils.getTimeStamp(url.getModifiedAt()))", target = "modifiedAtTimeStamp")
    UrlDto.DetailUrl mapToDetailDto(Url url);

    Url.UrlRef mapToRefDto(Url url);

    Url mapToPojo(UrlDto.SaveUrl create);

    Url mapToPojo(@MappingTarget Url url, UrlDto.UpdateUrl update);

    @Mapping(expression = "java(url.getUuid())", target = "key")
    @Mapping(expression = "java(url.getTitle())", target = "value")
    @Mapping(expression = "java(url.getTitle())", target = "label")
    KeyValueDto mapToKeyValueDto(Url url);

    Url.UrlRef mapToDetailRefDto(Url url);
}
