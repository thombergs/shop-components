package com.example.shopcomponents.order.internal;

import com.example.shopcomponents.cart.api.ShoppingCart;
import com.example.shopcomponents.cart.api.ShoppingCartItem;
import com.example.shopcomponents.order.api.Order;
import com.example.shopcomponents.order.api.OrderItem;
import com.example.shopcomponents.order.internal.database.api.OrderDatabase;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

import static java.util.Objects.requireNonNull;

@Component
@Transactional
public class OrderDomainService {

    private final OrderDatabase orderDatabase;

    public OrderDomainService(OrderDatabase orderDatabase) {
        this.orderDatabase = orderDatabase;
    }

    public void createOrderFromCart(ShoppingCart cart) {
        requireNonNull(cart);
        Order order = fromShoppingCart(cart);

        // TODO do a bunch of business validations like "may the user place an order at all?" or "are enough items in stock?"

        orderDatabase.saveOrder(order);
    }

    private Order fromShoppingCart(ShoppingCart cart) {
        return Order.createNew(fromShoppingCartItems(cart.getItems()), cart.getUserId().value());
    }

    private List<OrderItem> fromShoppingCartItems(Collection<ShoppingCartItem> shoppingCartItems) {
        return shoppingCartItems.stream()
                .map(i -> new OrderItem(i.articleId().value(), i.amount(), i.priceInCents()))
                .toList();
    }

}
