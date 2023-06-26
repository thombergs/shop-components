package com.example.shopcomponents.order.api;

public record OrderItem(
    long articleId,
    long amount,
    long priceInCents
) {
}
