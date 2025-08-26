package com.bt.ecommerce.primary.dto;

import com.bt.ecommerce.primary.pojo.InventoryCart;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class InventoryCartDto extends AbstractDto{

    @Getter
    @Setter
    public static class UpdateInventoryCart {
        private String itemUuid;
        private Map<String, Long> itemQuantityMap = new HashMap<>();
    }

    @Getter
    @Setter
    public static class DetailInventoryCart extends Detail{
        protected ObjectId id;
        private InventoryCart.StaffRefDetail customerDetail;
        private List<InventoryCart.ItemDetail> itemDetailList = new ArrayList<>();
    }

    @Getter
    @Setter
    public class UpdateItemsAsOrderedRequest {
        private List<CartItemUpdate> updates;
    }

    @Getter
    @Setter
    public  class CartItemUpdate {
        private String cartId;              // MongoDB _id (ObjectId as String)
        private List<String> itemUuids;     // Which items inside cart to update
    }

}
