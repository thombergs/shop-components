package com.example.shopcomponents.cart.internal.database.internal;

import org.springframework.data.repository.CrudRepository;

interface ArticleRepository extends CrudRepository<ArticleDatabaseEntity, Long> {
}
