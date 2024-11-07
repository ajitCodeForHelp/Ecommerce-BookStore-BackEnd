package com.bt.ecommerce.primary.repository;

import com.bt.ecommerce.primary.pojo.WebsiteCms;
import com.bt.ecommerce.primary.pojo.enums.WebsiteCmsSettingEnum;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface WebsiteCmsSettingRepository extends MongoRepository<WebsiteCms, ObjectId> {

    WebsiteCms findFirstBySettingKey(WebsiteCmsSettingEnum settingKey);

}

