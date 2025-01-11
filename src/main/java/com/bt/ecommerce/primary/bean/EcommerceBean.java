package com.bt.ecommerce.primary.bean;

import com.bt.ecommerce.primary.pojo.common.BasicParent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class EcommerceBean {

    @Getter
    @Setter
    public static class HomepageData {
        List<DashboardCategory> dashboardCategoryList;
        List<EcommerceCategory> displayCategoryList;
    }

    @Getter
    @Setter
    public static class DashboardCategory {
        private String uuid;
        private String title;
        private String categoryIconUrl;
        private String description;
        private int sequenceNo;
        private Boolean displayCategory;

        private List<DashboardCategory> subCategoryList;
    }

    @Getter
    @Setter
    public static class EcommerceCategory {
        private String uuid;
        private String title;
        private String categoryIconUrl;
        private String description;
        private int sequenceNo;
        private Boolean displayCategory;

        private List<EcommerceCategoryItem> displayCategoryItemList;
    }

    @Getter
    @Setter
    public static class EcommerceCategoryItem {
        private String uuid;
        private String title;
        private String description;
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
