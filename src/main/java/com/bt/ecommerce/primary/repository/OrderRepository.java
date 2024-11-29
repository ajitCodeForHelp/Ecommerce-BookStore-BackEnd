package com.bt.ecommerce.primary.repository;

import com.bt.ecommerce.primary.pojo.Order;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface OrderRepository extends MongoRepository<Order, ObjectId> {

    Order findByOrderId(String uuid);

    List<Order> findByCustomerId(ObjectId customerId);

    @Query(value = "{" +
            "  'orderId' : { '$in' : ?0 }," +
            "}")
    List<Order> findByOrderIds(List<String> orderIds);
}
