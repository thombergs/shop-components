package com.example.shopcomponents.order.internal.eventlistener.internal;

import com.example.shopcomponents.events.CheckoutEvent;
import com.example.shopcomponents.order.internal.OrderDomainService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
class CheckoutEventListener {

    private final OrderDomainService orderService;

    CheckoutEventListener(OrderDomainService orderService) {
        this.orderService = orderService;
    }

    @EventListener(CheckoutEvent.class)
    public void onCheckoutEvent(CheckoutEvent event) {
        orderService.createOrderFromCart(event.shoppingCart());
    }

}
