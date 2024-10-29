package com.bt.ecommerce.primary.mapper;

import com.bt.ecommerce.primary.dto.BannerDto.CreateBanner;
import com.bt.ecommerce.primary.dto.BannerDto.DetailBanner;
import com.bt.ecommerce.primary.dto.BannerDto.UpdateDetail;
import com.bt.ecommerce.primary.pojo.Banner;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-10-28T00:10:45+0530",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 17.0.5 (Amazon.com Inc.)"
)
@Component
public class BannerMapperImpl implements BannerMapper {

    @Override
    public Banner mapToPojo(CreateBanner create) {
        if ( create == null ) {
            return null;
        }

        Banner banner = new Banner();

        banner.setTitle( create.getTitle() );
        banner.setBannerImageUrl( create.getBannerImageUrl() );
        banner.setBannerTypeEnum( create.getBannerTypeEnum() );

        return banner;
    }

    @Override
    public DetailBanner mapToPojo(Banner create) {
        if ( create == null ) {
            return null;
        }

        DetailBanner detailBanner = new DetailBanner();

        detailBanner.setUuid( create.getUuid() );
        detailBanner.setActive( create.isActive() );
        detailBanner.setDeleted( create.isDeleted() );
        detailBanner.setTitle( create.getTitle() );
        detailBanner.setBannerTypeEnum( create.getBannerTypeEnum() );
        detailBanner.setBannerImageUrl( create.getBannerImageUrl() );

        return detailBanner;
    }

    @Override
    public Banner mapToPojo(Banner banner, UpdateDetail update) {
        if ( update == null ) {
            return null;
        }

        banner.setTitle( update.getTitle() );
        banner.setBannerImageUrl( update.getBannerImageUrl() );
        banner.setBannerTypeEnum( update.getBannerTypeEnum() );

        return banner;
    }
}
