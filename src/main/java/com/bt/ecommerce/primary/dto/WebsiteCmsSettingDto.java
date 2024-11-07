package com.bt.ecommerce.primary.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class WebsiteCmsSettingDto extends AbstractDto {

    @Setter
    @Getter
    public static class UpdateSetting {
        private String settingValue;
    }
}
