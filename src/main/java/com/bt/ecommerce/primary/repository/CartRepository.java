package com.bt.ecommerce.primary.repository;

import com.bt.ecommerce.primary.pojo.Cart;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDateTime;

public interface CartRepository extends MongoRepository<Cart, ObjectId> {
    Cart findByUuid(String uuid);

    Cart findByCustomerId(ObjectId customerId);

    Cart findByDeviceId(String deviceId);
    @Query(value = "{ 'createdAt': { $gte: ?0, $lte: ?1 }, 'orderTotal': { $gt: 0 } }", count = true)
    Integer countCartsWithOrderTotalGreaterThanZero(LocalDateTime start, LocalDateTime end);
}
