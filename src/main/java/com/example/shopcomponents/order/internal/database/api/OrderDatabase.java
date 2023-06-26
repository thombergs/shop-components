package com.example.shopcomponents.order.internal.database.api;

import com.example.shopcomponents.order.api.Order;

public interface OrderDatabase {

    Order saveOrder(Order order);

}
