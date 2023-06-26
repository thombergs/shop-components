package com.example.shopcomponents.cart.internal.database.internal;

import org.springframework.data.relational.core.mapping.Table;

@Table("SHOPPING_CART_ARTICLE")
record ArticleRef(
        Long articleId
) {
}
