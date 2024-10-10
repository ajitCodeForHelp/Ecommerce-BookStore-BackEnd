package com.bt.ecommerce.primary.dto;

import com.bt.ecommerce.primary.pojo.common.BasicParent;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
        private int sequenceNo;
    }
    @Setter
    @Getter
    public static class UpdateCategory extends Update {
        private String parentCategoryUuid;

        @NotNull private String title;
        private String categoryIconUrl;
        private String description;
        private int sequenceNo;
    }
    @Setter
    @Getter
    public static class DetailCategory extends Detail {
        private BasicParent parentCategoryDetail;
        private String title;
        private String categoryIconUrl;
        private String description;
        private int sequenceNo;
    }

    @Setter
    @Getter
    public static class GetList extends _BasePageRequest {
        private String search;
    }




}
