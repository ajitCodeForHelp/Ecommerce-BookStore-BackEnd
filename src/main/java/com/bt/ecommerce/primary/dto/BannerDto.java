package com.bt.ecommerce.primary.dto;

import com.bt.ecommerce.primary.pojo.common.BasicParent;
import com.bt.ecommerce.primary.pojo.enums.BannerTypeEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class BannerDto extends AbstractDto{

    @Getter
    @Setter
    public static class CreateBanner extends Save{
        private String title;
        private BannerTypeEnum bannerTypeEnum;
        private String bannerImageUrl;
    }

    @Getter
    @Setter
    public static class UpdateDetail extends Update{
        private String title;
        private String bannerImageUrl;
        private BannerTypeEnum bannerTypeEnum;
    }
    @Setter
    @Getter
    public static class GetList extends _BasePageRequest {
        private String search;
    }
    @Setter
    @Getter
    public static class DetailBanner extends Detail {
        private String title;
        private BannerTypeEnum bannerTypeEnum;
        private String bannerImageUrl;

    }

}
