package com.bt.ecommerce.primary.pojo;

import com.bt.ecommerce.primary.pojo.common.BasicParent;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Setter
@Getter
@Document(value = "category")
public class Category extends _BasicEntity {

    private String title;

    @Field("parent_category_id")
    private ObjectId parentCategoryId;
    private BasicParent parentCategoryDetail;

//    @Field("parent_shop_id")
//    private ObjectId parentShopId;
//    private BasicParent parentShopDetail;

    @Field("category_icon_ref_id")
    private String categoryIconRefId;

    @Field("description")
    private String description;

    private int sequenceNo;

}
