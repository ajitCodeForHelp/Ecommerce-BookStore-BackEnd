package com.bt.ecommerce.primary.mapper;

import com.bt.ecommerce.bean.KeyValueDto;
import com.bt.ecommerce.primary.dto.DisplayCategoryDto;
import com.bt.ecommerce.primary.dto.CategoryDto;
import com.bt.ecommerce.primary.pojo.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryMapper MAPPER = Mappers.getMapper(CategoryMapper.class);

    @Mapping(expression = "java(com.bt.ecommerce.utils.DateUtils.getTimeStamp(category.getCreatedAt()))", target = "createdAtTimeStamp")
    @Mapping(expression = "java(com.bt.ecommerce.utils.DateUtils.getTimeStamp(category.getModifiedAt()))", target = "modifiedAtTimeStamp")
    CategoryDto.DetailCategory mapToDetailDto(Category category);

    Category mapToPojo(CategoryDto.SaveCategory create);

    Category mapToPojo(DisplayCategoryDto.SaveDisplayCategory create);

    @Mapping(target = "title", expression = "java(com.bt.ecommerce.utils.TextUtils.getValidValue(category.getTitle(), update.getTitle()))")
    Category mapToPojo(@MappingTarget Category category, CategoryDto.UpdateCategory update);

    @Mapping(target = "title", expression = "java(com.bt.ecommerce.utils.TextUtils.getValidValue(category.getTitle(), update.getTitle()))")
    Category mapToPojo(@MappingTarget Category category, DisplayCategoryDto.UpdateDisplayCategory update);

    @Mapping(expression = "java(category.getUuid())", target = "key")
    @Mapping(expression = "java(category.getUuid())", target = "value")
    @Mapping(expression = "java(category.getTitle())", target = "label")
    KeyValueDto mapToKeyPairDto(Category category);
}
