package com.example.shopcomponents.order.api;

public record OrderId(Long value) {

    /**
     * Use when an entity doesn't have an ID (yet).
     */
    public static OrderId NONE = new OrderId(null);

}
