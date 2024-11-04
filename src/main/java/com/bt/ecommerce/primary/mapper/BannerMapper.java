package com.bt.ecommerce.primary.mapper;

import com.bt.ecommerce.primary.dto.BannerDto;
import com.bt.ecommerce.primary.pojo.Banner;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface BannerMapper {
    BannerMapper MAPPER = Mappers.getMapper(BannerMapper.class);

    Banner mapToPojo(BannerDto.CreateBanner create);

    BannerDto.DetailBanner mapToPojo(Banner create);

    Banner mapToPojo(@MappingTarget Banner banner, BannerDto.UpdateDetail update);


}
