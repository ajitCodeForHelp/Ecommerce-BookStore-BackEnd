package com.bt.ecommerce.primary.repository;

import com.bt.ecommerce.primary.pojo.Shop;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ShopRepository extends MongoRepository<Shop, ObjectId> {

}
