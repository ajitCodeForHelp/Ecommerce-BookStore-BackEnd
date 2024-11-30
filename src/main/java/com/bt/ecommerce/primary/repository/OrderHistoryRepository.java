package com.bt.ecommerce.primary.repository;

import com.bt.ecommerce.primary.pojo.OrderHistory;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface OrderHistoryRepository extends MongoRepository<OrderHistory, ObjectId> {

    OrderHistory findByOrderId(String orderId);

    List<OrderHistory> findByCustomerId(ObjectId customerId);
}
