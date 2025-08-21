package com.bt.ecommerce.primary.repository;

import com.bt.ecommerce.primary.pojo.InventoryCart;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface InventoryCartRepository extends MongoRepository<InventoryCart, ObjectId> {

    InventoryCart findByUuid(String uuid);

    InventoryCart findByCustomerId(ObjectId customerId);
}
