package com.bt.ecommerce.primary.service;

import com.bt.ecommerce.exception.BadRequestException;
import com.bt.ecommerce.primary.dto.DashboardDto;
import com.bt.ecommerce.primary.pojo.Cart;
import com.bt.ecommerce.primary.pojo.Order;
import com.bt.ecommerce.primary.pojo.enums.OrderStatusEnum;
import com.bt.ecommerce.utils.DateUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class DashboardService extends _BaseService {
    public DashboardDto.DashboardReport getRestaurantDashboardReport(Long startTimeStamp, Long endTimeStamp) throws BadRequestException {
        // Date Processing
        LocalDate date = DateUtils.getLocalDate(startTimeStamp);
        LocalDateTime startDate = date.atStartOfDay();
        LocalDateTime endDate = DateUtils.getLocalDateTime(endTimeStamp);

        // Order Processing
        List<Cart> carts = cartRepository.findAll();
        List<Order> liveOrders = orderRepository.findAll();
        List<OrderStatusEnum> orderStatusEnumList = new ArrayList<>();
        orderStatusEnumList.add(OrderStatusEnum.DISPATCHED);
        orderStatusEnumList.add(OrderStatusEnum.DELIVERED);
        List<Order> orders = orderHistoryRepository.getDeliveredOrderList(orderStatusEnumList, startDate, endDate);


        // Prepare Dashboard Report
        DashboardDto.OrderReport orderReport = new DashboardDto.OrderReport();
        DashboardDto.EcommerceOrderReport ecommerceOrderReport = new DashboardDto.EcommerceOrderReport();
        List<DashboardDto.OrderSalesReport> top10DeliveredOrderList = new ArrayList<>();

        // Customer Report > Category Report > Item Report
        ecommerceOrderReport.setCustomerCount(customerRepository.count());
        ecommerceOrderReport.setCategoryCount(categoryRepository.count());
        ecommerceOrderReport.setItemCount(itemRepository.count());

        Map<LocalDate, DashboardDto.OrderReport> dateWiseOrderReportMap = new HashMap<>();
        Map<Month, DashboardDto.OrderReport> monthWiseOrderReportMap = new HashMap<>();
        Map<String, DashboardDto.CategorySalesReport> categorySalesMap = new HashMap<>();
        Map<String, DashboardDto.ItemSalesReport> itemSalesMap = new HashMap<>();

        int topRecordCount = 10;
        if (!CollectionUtils.isEmpty(carts)) {
            ecommerceOrderReport.setCartCount(carts.size());
            ecommerceOrderReport.setCartTotal(carts.stream().mapToDouble(Cart::getOrderTotal).sum());
        }
        if (!CollectionUtils.isEmpty(liveOrders)) {
            ecommerceOrderReport.setLiveOrderCount(liveOrders.size());
            ecommerceOrderReport.setLiveOrderTotal(liveOrders.stream().mapToDouble(Order::getOrderTotal).sum());
        }
        if (!CollectionUtils.isEmpty(orders)) {
            orderReport.setOrderCount(orders.size());
            orderReport.setOrderTotal(orders.stream().mapToDouble(Order::getOrderTotal).sum());

            for (Order order : orders) {

                // -------------------- Day Wise Order Sale Data------------------------------
                LocalDate localDate = order.getOrderAt().toLocalDate();
                DashboardDto.OrderReport dayOrderReport = dateWiseOrderReportMap.getOrDefault(localDate, new DashboardDto.OrderReport());
                if (dayOrderReport == null) {
                    dayOrderReport = new DashboardDto.OrderReport();
                }
                dayOrderReport.setOrderCount(dayOrderReport.getOrderCount() + 1);
                dayOrderReport.setOrderTotal(dayOrderReport.getOrderTotal() + order.getOrderTotal());
                dateWiseOrderReportMap.put(localDate, orderReport);

                // -------------------- Month Wise Order Sale Data------------------------------
                Month orderMonth = order.getOrderAt().toLocalDate().getMonth();
                DashboardDto.OrderReport monthOrderReport = monthWiseOrderReportMap.getOrDefault(orderMonth, new DashboardDto.OrderReport());
                if (monthOrderReport == null) {
                    monthOrderReport = new DashboardDto.OrderReport();
                }
                monthOrderReport.setOrderCount(monthOrderReport.getOrderCount() + 1);
                monthOrderReport.setOrderTotal(monthOrderReport.getOrderTotal() + order.getOrderTotal());
                monthWiseOrderReportMap.put(orderMonth, monthOrderReport);

                // ---------------------- Top 10 Order And There customer details ------------------------
                if (topRecordCount > 0) {
                    topRecordCount--;
                    DashboardDto.OrderSalesReport orderSalesReport = new DashboardDto.OrderSalesReport();
                    orderSalesReport.setOrderAt(DateUtils.getTimeStamp(order.getOrderAt()));
                    orderSalesReport.setOrderId(order.getOrderId());
                    orderSalesReport.setOrderTotal(order.getOrderTotal());
                    orderSalesReport.setCustomerDetail(order.getCustomerDetail());
                    top10DeliveredOrderList.add(orderSalesReport);
                }

                //--------------------------Category & Item --------------------------------
                for (Cart.ItemDetail item : order.getItemDetailList()) {
                    double itemTotal = item.getItemTotal();
                    Long itemQuantity = Objects.requireNonNullElse(item.getQuantity(), 1L);

                    // Category Sales Report
                    String categoryId = item.getCategoryDetail() != null ? item.getCategoryDetail().getParentUuid() : null;
                    if (categoryId != null) {
                        String categoryTitle = item.getCategoryDetail().getParentTitle() != null
                                ? item.getCategoryDetail().getParentTitle()
                                : "Un-Titled";

                        DashboardDto.CategorySalesReport categoryDto = categorySalesMap.getOrDefault(categoryId, new DashboardDto.CategorySalesReport());
                        categoryDto.setCategoryId(categoryId);
                        categoryDto.setCategoryTitle(categoryTitle);
                        categoryDto.setCategoryTotalAmount(categoryDto.getCategoryTotalAmount() + itemTotal);
                        categorySalesMap.put(categoryId, categoryDto);
                    }

                    // Item Sales Report
                    String itemId = item.getItemUuid();
                    if (itemId != null) {
                        String itemTitle = item.getTitle();
                        DashboardDto.ItemSalesReport itemDto = itemSalesMap.getOrDefault(itemId, new DashboardDto.ItemSalesReport());
                        itemDto.setItemId(itemId);
                        itemDto.setItemTitle(itemTitle);
                        itemDto.setItemQuantity(itemDto.getItemQuantity() + itemQuantity);
                        itemDto.setItemTotalAmount(itemDto.getItemTotalAmount() + itemTotal);
                        itemSalesMap.put(itemId, itemDto);
                    }
                }
            }
        }

        // Sort and prepare final lists
        List<DashboardDto.CategorySalesReport> sortedCategorySales = categorySalesMap.values().stream()
                .sorted(Comparator.comparingDouble(DashboardDto.CategorySalesReport::getCategoryTotalAmount).reversed())
                .collect(Collectors.toList());

        List<DashboardDto.ItemSalesReport> sortedItemSales = itemSalesMap.values().stream()
                .sorted(Comparator.comparingDouble(DashboardDto.ItemSalesReport::getItemTotalAmount).reversed())
                .collect(Collectors.toList());

        // Top N + Others
        List<DashboardDto.CategorySalesReport> top10Categories = getTopNWithOthersCategory(sortedCategorySales, 10);
        List<DashboardDto.ItemSalesReport> top10ItemsList = getTopNWithOthersItem(sortedItemSales, 10);


        // Prepare Dashboard Report
        DashboardDto.DashboardReport dashboardReport = new DashboardDto.DashboardReport();
        dashboardReport.setEcommerceOrderReport(ecommerceOrderReport);
        dashboardReport.setOrderReport(orderReport);
        dashboardReport.setDateWiseOrderReport(dateWiseOrderReportMap);
        dashboardReport.setCategorySalesReport(top10Categories);
        dashboardReport.setItemSalesReport(top10ItemsList);
        dashboardReport.setTop10OrderSalesReport(top10DeliveredOrderList);
        return dashboardReport;
    }

    private List<DashboardDto.CategorySalesReport> getTopNWithOthersCategory(List<DashboardDto.CategorySalesReport> sortedList, int limit) {
        if (sortedList.size() <= limit) {
            return sortedList;
        }
        List<DashboardDto.CategorySalesReport> top = new ArrayList<>(sortedList.subList(0, limit));
        Double othersTotal = sortedList.subList(limit, sortedList.size()).stream()
                .mapToDouble(DashboardDto.CategorySalesReport::getCategoryTotalAmount)
                .sum();

        DashboardDto.CategorySalesReport others = new DashboardDto.CategorySalesReport();
        others.setCategoryId("others");
        others.setCategoryTitle("Others");
        others.setCategoryTotalAmount(othersTotal);

        top.add(others);
        return top;
    }

    private List<DashboardDto.ItemSalesReport> getTopNWithOthersItem(List<DashboardDto.ItemSalesReport> sortedList, int limit) {
        if (sortedList.size() <= limit) {
            return sortedList;
        }

        List<DashboardDto.ItemSalesReport> top = new ArrayList<>(sortedList.subList(0, limit));

        double othersTotalAmount = sortedList.subList(limit, sortedList.size()).stream()
                .mapToDouble(DashboardDto.ItemSalesReport::getItemTotalAmount)
                .sum();

        long othersTotalQuantity = sortedList.subList(limit, sortedList.size()).stream()
                .mapToLong(DashboardDto.ItemSalesReport::getItemQuantity)
                .sum();

        DashboardDto.ItemSalesReport others = new DashboardDto.ItemSalesReport();
        others.setItemId("others");
        others.setItemTitle("Others");
        others.setItemTotalAmount(othersTotalAmount);
        others.setItemQuantity(othersTotalQuantity);

        top.add(others);
        return top;
    }

}
