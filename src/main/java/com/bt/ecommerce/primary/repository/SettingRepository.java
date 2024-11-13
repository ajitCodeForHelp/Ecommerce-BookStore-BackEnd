package com.bt.ecommerce.primary.repository;

import com.bt.ecommerce.primary.pojo.Setting;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SettingRepository extends MongoRepository<Setting, ObjectId> {

//    Setting findFirstBySettingKey(Setting settingKey);

}

