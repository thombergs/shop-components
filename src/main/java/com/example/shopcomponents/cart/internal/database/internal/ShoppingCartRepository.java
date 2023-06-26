package com.example.shopcomponents.cart.internal.database.internal;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

interface ShoppingCartRepository extends CrudRepository<ShoppingCartDatabaseEntity, Long> {

    Optional<ShoppingCartDatabaseEntity> findByUserId(Long userId);

}
