package com.example.shopcomponents.cart.api;

public record ArticleId(Long value) {

    /**
     * Use when an entity doesn't have an ID (yet).
     */
    public static ArticleId NONE = new ArticleId(null);

}
