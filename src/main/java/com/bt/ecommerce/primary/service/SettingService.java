//package com.kpis.kpisFood.primary.service;
//
//
//import com.kpis.kpisFood.primary.pojo.Setting;
//import com.kpis.kpisFood.primary.pojo.enums.SettingEnum;
//import com.kpis.kpisFood.primary.repository.SettingRepository;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class SettingService extends AbstractService<Setting, SettingRepository> {
//    public SettingService(SettingRepository settingRepository) {
//        super(settingRepository);
//    }
//
//    public Setting findFirstBySettingKey(SettingEnum settingKey) {
//        return settingRepository.findFirstBySettingKey(settingKey);
//    }
//
//
//    public List<Setting> findAllRecords() {
//        return settingRepository.findAll();
//    }
//}
