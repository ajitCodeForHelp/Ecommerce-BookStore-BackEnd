package com.bt.ecommerce.primary.dto;

import com.bt.ecommerce.primary.pojo.Cart;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class DashboardDto {

    @Getter
    @Setter
    public static class DashboardDataRequest {
        @NotNull private Long startTimestamp;
        @NotNull private Long endTimestamp;
    }

    @Getter
    @Setter
    public static class  DashboardOrderAndSalesStats {
       private Integer totalCart;
       private Integer totalOrder;
       private Double totalSale;
        private Integer customerCount;
        private long liveOrderCount;
        private List<OrderMonthWiseChartData> orderMonthWiseChartData;
    }

    @Getter
    @Setter
    public static class  DashboardReport {
        private EcommerceOrderReport ecommerceOrderReport;
        private OrderReport orderReport;
        private Map<LocalDate, OrderReport> dateWiseOrderReport;
        private List<CategorySalesReport> categorySalesReport;
        private List<ItemSalesReport> itemSalesReport;

        // *** To Know Top About Customer of Top 10 Highest Value Order > To Use This Data For There Quality
        private List<OrderSalesReport> top10OrderSalesReport; // Delivered Order
    }

    @Getter
    @Setter
    public static class EcommerceOrderReport {
        private Integer cartCount = 0;
        private Double cartTotal = 0.0;
        // Live Order
        private Integer liveOrderCount = 0;
        private Double liveOrderTotal = 0.0;

        // Other Associated Data
        private Long customerCount = 0L;
        private Long categoryCount = 0L;
        private Long itemCount = 0L;
    }

    @Getter
    @Setter
    public static class OrderReport {
        private Integer orderCount = 0;
        private Double totalSales = 0.0;
    }


    @Getter
    @Setter
    public static class OrderMonthWiseChartData {
        private String monthName;
        private int month;
        private int orderCount;
        private double totalSales;

        public OrderMonthWiseChartData(String monthName, int orderCount, double totalSales) {
            this.monthName = monthName;
            this.orderCount = orderCount;
            this.totalSales = totalSales;
        }
    }
    @Getter
    @Setter
    public static class OrderSalesReport {
        // Top 10 Order > By Price range
        private Long orderAt;
        private String orderId;
        private Cart.CustomerRefDetail customerDetail;
        private double orderTotal = 0.0;
    }

    @Getter
    @Setter
    public static class CategorySalesReport {
        private String categoryId;
        private String categoryTitle;
        private Double categoryTotalAmount = 0.0;
    }

    @Getter
    @Setter
    public static class ItemSalesReport {
        private String itemId;
        private String itemTitle;
        private Long itemQuantity = 0L;
        private Double itemTotalAmount = 0.0;
    }

}
