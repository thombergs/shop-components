package com.example.shopcomponents.cart.internal.database.internal;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Table("ARTICLE")
record ArticleDatabaseEntity(
        @Id Long id,
        String name,
        Long priceInCents
) {
}
