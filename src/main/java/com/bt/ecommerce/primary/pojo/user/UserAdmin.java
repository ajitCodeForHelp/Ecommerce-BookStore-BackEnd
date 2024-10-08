package com.bt.ecommerce.primary.pojo.user;

import com.bt.ecommerce.primary.pojo.common.BasicParent;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Setter
@Getter
@Document(collection = "system_user")
public class UserAdmin extends _BaseUser/*implements UserDetails*/ {

    private ObjectId parentAdminId; // For SUB_ADMIN
    private BasicParent parentAdminDetail;

//    @Field(name = "fk_associated_shop_id")
//    private ObjectId associatedShopId;
//
//    private BasicParent associatedShopDetail;

}