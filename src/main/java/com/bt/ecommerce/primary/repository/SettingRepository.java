package com.bt.ecommerce.primary.repository;

import com.bt.ecommerce.primary.pojo.Setting;
import com.bt.ecommerce.primary.pojo.enums.SettingEnum;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SettingRepository extends MongoRepository<Setting, ObjectId> {

    Setting findFirstBySettingKey(SettingEnum packingCharges);

}

