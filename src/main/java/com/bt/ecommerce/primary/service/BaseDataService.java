package com.bt.ecommerce.primary.service;

import com.bt.ecommerce.configuration.SpringBeanContext;
import com.bt.ecommerce.primary.pojo.Setting;
import com.bt.ecommerce.primary.pojo.enums.GenderEnum;
import com.bt.ecommerce.primary.pojo.enums.RoleEnum;
import com.bt.ecommerce.primary.pojo.enums.SettingEnum;
import com.bt.ecommerce.primary.pojo.user.SystemUser;
import com.bt.ecommerce.utils.ProjectConst;
import com.bt.ecommerce.utils.TextUtils;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class BaseDataService extends _BaseService {

    @PostConstruct
    private void postConstruct() {
        generateDefaultAdmins();
        SpringBeanContext.getBean(EcommerceDataService.class).generateEcommerceDefaultData();
        // Generate Default Settings
        createDefaultSettings();
    }

    public void generateDefaultAdmins() {
        {
            String superAdminEmail = "super@admin.com";
            SystemUser userAdmin = systemUserRepository.findFirstByUsername(superAdminEmail);
            if (userAdmin == null) {
                userAdmin = new SystemUser();
                userAdmin.setFirstName("SuperAdmin");
                userAdmin.setLastName("SuperAdmin");
                userAdmin.setGender(GenderEnum.Male);
                userAdmin.setIsdCode("+91");
                userAdmin.setMobile("9999999999");
                userAdmin.setEmail(superAdminEmail);
                userAdmin.setUsername(userAdmin.getEmail());
                userAdmin.setPwdSecure(TextUtils.getEncodedPassword("12"));
                userAdmin.setPwdText("12");
                userAdmin.setUserType(RoleEnum.ROLE_SUPER_ADMIN);
                userAdmin.setUniqueKey(TextUtils.getUniqueKey());
                systemUserRepository.save(userAdmin);
            }
        }

        {
            String adminEmail = "admin@admin.com";
            SystemUser userAdmin = systemUserRepository.findFirstByUsername(adminEmail);
            if (userAdmin == null) {
                userAdmin = new SystemUser();
                userAdmin.setFirstName("Admin");
                userAdmin.setLastName("Admin");
                userAdmin.setGender(GenderEnum.Male);
                userAdmin.setIsdCode("+91");
                userAdmin.setMobile("8888888888");
                userAdmin.setEmail(adminEmail);
                userAdmin.setUsername(userAdmin.getEmail());
                userAdmin.setPwdSecure(TextUtils.getEncodedPassword("12"));
                userAdmin.setPwdText("12");
                userAdmin.setUserType(RoleEnum.ROLE_ADMIN);
                userAdmin.setUniqueKey(TextUtils.getUniqueKey());
                systemUserRepository.save(userAdmin);
            }
        }
    }

    private void createDefaultSettings() {
        // Fetch Setting Data > Prepare Setting Map
        Map<SettingEnum, Setting> settingMap = new LinkedHashMap<>();
        List<Setting> settingList = settingRepository.findAll();
        for (Setting setting : settingList) {
            settingMap.put(setting.getSettingKey(), setting);
        }
        List<Setting> settingToBeUpdating = new ArrayList<>();
        for (SettingEnum settingEnum : SettingEnum.getToggleSettingsKey()) {
            if (!settingMap.containsKey(settingEnum)) {
                Setting setting = new Setting();
                setting.setSettingKey(settingEnum);
                setting.setSettingValue("0");
                settingToBeUpdating.add(setting);
            }
        }
        for (SettingEnum settingEnum : SettingEnum.getFloatSettingsKey()) {
            if (!settingMap.containsKey(settingEnum)) {
                Setting setting = new Setting();
                setting.setSettingKey(settingEnum);
                setting.setSettingValue("0.0");
                settingToBeUpdating.add(setting);
            }
        }
        for (SettingEnum settingEnum : SettingEnum.getStringSettingsKey()) {
            if (!settingMap.containsKey(settingEnum)) {
                Setting setting = new Setting();
                setting.setSettingKey(settingEnum);
                setting.setSettingValue("");
                settingToBeUpdating.add(setting);
            }
        }
        if (!settingToBeUpdating.isEmpty()) {
            settingRepository.saveAll(settingToBeUpdating);
        }
    }
}