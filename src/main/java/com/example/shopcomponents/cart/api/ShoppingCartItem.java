package com.example.shopcomponents.cart.api;

import com.example.shopcomponents.cart.api.ArticleId;

public record ShoppingCartItem(
        ArticleId articleId,
        int amount,
        long priceInCents
) {
}