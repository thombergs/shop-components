package com.example.shopcomponents.cart.internal.database.api;

import com.example.shopcomponents.cart.api.Article;
import com.example.shopcomponents.cart.api.ArticleId;
import com.example.shopcomponents.cart.api.UserId;
import com.example.shopcomponents.cart.api.ShoppingCart;

import java.util.Optional;

public interface ShoppingCartDatabase {

    Optional<ShoppingCart> loadShoppingCartByUserId(UserId id);

    ShoppingCart saveShoppingCart(ShoppingCart shoppingCart);

    void updateShoppingCart(ShoppingCart shoppingCart);

    Article addArticle(Article article);

    Optional<Article> loadArticle(ArticleId articleId);

}
