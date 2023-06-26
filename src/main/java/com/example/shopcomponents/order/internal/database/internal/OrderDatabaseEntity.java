package com.example.shopcomponents.order.internal.database.internal;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("ORDER")
record OrderDatabaseEntity(
        @Id Long id,
        Long userId,
        LocalDateTime orderDate
) {
}
