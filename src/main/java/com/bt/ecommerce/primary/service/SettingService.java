package com.bt.ecommerce.primary.service;

import com.bt.ecommerce.configuration.SpringBeanContext;
import com.bt.ecommerce.exception.BadRequestException;
import com.bt.ecommerce.primary.dto.SettingDto;
import com.bt.ecommerce.primary.pojo.Setting;
import com.bt.ecommerce.primary.pojo.enums.SettingEnum;
import com.bt.ecommerce.utils.Const;
import com.bt.ecommerce.utils.TextUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class SettingService extends _BaseService {

    public SettingDto.SettingList settingList() throws BadRequestException {
        // Fetch Setting Data > Prepare Setting Map
        Map<SettingEnum, Setting> settingMap = new LinkedHashMap<>();
        List<Setting> settingList = settingRepository.findAll();
        for (Setting setting : settingList) {
            settingMap.put(setting.getSettingKey(), setting);
        }

        SettingDto.SettingList systemSetting = new SettingDto.SettingList();
        for (SettingEnum settingEnum : SettingEnum.getToggleSettingsKey()) {
            if (settingMap.containsKey(settingEnum)) {
                systemSetting.getToggleSettings().put(
                        settingEnum, settingMap.get(settingEnum).getSettingValue().equals("1") ? "1" : "0"
                );
            } else {
                systemSetting.getToggleSettings().put(settingEnum, null);
            }
        }
        for (SettingEnum settingEnum : SettingEnum.getFloatSettingsKey()) {
            if (settingMap.containsKey(settingEnum)) {
                try {
                    systemSetting.getFloatSettings().put(
                            settingEnum, Double.parseDouble(settingMap.get(settingEnum).getSettingValue()) + ""
                    );
                } catch (Exception e) {
                    throw new BadRequestException("Error: The setting value '" + settingMap.get(settingEnum).getSettingValue() + "' is not a valid Double.");
                }
            } else {
                systemSetting.getFloatSettings().put(settingEnum, null);
            }
        }
        for (SettingEnum settingEnum : SettingEnum.getStringSettingsKey()) {
            if (settingMap.containsKey(settingEnum)) {
                systemSetting.getStringSettings().put(
                        settingEnum, settingMap.get(settingEnum).getSettingValue()
                );
            } else {
                systemSetting.getStringSettings().put(settingEnum, null);
            }
        }

        if (settingMap.containsKey(SettingEnum.DeliveryChargeJson)) {
//            Setting setting = settingMap.get(SettingEnum.DeliveryChargeJson);
//            Type type = new TypeToken<List<SettingDto.DeliveryCharge>>() {
//            }.getType();
//            List<SettingDto.DeliveryCharge> deliveryChargeList = new Gson().fromJson(setting.getSettingValue(), type);
            systemSetting.setDeliveryCharges(new SettingDto.UpdateSetting(SettingEnum.DeliveryChargeJson, settingMap.get(SettingEnum.DeliveryChargeJson).getSettingValue()));
        } else {
            systemSetting.setDeliveryCharges(new SettingDto.UpdateSetting(SettingEnum.DeliveryChargeJson, null));
        }
        return systemSetting;
    }

    public void updateSettings(SettingDto.UpdateSettings updateSettings) throws BadRequestException {
        Map<SettingEnum, Setting> settingMap = new LinkedHashMap<>();
        List<Setting> settingList = settingRepository.findAll();
        for (Setting setting : settingList) {
            settingMap.put(setting.getSettingKey(), setting);
        }

        List<Setting> settingsToUpdate = new ArrayList<>();
        for (SettingDto.UpdateSetting setting : updateSettings.getSettings()) {
            Setting updateSetting = null;
            if (settingMap.containsKey(setting.getSettingKey())) {
                updateSetting = settingMap.get(setting.getSettingKey());
            }
            if (updateSetting == null) {
                updateSetting = new Setting();
                updateSetting.setSettingKey(setting.getSettingKey());
            }

            if (SettingEnum.getToggleSettingsKey().contains(setting.getSettingKey())) {
                updateSetting.setSettingValue(setting.getSettingValue().equals("1") ? "1" : "0");
                SpringBeanContext.getBean(Const.class).refreshSystemSetting();
            }
            if (SettingEnum.getFloatSettingsKey().contains(setting.getSettingKey())) {
                try {
                    updateSetting.setSettingValue(Double.parseDouble(setting.getSettingValue()) + "");
                } catch (Exception e) {
                    throw new BadRequestException("Error: The setting value '" + setting.getSettingValue() + "' is not a valid Long.");
                }
            }
            if (SettingEnum.getStringSettingsKey().contains(setting.getSettingKey())) {
                updateSetting.setSettingValue(setting.getSettingValue());
                SpringBeanContext.getBean(Const.class).refreshSystemSetting();

            }
            // Delivery Charges >
            if (setting.getSettingKey().equals(SettingEnum.DeliveryChargeJson)) {
                // Validate That | deliveryChargeList |
                Type type = new TypeToken<List<SettingDto.DeliveryCharge>>() {
                }.getType();
                List<SettingDto.DeliveryCharge> deliveryChargeList = new Gson().fromJson(setting.getSettingValue(), type);
                updateSetting.setSettingValue(setting.getSettingValue());
            }
            settingsToUpdate.add(updateSetting);
        }
        settingRepository.saveAll(settingsToUpdate);
    }
    public double getBaseChargesValue(SettingEnum settingEnum) {
        if (!SettingEnum.getChargesList().contains(settingEnum)) {
            return 0.0;
        }
        Setting setting = settingRepository.findFirstBySettingKey(settingEnum);
        if (setting == null || setting.getSettingValue() == null) {
            return 0.0;
        } else {
            return Double.parseDouble(setting.getSettingValue());
        }
    }

    public double getDeliveryCharges(boolean standardDelivery, double totalItemPackageWeight) {
        Setting setting = settingRepository.findFirstBySettingKey(SettingEnum.DeliveryChargeJson);
        if (setting == null) return 0.0;
        Type type = new TypeToken<List<SettingDto.DeliveryCharge>>() {
        }.getType();
        List<SettingDto.DeliveryCharge> deliveryChargeList = new Gson().fromJson(setting.getSettingValue(), type);
        if (TextUtils.isEmpty(deliveryChargeList)) return 0.0;
        Double standardDeliveryCharges = 0.0;
        Double expressDeliveryCharges = 0.0;
        for (SettingDto.DeliveryCharge deliveryCharge : deliveryChargeList) {
            if (totalItemPackageWeight >= deliveryCharge.getStartWeight() && totalItemPackageWeight <= deliveryCharge.getEndWeight()) {
                standardDeliveryCharges = deliveryCharge.getStandardDeliveryCharges();
                expressDeliveryCharges = deliveryCharge.getExpressDeliveryCharges();
            }
        }
        return (standardDelivery) ? standardDeliveryCharges : expressDeliveryCharges;
    }

    public String getSettingValueByKey(SettingEnum key) {
        Setting setting = settingRepository.findFirstBySettingKey(key);
        if (setting != null) {
            return setting.getSettingValue();
        }
        return "";
    }
}
