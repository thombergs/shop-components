package com.example.shopcomponents.cart.internal.eventpublisher.api;

import com.example.shopcomponents.cart.api.ShoppingCart;

public interface ShoppingCartEventPublisher {

    void publishCheckoutEvent(ShoppingCart shoppingCart);

}
