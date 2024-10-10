//package com.bt.ecommerce.primary.pojo;
//
//import com.bt.ecommerce.primary.pojo.common.BasicParent;
//import lombok.Getter;
//import lombok.Setter;
//import org.bson.types.ObjectId;
//import org.springframework.data.mongodb.core.mapping.Document;
//import org.springframework.data.mongodb.core.mapping.Field;
//
//@Setter
//@Getter
//@Document(collection = "shop")
//public class Shop extends _BasicEntity {
//
//    @Field("fk_shop_owner_id")
//    private ObjectId shopOwnerId;
//    private BasicParent shopOwnerDetail;
//
//    @Field("fk_parent_admin_id")
//    private ObjectId parentAdminId;
//    private BasicParent parentAdminDetail;
//
//    private String orderPreFix;
//    private String title;
//
//    private Address address;
//
//    private String gstNo;
//    private String vatNo;
//
//    private String shopImageUrl;
//
//}
//
