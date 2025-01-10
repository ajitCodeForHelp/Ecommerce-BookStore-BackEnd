package com.bt.ecommerce.primary.service;

import com.bt.ecommerce.bean.KeyValueDto;
import com.bt.ecommerce.configuration.SpringBeanContext;
import com.bt.ecommerce.exception.BadRequestException;
import com.bt.ecommerce.primary.dto.WebsiteCmsSettingDto;
import com.bt.ecommerce.primary.pojo.WebsiteCms;
import com.bt.ecommerce.primary.pojo.enums.WebsiteCmsSettingEnum;
import com.bt.ecommerce.utils.Const;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WebsiteCmsSettingService extends _BaseService {
    public String get(WebsiteCmsSettingEnum settingKey) {
        WebsiteCms setting = websiteCmsSettingRepository.findFirstBySettingKey(settingKey);
        if (setting == null) {
            setting = new WebsiteCms();
            setting.setSettingKey(settingKey);
            setting.setSettingValue(setting.getSettingValue());
        }
        return setting.getSettingValue();
    }

    public void update(WebsiteCmsSettingEnum settingKey, WebsiteCmsSettingDto.UpdateSetting update) throws BadRequestException {
        WebsiteCms setting = websiteCmsSettingRepository.findFirstBySettingKey(settingKey);
        if (setting == null) {
            setting = new WebsiteCms();
            setting.setSettingKey(settingKey);
        }
        setting.setSettingValue(update.getSettingValue());
        websiteCmsSettingRepository.save(setting);
         SpringBeanContext.getBean(Const.class).refreshStaticContentCMSVariables();
    }

    public List<KeyValueDto> listInKeyValue() {
        List<KeyValueDto> keyValueDtoList = new ArrayList<>();
        List<WebsiteCms> settingList = websiteCmsSettingRepository.findAll();
        for (WebsiteCms setting : settingList) {
            KeyValueDto keyValueDto = new KeyValueDto();
            keyValueDto.setKey(setting.getSettingKey().toString());
            keyValueDto.setValue(setting.getSettingValue());
            keyValueDtoList.add(keyValueDto);
        }
        return keyValueDtoList;
    }
}
