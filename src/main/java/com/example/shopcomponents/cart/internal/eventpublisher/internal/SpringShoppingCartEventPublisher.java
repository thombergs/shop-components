package com.example.shopcomponents.cart.internal.eventpublisher.internal;

import com.example.shopcomponents.cart.api.ShoppingCart;
import com.example.shopcomponents.cart.internal.eventpublisher.api.ShoppingCartEventPublisher;
import com.example.shopcomponents.events.CheckoutEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class SpringShoppingCartEventPublisher implements ShoppingCartEventPublisher {

    private final ApplicationEventPublisher eventPublisher;

    public SpringShoppingCartEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void publishCheckoutEvent(ShoppingCart shoppingCart) {
        eventPublisher.publishEvent(new CheckoutEvent(shoppingCart));
    }

}
