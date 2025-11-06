package com.ecommerce.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class Order {
    private int id;
    private int userId;
    private List<CartItem> items;
    private BigDecimal total;
    private LocalDateTime orderDate;
    private OrderStatus status;
    private String deliveryAddress;

    public enum OrderStatus {
        PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED
    }

    public Order() {}

    public Order(int id, int userId, List<CartItem> items, BigDecimal total, LocalDateTime orderDate, OrderStatus status, String deliveryAddress) {
        this.id = id;
        this.userId = userId;
        this.items = items;
        this.total = total;
        this.orderDate = orderDate;
        this.status = status;
        this.deliveryAddress = deliveryAddress;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public List<CartItem> getItems() { return items; }
    public void setItems(List<CartItem> items) { this.items = items; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }

    public LocalDateTime getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDateTime orderDate) { this.orderDate = orderDate; }

    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }

    public String getDeliveryAddress() { return deliveryAddress; }
    public void setDeliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; }
}