package com.example.shopcomponents.cart.api;

public record UserId(Long value) {

    /**
     * Use when an entity doesn't have an ID (yet).
     */
    public static UserId NONE = new UserId(null);
}
