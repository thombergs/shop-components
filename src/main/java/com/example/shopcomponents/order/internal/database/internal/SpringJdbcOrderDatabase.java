package com.example.shopcomponents.order.internal.database.internal;

import com.example.shopcomponents.order.api.Order;
import com.example.shopcomponents.order.api.OrderId;
import com.example.shopcomponents.order.api.OrderItem;
import com.example.shopcomponents.order.internal.database.api.OrderDatabase;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.StreamSupport;

@Component
public class SpringJdbcOrderDatabase implements OrderDatabase {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    public SpringJdbcOrderDatabase(OrderRepository orderRepository, OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @Override
    public Order saveOrder(Order order) {
        OrderDatabaseEntity savedOrder = orderRepository.save(fromDomainObject(order));
        List<OrderItemDatabaseEntity> orderItems = fromDomainObject(savedOrder.id(), order.getItems());
        Iterable<OrderItemDatabaseEntity> savedItems = orderItemRepository.saveAll(orderItems);

        return toDomainObject(savedOrder, savedItems);
    }

    Order toDomainObject(OrderDatabaseEntity o, Iterable<OrderItemDatabaseEntity> items) {
        return Order.reconstitute(new OrderId(o.id()), o.userId(), toDomainObject(items));
    }

    List<OrderItem> toDomainObject(Iterable<OrderItemDatabaseEntity> items) {
        return StreamSupport.stream(items.spliterator(), false)
                .map(i -> new OrderItem(i.articleId(), 1, i.priceInCents()))
                .toList();
    }

    OrderDatabaseEntity fromDomainObject(Order order) {
        return new OrderDatabaseEntity(order.getOrderId().value(), order.getUserId(), order.getOrderDate());
    }

    List<OrderItemDatabaseEntity> fromDomainObject(Long orderId, List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(i -> new OrderItemDatabaseEntity(i.articleId(), orderId, i.amount(), i.articleId(), i.priceInCents()))
                .toList();
    }

}
