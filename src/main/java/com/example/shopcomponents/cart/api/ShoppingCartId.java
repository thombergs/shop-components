package com.example.shopcomponents.cart.api;

public record ShoppingCartId(Long value) {

    /**
     * Use when an entity doesn't have an ID (yet).
     */
    public static ShoppingCartId NONE = new ShoppingCartId(null);

}
