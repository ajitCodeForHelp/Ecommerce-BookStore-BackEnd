package com.bt.ecommerce.primary.dto;

import com.bt.ecommerce.primary.pojo.enums.SettingEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class SettingDto extends AbstractDto {

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateSetting {
        private SettingEnum settingKey;
        private String settingValue;
    }

    @Setter
    @Getter
    public static class SettingList {
        private Map<SettingEnum, String> toggleSettings = new LinkedHashMap<>();
        private Map<SettingEnum, String> floatSettings = new LinkedHashMap<>();
        private Map<SettingEnum, String> stringSettings = new LinkedHashMap<>();
        private UpdateSetting deliveryCharges;
    }

    @Setter
    @Getter
    public static class UpdateSettings {
        private List<UpdateSetting> settings;
    }

    @Setter
    @Getter
    public static class DeliveryCharge {
        private Long startWeight; // grams
        private Long endWeight;  // grams
        private Double standardDeliveryCharges; // Rupees
        private Double expressDeliveryCharges;  // Rupees
    }
}
