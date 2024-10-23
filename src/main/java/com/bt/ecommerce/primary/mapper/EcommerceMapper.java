package com.bt.ecommerce.primary.mapper;

import com.bt.ecommerce.primary.bean.EcommerceBean;
import com.bt.ecommerce.primary.pojo.Address;
import com.bt.ecommerce.primary.pojo.Cart;
import com.bt.ecommerce.primary.pojo.Category;
import com.bt.ecommerce.primary.pojo.Item;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface EcommerceMapper {
    EcommerceMapper MAPPER = Mappers.getMapper(EcommerceMapper.class);

    EcommerceBean.DashboardCategory mapToDashboardCategory(Category category);
    EcommerceBean.DisplayCategory mapToDisplayCategory(Category category);
    EcommerceBean.DisplayCategoryItem mapToDisplayCategoryItem(Item item);
}
