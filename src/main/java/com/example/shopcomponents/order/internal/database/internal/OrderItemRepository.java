package com.example.shopcomponents.order.internal.database.internal;

import org.springframework.data.repository.CrudRepository;

interface OrderItemRepository extends CrudRepository<OrderItemDatabaseEntity, Long> {

}
