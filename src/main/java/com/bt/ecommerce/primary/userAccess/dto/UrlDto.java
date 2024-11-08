package com.bt.ecommerce.primary.userAccess.dto;

import com.bt.ecommerce.primary.dto.AbstractDto;
import com.bt.ecommerce.primary.pojo.common.BasicParent;
import com.bt.ecommerce.primary.userAccess.pojo.enums.AccessTypeEnum;
import com.bt.ecommerce.primary.userAccess.pojo.enums.MethodTypeEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class UrlDto extends AbstractDto {

    @Setter
    @Getter
    public static class DetailUrl extends Detail {
        private BasicParent moduleDetail;

        private String title;
        private String description;

        private MethodTypeEnum methodType;
        private String url;
        private AccessTypeEnum accessType;
    }

    @Setter
    @Getter
    public static class SaveUrl extends Save {
        @NotNull private String title;
        private String description;

        private MethodTypeEnum methodType;
        @NotNull private String url;
        private AccessTypeEnum accessType;
    }

    @Setter
    @Getter
    public static class UpdateUrl extends Update {
        @NotNull private String title;
        private String description;

        private MethodTypeEnum methodType;
        @NotNull private String url;
        private AccessTypeEnum accessType;
    }
}
