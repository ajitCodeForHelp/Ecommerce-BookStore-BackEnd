package com.bt.ecommerce.primary.service;

import com.bt.ecommerce.exception.BadRequestException;
import com.bt.ecommerce.primary.dto.OrderDto;
import com.bt.ecommerce.primary.mapper.OrderMapper;
import com.bt.ecommerce.primary.pojo.Order;
import com.bt.ecommerce.primary.pojo.user.SystemUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public List<OrderDto.DetailOrder> getCustomerOrderList() throws BadRequestException {
        SystemUser systemUser = systemUserRepository.findAll().get(0);
        List<Order> orderList = orderRepository.findByCustomerId(systemUser.getId());
        return orderList.stream()
                .map(order -> OrderMapper.MAPPER.mapToDetailOrderDto(order))
                .collect(Collectors.toList());
    }

    public List<OrderDto.DetailOrder> getOrderList() throws BadRequestException {
        List<Order> orderList = orderRepository.findAll();
        return orderList.stream()
                .map(order -> OrderMapper.MAPPER.mapToDetailOrderDto(order))
                .collect(Collectors.toList());
    }

}
