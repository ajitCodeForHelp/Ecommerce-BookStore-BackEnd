package com.bt.ecommerce.primary.pojo;

import com.bt.ecommerce.primary.pojo.common.BasicParent;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Document(value = "item")
public class Item extends _BasicEntity {

    private List<ObjectId> parentCategoryIds = new ArrayList<>();
    private List<BasicParent> parentCategoryDetails = new ArrayList<>();
    private List<ObjectId> subCategoryIds = new ArrayList<>();
    private List<BasicParent> subCategoryDetails = new ArrayList<>();

    private ObjectId publisherId;
    private BasicParent publisherDetails;

    private ObjectId taxId;
    private BasicParent taxDetails;

    private String title;
    private String description;
    private String customerDescription;
    private double Mrp;
    private double sellingPrice;
    private List<String> itemImageUrls;
    private int sequenceNo;
    private double weight;
    private Boolean offerApplicable = Boolean.FALSE;
    private Boolean stockOut = Boolean.FALSE;
    private String otherDataJson; // other book associated details

    // Todo Author ,Publications , Pages  , Publish Year ,Language
    // Todo Color , Size , Dynamic Specification  (JSON)


    public boolean isStockOut() {
        if (stockOut == null) return false;
        return stockOut;
    }
    public boolean isOfferApplicable() {
        if (offerApplicable == null) return false;
        return offerApplicable;
    }
}
