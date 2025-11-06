package com.ecommerce.service;

import com.ecommerce.model.Order;
import com.ecommerce.model.CartItem;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderService {
    private static List<Order> orders = new ArrayList<>();
    private static int nextId = 1;

    public Order createOrder(int userId, List<CartItem> items, String deliveryAddress) {
        BigDecimal total = items.stream()
                .map(CartItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = new Order(nextId++, userId, new ArrayList<>(items), total, 
                               LocalDateTime.now(), Order.OrderStatus.PENDING, deliveryAddress);
        orders.add(order);
        return order;
    }

    public List<Order> getOrdersByUser(int userId) {
        return orders.stream().filter(o -> o.getUserId() == userId).toList();
    }

    public List<Order> getAllOrders() {
        return new ArrayList<>(orders);
    }

    public Order findById(int orderId) {
        return orders.stream().filter(o -> o.getId() == orderId).findFirst().orElse(null);
    }

    public void updateOrderStatus(int orderId, Order.OrderStatus status) {
        Order order = findById(orderId);
        if (order != null) {
            order.setStatus(status);
        }
    }
}