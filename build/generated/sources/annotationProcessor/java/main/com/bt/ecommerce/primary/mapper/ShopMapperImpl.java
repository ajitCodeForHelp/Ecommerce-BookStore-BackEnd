package com.bt.ecommerce.primary.mapper;

import com.bt.ecommerce.primary.dto.ShopDto.Save;
import com.bt.ecommerce.primary.pojo.Shop;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-10-06T20:33:25+0530",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 17.0.9 (Oracle Corporation)"
)
@Component
public class ShopMapperImpl implements ShopMapper {

    @Override
    public Shop mapToPojo(Save create) {
        if ( create == null ) {
            return null;
        }

        Shop shop = new Shop();

        shop.setOrderPreFix( create.getOrderPreFix() );
        shop.setTitle( create.getTitle() );
        shop.setGstNo( create.getGstNo() );
        shop.setVatNo( create.getVatNo() );
        shop.setShopImageUrl( create.getShopImageUrl() );

        return shop;
    }
}
