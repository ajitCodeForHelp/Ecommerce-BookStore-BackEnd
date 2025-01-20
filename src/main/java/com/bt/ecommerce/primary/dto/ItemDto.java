package com.bt.ecommerce.primary.dto;

import com.bt.ecommerce.primary.pojo.common.BasicParent;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class ItemDto extends AbstractDto{

    @Setter
    @Getter
    public static class SaveItem extends Save {
        @NotNull private List<String> parentCategoryUuids;
        private List<String> subCategoryUuids;

        @NotNull private String title;
        private String description;
        private String customerDescription;
        @NotNull private double Mrp;
        @NotNull private double sellingPrice;
        private List<String> itemImageUrls;
        private Boolean offerApplicable;
        private int sequenceNo;
        private double weight;
        private String publisherUuid;
        private String taxUuid;
        private String otherDataJson;

    }
    @Setter
    @Getter
    public static class UpdateItem extends Update {
        @NotNull private List<String> parentCategoryUuids;
        private List<String> subCategoryUuids;

        @NotNull private String title;
        private String description;
        private String customerDescription;
        @NotNull private double Mrp;
        @NotNull private double sellingPrice;
        private List<String> itemImageUrls;
        private Boolean offerApplicable;
        private int sequenceNo;
        private double weight;
        private String publisherUuid;
        private String taxUuid;
        private String otherDataJson;
    }
    @Setter
    @Getter
    public static class DetailItem extends Detail {
        private List<BasicParent> parentCategoryDetails;
        private List<BasicParent> subCategoryDetails;
        private BasicParent publisherDetails;
        private BasicParent taxDetails;

        private String title;
        private String customerDescription;
        private String description;
        private double Mrp;
        private double sellingPrice;
        private List<String> itemImageUrls;
        private Boolean offerApplicable;
        private int sequenceNo;
        private double weight;
        private String otherDataJson;
        private Boolean stockOut;
        private String itemCode;
    }

    @Setter
    @Getter
    public static class GetList extends _BasePageRequest {
        private String search;
    }
    @Setter
    @Getter
    public static class ItemSearchDto {
        private String uuid;
        private String title;
        private String customerDescription;
        private double Mrp;
        private double sellingPrice;
        private List<String> itemImageUrls;
        private Boolean offerApplicable;
        private int sequenceNo;
        private double weight;
        private String otherDataJson;
        private Boolean stockOut;
        private BasicParent publisherDetails;
        private BasicParent taxDetails;
    }


}
