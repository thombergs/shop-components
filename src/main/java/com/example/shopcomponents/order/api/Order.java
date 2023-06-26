package com.example.shopcomponents.order.api;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.Objects.requireNonNull;

public class Order {

    private final OrderId orderId;
    private final Long userId;
    private final List<OrderItem> items;
    private final LocalDateTime orderDate;

    private Order(OrderId orderId, Long userId, List<OrderItem> items, LocalDateTime orderDate) {
        requireNonNull(orderId);
        requireNonNull(userId);
        requireNonNull(items);
        requireNonNull(orderDate);
        this.userId = userId;
        this.orderId = orderId;
        this.items = items;
        this.orderDate = orderDate;
    }

    public static Order createNew(List<OrderItem> items, long userId) {
        return new Order(OrderId.NONE, userId, items, LocalDateTime.now());
    }

    public static Order reconstitute(OrderId id, long userId, List<OrderItem> items) {
        return new Order(id, userId, items, LocalDateTime.now());
    }

    public OrderId getOrderId() {
        return orderId;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public Long getUserId() {
        return userId;
    }
}
