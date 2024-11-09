package com.bt.ecommerce.primary.pojo.user;

import com.bt.ecommerce.primary.pojo.common.BasicParent;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Setter
@Getter
@Document(collection = "system_user")
public class SystemUser extends _BaseUser/*implements UserDetails*/ {

    private String jobRole; // Account / Cashier

    private ObjectId parentAdminId; // For SUB_ADMIN
    private BasicParent parentAdminDetail;

    // User Assign Url List
    // urlUuidList == {Null or Empty} == access all data || SuperAdmin | Admin
    private List<String> urlUuidList;

}