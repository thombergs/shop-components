package com.example.shopcomponents.cart.api;

public interface ShoppingCartService {

    void addArticle(UserId userId, ArticleId articleId);

    void removeArticle(UserId userId, ArticleId id);

    void resetCart(UserId userId);

    void checkout(UserId userId);

    ShoppingCart getShoppingCart(UserId userId);

}
