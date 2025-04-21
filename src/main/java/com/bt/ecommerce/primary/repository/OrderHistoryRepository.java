package com.bt.ecommerce.primary.repository;

import com.bt.ecommerce.primary.dto.DashboardDto;
import com.bt.ecommerce.primary.pojo.Order;
import com.bt.ecommerce.primary.pojo.OrderHistory;
import com.bt.ecommerce.primary.pojo.enums.OrderStatusEnum;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Aggregation;
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


    @Aggregation(pipeline = {
            "{ $match: { orderAt: { $gte: ?0, $lte: ?1 } } }", // Match the date range
            "{ $group: { _id: null, orderCount: { $sum: 1 }, totalSales: { $sum: '$orderTotal' } } }"
    })
    DashboardDto.OrderReport getOrderStatsBetween(LocalDateTime start, LocalDateTime end);
    @Aggregation(pipeline = {
            "{ '$match': { 'orderAt': { '$gte': ?0 } } }",
            "{ '$project': { " +
                    "'month': { '$month': '$orderAt' }, " +
                    "'orderTotal': 1 } }",
            "{ '$group': { " +
                    "'_id': '$month', " +
                    "'orderCount': { '$sum': 1 }, " +
                    "'totalSales': { '$sum': '$orderTotal' } } }",
            "{ '$sort': { '_id': 1 } }",
            "{ '$project': { " +
                    "'month': '$_id', " +
                    "'orderCount': 1, " +
                    "'totalSales': 1, " +
                    "'_id': 0 } }"
    })
    List<DashboardDto.OrderMonthWiseChartData> getLastSixMonthsSales(LocalDateTime fromDate);
}
