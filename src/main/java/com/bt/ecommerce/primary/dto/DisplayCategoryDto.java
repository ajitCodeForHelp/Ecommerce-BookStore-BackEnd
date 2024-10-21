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
public class DisplayCategoryDto extends AbstractDto{

    @Setter
    @Getter
    public static class SaveDisplayCategory extends Save {
        @NotNull private String title;
        private String categoryIconUrl;
        private String description;
        private Boolean displayCategory = true;
    }
    @Setter
    @Getter
    public static class UpdateDisplayCategory extends Update {

        @NotNull private String title;
        private String categoryIconUrl;
        private String description;
    }
    @Setter
    @Getter
    public static class DetailDisplayCategory extends Detail {
        private String title;
        private String categoryIconUrl;
        private String description;
        private int sequenceNo;
        private Boolean displayCategory;


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

}
