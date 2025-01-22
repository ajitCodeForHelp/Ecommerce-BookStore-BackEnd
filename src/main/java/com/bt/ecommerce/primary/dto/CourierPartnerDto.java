package com.bt.ecommerce.primary.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CourierPartnerDto extends AbstractDto{
    @Getter
    @Setter
    public static class SaveCourierPartner extends Save{
        private String title;
        private String trackingUrl;
    }
    @Getter
    @Setter
    public static class UpdateCourierPartner extends Update{
        private String title;
        private String trackingUrl;
    }
    @Getter
    @Setter
    public static class DetailCourierPartner extends Detail{
        private String title;
        private String trackingUrl;
    }
}
