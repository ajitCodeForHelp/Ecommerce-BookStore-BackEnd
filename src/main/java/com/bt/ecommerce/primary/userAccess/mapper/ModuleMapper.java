package com.bt.ecommerce.primary.userAccess.mapper;

import com.bt.ecommerce.bean.KeyValueDto;
import com.bt.ecommerce.primary.userAccess.dto.ModuleDto;
import com.bt.ecommerce.primary.userAccess.pojo.Module;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ModuleMapper {

    ModuleMapper MAPPER = Mappers.getMapper(ModuleMapper.class);

    @Mapping(expression = "java(com.bt.ecommerce.utils.DateUtils.getTimeStamp(module.getCreatedAt()))", target = "createdAtTimeStamp")
    @Mapping(expression = "java(com.bt.ecommerce.utils.DateUtils.getTimeStamp(module.getModifiedAt()))", target = "modifiedAtTimeStamp")
    ModuleDto.DetailModule mapToDetailDto(Module module);

    Module mapToPojo(ModuleDto.SaveModule create);

    Module mapToPojo(@MappingTarget Module module, ModuleDto.UpdateModule update);

    @Mapping(expression = "java(module.getUuid())", target = "key")
    @Mapping(expression = "java(module.getTitle())", target = "value")
    @Mapping(expression = "java(module.getTitle())", target = "label")
    KeyValueDto mapToKeyValueDto(Module module);

    Module.ModuleRef mapToDetailRefDto(Module module);
}
