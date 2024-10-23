package com.bt.ecommerce.primary.repository;

import com.bt.ecommerce.primary.pojo.EcommerceJson;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EcommerceJsonRepository extends MongoRepository<EcommerceJson, ObjectId> {

    EcommerceJson findByKey(String key);
}
