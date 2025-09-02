package com.bt.ecommerce.primary.pojo;

import com.bt.ecommerce.primary.pojo.common.BasicParent;
import lombok.AllArgsConstructor;
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
public class InventoryCart extends _BasicEntity {
    private List<ObjectId> itemIds = new ArrayList<>();
    private ObjectId customerId;
    private InventoryCart.StaffRefDetail customerDetail;
    private List<InventoryCart.ItemDetail> itemDetailList = new ArrayList<>();

    @Setter
    @Getter
    public static class ItemDetail {
        private BasicParent categoryDetail;
        private String itemUuid;
        private String title;
        private List<String> itemImageUrls;
        private Long quantity = 1L;
        private Boolean isOrdered;
        private List<BasicParent> subCategoryDetails = new ArrayList<>();
        private BasicParent publisherDetails;
        private List<BasicParent> parentCategoryDetails = new ArrayList<>();
    }

    @Getter
    @Setter
    public static class UpdateInventoryCart {
        private String itemUuid;
        private Map<String, Long> itemQuantityMap = new HashMap<>();
    }
    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StaffRefDetail {
        private String staffUuid;
        private String staffFirstName;
        private String staffLastName;
    }

}