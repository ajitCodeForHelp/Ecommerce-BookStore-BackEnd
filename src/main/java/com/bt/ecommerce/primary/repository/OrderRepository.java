package com.bt.ecommerce.primary.repository;

import com.bt.ecommerce.primary.pojo.Order;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface OrderRepository extends MongoRepository<Order, ObjectId> {

    Order findByOrderId(String uuid);

    List<Order> findByCustomerId(ObjectId customerId);
}
