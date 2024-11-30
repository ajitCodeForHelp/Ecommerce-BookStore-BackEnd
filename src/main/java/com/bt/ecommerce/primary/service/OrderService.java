package com.bt.ecommerce.primary.service;

import com.bt.ecommerce.configuration.SpringBeanContext;
import com.bt.ecommerce.exception.BadRequestException;
import com.bt.ecommerce.primary.dto.OrderDto;
import com.bt.ecommerce.primary.mapper.OrderMapper;
import com.bt.ecommerce.primary.pojo.Order;
import com.bt.ecommerce.primary.pojo.enums.OrderStatusEnum;
import com.bt.ecommerce.primary.pojo.enums.PaymentStatusEnum;
import com.bt.ecommerce.primary.pojo.user.Customer;
import com.bt.ecommerce.security.JwtUserDetailsService;
import com.bt.ecommerce.utils.TextUtils;
import lombok.extern.slf4j.Slf4j;
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
            throw new BadRequestException("Invalid OrderId Provided");
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
        orderRepository.save(order);
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
            if (!order.getOrderStatus().equals(OrderStatusEnum.ORDER)) {
                throw new BadRequestException("Invalid Update Request, Order Already Dispatched");
            }
            String orderTrackingId = orderTrackingIdMap.get(order.getOrderId());
            if (TextUtils.isEmpty(orderTrackingId)) {
                throw new BadRequestException("Invalid OrderTrackingId Provided");
            }
            order.setOrderTrackingId(orderTrackingId);
        }
        // Update All Order Tracking Ids
        orderRepository.saveAll(orderList);
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
        if (orderStatus.equals(OrderStatusEnum.DELIVERED)) {
            if (!order.getOrderStatus().equals(OrderStatusEnum.DISPATCHED)) {
                throw new BadRequestException("Order Must Be In DISPATCHED Status");
            }
            // TODO > What is about > setPaymentStatus
            order.setPaymentStatus(PaymentStatusEnum.SUCCESS);
        }
        order.setOrderStatus(orderStatus);
        order = updateOrderStatusLog(order, orderStatus);
        orderRepository.save(order);
    }

    public List<OrderDto.DetailOrder> getCustomerOrderList() throws BadRequestException {
        Customer loggedInCustomer = (Customer) SpringBeanContext.getBean(JwtUserDetailsService.class).getLoggedInUser();
        List<Order> orderList = orderRepository.findByCustomerId(loggedInCustomer.getId());
        return orderList.stream()
                .map(order -> OrderMapper.MAPPER.mapToDetailOrderDto(order))
                .collect(Collectors.toList());
    }
    public long getOrderCount() {
        return orderRepository.count();
    }

    public List<OrderDto.DetailOrder> getOrderList() throws BadRequestException {
        List<Order> orderList = orderRepository.findAll();
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
}
