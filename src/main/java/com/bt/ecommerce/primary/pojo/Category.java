package com.bt.ecommerce.primary.pojo;

import com.bt.ecommerce.primary.pojo.common.BasicParent;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@Document(value = "category")
public class Category extends _BasicEntity {

    private ObjectId parentCategoryId;
    private BasicParent parentCategoryDetail;

    private String title;
    private String categoryIconUrl;
    private String description;
    private int sequenceNo;

}
