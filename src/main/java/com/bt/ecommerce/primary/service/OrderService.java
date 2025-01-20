package com.bt.ecommerce.primary.service;

import com.bt.ecommerce.configuration.SpringBeanContext;
import com.bt.ecommerce.exception.BadRequestException;
import com.bt.ecommerce.primary.dto.OrderDto;
import com.bt.ecommerce.primary.mapper.OrderHistoryMapper;
import com.bt.ecommerce.primary.mapper.OrderMapper;
import com.bt.ecommerce.primary.pojo.Order;
import com.bt.ecommerce.primary.pojo.OrderHistory;
import com.bt.ecommerce.primary.pojo.enums.OrderStatusEnum;
import com.bt.ecommerce.primary.pojo.user.Customer;
import com.bt.ecommerce.security.JwtUserDetailsService;
import com.bt.ecommerce.utils.TextUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderService extends _BaseService {

    public OrderDto.DetailOrder getOrderDetail(String orderId) throws BadRequestException {
        Order order = orderRepository.findByOrderId(orderId);
        if (order == null) {
            OrderHistory orderHistory = orderHistoryRepository.findByOrderId(orderId);
            if(orderHistory == null){
                throw new BadRequestException("Invalid OrderId Provided");
            }
            return OrderHistoryMapper.MAPPER.mapToDetailOrderDto(orderHistory);
        }
        return OrderMapper.MAPPER.mapToDetailOrderDto(order);
    }

    public void updateOrderTrackingId(String orderId, String orderTrackingId) throws BadRequestException {
        Order order = orderRepository.findByOrderId(orderId);
        if (order == null) {
            throw new BadRequestException("Invalid OrderId Provided");
        }
        if (!order.getOrderStatus().equals(OrderStatusEnum.ORDER)) {
            throw new BadRequestException("Invalid Update Request, Order Already Dispatched Or Delivered");
        }
        if (TextUtils.isEmpty(orderTrackingId)) {
            throw new BadRequestException("Invalid OrderTrackingId Provided");
        }
        order.setOrderTrackingId(orderTrackingId);
        order.setOrderStatus(OrderStatusEnum.DISPATCHED);
        order = updateOrderStatusLog(order, OrderStatusEnum.DISPATCHED);
        orderRepository.save(order);
            SpringBeanContext.getBean(OrderHistoryService.class).moveOrderToHistory();
    }

    public void updateOrdersTrackingId(List<OrderDto.UpdateOrdersTrackingIds> request) throws BadRequestException {
        Map<String, String> orderTrackingIdMap = new HashMap<>();
        for (OrderDto.UpdateOrdersTrackingIds updateOrdersTrackingIds : request) {
            orderTrackingIdMap.put(updateOrdersTrackingIds.getOrderId(), updateOrdersTrackingIds.getOrderTrackingId());
        }
        List<Order> orderList = orderRepository.findByOrderIds(orderTrackingIdMap.keySet().stream().toList());
        if (TextUtils.isEmpty(orderList)) {
            throw new BadRequestException("Invalid OrderId Provided");
        }
        for (Order order : orderList) {
//            if (!order.getOrderStatus().equals(OrderStatusEnum.ORDER)) {
//                throw new BadRequestException("Invalid Update Request, Order Already Dispatched");
//            }
            String orderTrackingId = orderTrackingIdMap.get(order.getOrderId());
            if (TextUtils.isEmpty(orderTrackingId)) {
                throw new BadRequestException("Invalid OrderTrackingId Provided");
            }
            order.setOrderTrackingId(orderTrackingId);
            order.setOrderStatus(OrderStatusEnum.DISPATCHED);
            order.setModifiedAt(LocalDateTime.now());
            order = updateOrderStatusLog(order, OrderStatusEnum.DISPATCHED);
        }
        // Update All Order Tracking Ids
        orderRepository.saveAll(orderList);
        SpringBeanContext.getBean(OrderHistoryService.class).moveOrderToHistory();

    }

    public void updateOrderStatus(String orderId, OrderStatusEnum orderStatus) throws BadRequestException {
        Order order = orderRepository.findByOrderId(orderId);
        if (order == null) {
            throw new BadRequestException("Invalid OrderId Provided");
        }
        if (orderStatus.equals(OrderStatusEnum.ORDER)) {
            return;
        }
        if (orderStatus.equals(OrderStatusEnum.DISPATCHED)) {
            if (!order.getOrderStatus().equals(OrderStatusEnum.ORDER)) {
                throw new BadRequestException("Order Must Be In ORDER Status");
            }
            if (TextUtils.isEmpty(order.getOrderTrackingId())) {
                throw new BadRequestException("Update OrderTrackingId In Order First");
            }
        }
        // if (orderStatus.equals(OrderStatusEnum.DELIVERED)) {
        //     if (!order.getOrderStatus().equals(OrderStatusEnum.DISPATCHED)) {
        //         throw new BadRequestException("Order Must Be In DISPATCHED Status");
        //     }
        //     // TODO > What is about > setPaymentStatus
        //     order.setPaymentStatus(PaymentStatusEnum.SUCCESS);
        // }
        order.setOrderStatus(orderStatus);
        order = updateOrderStatusLog(order, orderStatus);
        order = orderRepository.save(order);

        if (order.getOrderStatus().equals(OrderStatusEnum.DISPATCHED)) {
            // Move Order To History
            SpringBeanContext.getBean(OrderHistoryService.class).moveOrderToHistory();
        }
    }

    public long getOrderCount() {
        return orderRepository.count();
    }

    public List<OrderDto.DetailOrder> getOrderList() {
//        List<Order> orderList = orderRepository.findAll();
        List<Order> orderList = orderRepository.findAll(Sort.by(Sort.Order.desc("createdAt")));
        return orderList.stream()
                .map(order -> OrderMapper.MAPPER.mapToDetailOrderDto(order))
                .collect(Collectors.toList());
    }
    public Order updateOrderStatusLog(Order order, OrderStatusEnum orderStatusEnum) {
        List<Order.OrderStatusLog> orderStatusLogList = order.getOrderStatusLogList();
        Order.OrderStatusLog orderStatusLog = new Order.OrderStatusLog();
        orderStatusLog.setOrderStatusEnum(orderStatusEnum);
        orderStatusLog.setModifiedAt(LocalDateTime.now());
        orderStatusLogList.add(orderStatusLog);
        order.setOrderStatusLogList(orderStatusLogList);
        return order;
    }

    public List<OrderDto.DetailOrder> getCustomerOrderList() {
        // User Customer Order List Contains Live Order + History Orders
        Customer loggedInCustomer = (Customer) SpringBeanContext.getBean(JwtUserDetailsService.class).getLoggedInUser();
        List<Order> orderList = orderRepository.findByCustomerId(loggedInCustomer.getId());
        List<OrderDto.DetailOrder> customerOrderDtoList = new ArrayList<>();
        if (!TextUtils.isEmpty(orderList)) {
            customerOrderDtoList = orderList.stream()
                    .map(order -> OrderMapper.MAPPER.mapToDetailOrderDto(order))
                    .collect(Collectors.toList());
        }
        customerOrderDtoList.addAll(
                SpringBeanContext.getBean(OrderHistoryService.class).customerUserHistoryList(loggedInCustomer.getId()));
        return customerOrderDtoList;
    }
}
