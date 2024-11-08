package com.bt.ecommerce.primary.repository;

import com.bt.ecommerce.primary.pojo.Cart;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CartRepository extends MongoRepository<Cart, ObjectId> {
    Cart findByUuid(String uuid);

    Cart findByCustomerId(ObjectId customerId);

    Cart findByDeviceId(String deviceId);
}
