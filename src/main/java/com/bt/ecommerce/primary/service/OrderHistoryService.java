package com.bt.ecommerce.primary.service;

import com.bt.ecommerce.primary.dto.OrderDto;
import com.bt.ecommerce.primary.mapper.OrderHistoryMapper;
import com.bt.ecommerce.primary.pojo.Order;
import com.bt.ecommerce.primary.pojo.OrderHistory;
import com.bt.ecommerce.primary.pojo.enums.OrderStatusEnum;
import com.bt.ecommerce.utils.TextUtils;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderHistoryService extends _BaseService {

    // For Admin Panel
    public List<OrderDto.DetailOrder> orderHistoryList() {
        List<OrderHistory> orderHistoryList = orderHistoryRepository.findAll();
        return orderHistoryList.stream()
                .map(orderHistory -> OrderHistoryMapper.MAPPER.mapToDetailOrderDto(orderHistory))
                .collect(Collectors.toList());
    }

    public long getOrderHistoryCount() {
        return orderHistoryRepository.count();
    }

    // For Customer side use
    public List<OrderDto.DetailOrder> customerUserHistoryList(ObjectId userCustomerId) {
        List<OrderHistory> orderHistoryList = orderHistoryRepository.findByCustomerId(userCustomerId);
        List<OrderDto.DetailOrder> detailOrderList = new ArrayList<>();
        if (!TextUtils.isEmpty(orderHistoryList)) {
            detailOrderList = orderHistoryList.stream()
                    .map(orderHistory -> OrderHistoryMapper.MAPPER.mapToDetailOrderDto(orderHistory))
                    .collect(Collectors.toList());
        }
        return detailOrderList;
    }

    public void moveOrderToHistory() {
        List<Order> orderList = orderRepository.findByOrderStatus(OrderStatusEnum.DISPATCHED);
        if (TextUtils.isEmpty(orderList)) return;
        for (Order order : orderList) {
            OrderHistory orderHistory = OrderHistoryMapper.MAPPER.mapToOrderHistory(order);
            // Save Order To History
            orderHistoryRepository.save(orderHistory);
        }
        // Delete Order From History
        orderRepository.deleteAll(orderList);
    }
}
