package com.bt.ecommerce.primary.repository;

import com.bt.ecommerce.primary.dto.DashboardDto;
import com.bt.ecommerce.primary.pojo.Order;
import com.bt.ecommerce.primary.pojo.enums.OrderStatusEnum;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends MongoRepository<Order, ObjectId> {

    Order findByOrderId(String uuid);

    List<Order> findByCustomerId(ObjectId customerId);

    @Query(value = "{" +
            "  'orderId' : { '$in' : ?0 }," +
            "}")
    List<Order> findByOrderIds(List<String> orderIds);

    List<Order> findByOrderStatus(OrderStatusEnum orderStatus);
    @Aggregation(pipeline = {
            "{ $match: { orderAt: { $gte: ?0, $lte: ?1 } } }",
            "{ $group: { _id: null, orderCount: { $sum: 1 }, totalSales: { $sum: '$orderTotal' } } }"
    })
    DashboardDto.OrderReport getOrderStatsBetween(LocalDateTime start, LocalDateTime end);

}
