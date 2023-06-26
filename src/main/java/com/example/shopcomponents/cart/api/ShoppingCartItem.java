package com.example.shopcomponents.cart.api;

public record ShoppingCartItem(
        ArticleId articleId,
        int amount,
        long priceInCents
) {
}