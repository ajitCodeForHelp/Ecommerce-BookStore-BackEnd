package com.bt.ecommerce.primary.pojo;

import com.bt.ecommerce.primary.pojo.common.BasicParent;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Setter
@Getter
@Document(value = "item")
public class Item extends _BasicEntity {

    private String title;

    @Field("description")
    private String description;

    @Field("parent_category_id")
    private List<ObjectId> parentCategoryId;
    private List<BasicParent> parentCategoryDetail;

    @Field("sub_category_id")
    private List<ObjectId> subCategoryId;
    private List<BasicParent> subCategoryDetail;

//    @Field("parent_shop_id")
//    private ObjectId parentShopId;
//    private BasicParent parentShopDetail;

    private double Mrp;
    private double sellingPrice;

    private List<String> itemImageUrls;

    private int sequenceNo;

    private double weight;

    //Todo Author ,Publications , Pages  , Publish Year ,Language
    // Color , Size , Dynamic Specification  (JSON)


}
