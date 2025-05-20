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
public class CategoryDto extends AbstractDto{

    @Setter
    @Getter
    public static class SaveCategory extends Save {
        private String parentCategoryUuid;

        @NotNull private String title;
        private String categoryIconUrl;
        private String description;
        private Boolean displayCategory = false;
    }
    @Setter
    @Getter
    public static class UpdateCategory extends Update {
        private String parentCategoryUuid;

        @NotNull private String title;
        private String categoryIconUrl;
        private String description;
        private Boolean displayCategory = false;
    }
    @Setter
    @Getter
    public static class DetailCategory extends Detail {
        private BasicParent parentCategoryDetail;
        private String title;
        private String categoryIconUrl;
        private String description;
        private int sequenceNo;
        private Boolean displayCategory = false;
        // category assign items
        private List<BasicParent> categoryAssignItems;
    }

    @Setter
    @Getter
    public static class GetList extends _BasePageRequest {
        private String search;
    }

    @Setter
    @Getter
    public static class AssignCategory {
      @NotNull private List<String> itemUuids;
    }


    @Setter
    @Getter
    public static class CatSubCatSequenceReorder {
        @NotNull private List<CatSubCatSequence> catSubCatSequences;
    }

    @Setter
    @Getter
    public static class CatSubCatSequence {
        @NotNull private String id;
        @NotNull private int sequenceNo;
    }

    @Setter
    @Getter
    public static class CatSubCatSequenceDetail {
         private String uuid;
        private int sequenceNo;
        private String title;
    }
    @Setter
    @Getter
    public static class ParentCategoryIds {
        @NotNull private List<String> parentCategoryUuids;
    }

}
