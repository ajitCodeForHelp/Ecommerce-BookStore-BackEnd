package com.bt.ecommerce.primary.repository;

import com.bt.ecommerce.primary.pojo.Order;
import com.bt.ecommerce.primary.pojo.OrderHistory;
import com.bt.ecommerce.primary.pojo.enums.OrderStatusEnum;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderHistoryRepository extends MongoRepository<OrderHistory, ObjectId> {

    OrderHistory findByOrderId(String orderId);

    List<OrderHistory> findByCustomerId(ObjectId customerId);

    @Query(value = "{" +
            "  'orderStatus' : { '$in' : ?0 }," +
            "  'orderAt' : { '$gte' : ?1, '$lte' : ?2 }" +
            "}",
            sort = "{ 'orderTotal' : -1 }")
    List<Order> getDeliveredOrderList(List<OrderStatusEnum> statusEnumList, LocalDateTime startDate, LocalDateTime endDate);
}
