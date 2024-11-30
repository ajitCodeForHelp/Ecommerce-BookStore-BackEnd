package com.bt.ecommerce.primary.mapper;

import com.bt.ecommerce.bean.KeyValueDto;
import com.bt.ecommerce.primary.dto.CartDto;
import com.bt.ecommerce.primary.dto.ItemDto;
import com.bt.ecommerce.primary.pojo.Cart;
import com.bt.ecommerce.primary.pojo.Item;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ItemMapper {
    ItemMapper MAPPER = Mappers.getMapper(ItemMapper.class);

    @Mapping(expression = "java(com.bt.ecommerce.utils.DateUtils.getTimeStamp(item.getCreatedAt()))", target = "createdAtTimeStamp")
    @Mapping(expression = "java(com.bt.ecommerce.utils.DateUtils.getTimeStamp(item.getModifiedAt()))", target = "modifiedAtTimeStamp")
    ItemDto.DetailItem mapToDetailDto(Item item);

    Item mapToPojo(ItemDto.SaveItem create);

    @Mapping(target = "title", expression = "java(com.bt.ecommerce.utils.TextUtils.getValidValue(item.getTitle(), update.getTitle()))")
    Item mapToPojo(@MappingTarget Item item, ItemDto.UpdateItem update);

    @Mapping(expression = "java(item.getUuid())", target = "key")
    @Mapping(expression = "java(item.getUuid())", target = "value")
    @Mapping(expression = "java(item.getTitle())", target = "label")
    KeyValueDto mapToKeyPairDto(Item item);

    @Mapping(expression = "java(item.getUuid())", target = "itemUuid")
    Cart.ItemDetail mapToCartItem(Item item);

    ItemDto.ItemSearchDto mapToItemSearchDto(Item item);
}
