package com.example.shopcomponents.events;

import com.example.shopcomponents.cart.api.ShoppingCart;

public record CheckoutEvent(
        ShoppingCart shoppingCart
) {
}
