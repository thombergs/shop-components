package com.example.shopcomponents.order.internal.database.internal;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("ORDER_ITEM")
record OrderItemDatabaseEntity(
        @Id Long id,
        Long orderId,
        Long amount,
        Long articleId,
        Long priceInCents
) {
}
