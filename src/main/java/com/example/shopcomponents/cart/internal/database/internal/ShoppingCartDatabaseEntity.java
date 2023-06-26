package com.example.shopcomponents.cart.internal.database.internal;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Set;

@Table("SHOPPING_CART")
record ShoppingCartDatabaseEntity(
        @Id Long id,
        Long userId,
        @MappedCollection(idColumn = "SHOPPING_CART_ID")
        Set<ArticleRef> articles
) {
}
