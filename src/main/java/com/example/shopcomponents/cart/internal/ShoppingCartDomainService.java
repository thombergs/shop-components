package com.example.shopcomponents.cart.internal;

import com.example.shopcomponents.cart.api.Article;
import com.example.shopcomponents.cart.api.ArticleId;
import com.example.shopcomponents.cart.api.ShoppingCart;
import com.example.shopcomponents.cart.api.ShoppingCartItem;
import com.example.shopcomponents.cart.api.ShoppingCartService;
import com.example.shopcomponents.cart.api.UserId;
import com.example.shopcomponents.cart.internal.database.api.ShoppingCartDatabase;
import com.example.shopcomponents.cart.internal.eventpublisher.api.ShoppingCartEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static java.util.Collections.emptyList;
import static java.util.Objects.requireNonNull;

@Component
@Transactional
public class ShoppingCartDomainService implements ShoppingCartService {

    private final ShoppingCartDatabase database;

    private final ShoppingCartEventPublisher shoppingCartEventPublisher;

    public ShoppingCartDomainService(ShoppingCartDatabase database, ShoppingCartEventPublisher shoppingCartEventPublisher) {
        this.database = database;
        this.shoppingCartEventPublisher = shoppingCartEventPublisher;
    }

    @Override
    public void addArticle(UserId userId, ArticleId articleId) {
        requireNonNull(userId);
        requireNonNull(articleId);

        ShoppingCart shoppingCart = database.loadShoppingCartByUserId(userId)
                .orElse(null);

        if (shoppingCart == null) {
            shoppingCart = createNewShoppingCart(userId);
        }

        Article article = database.loadArticle(articleId)
                .orElseThrow();

        shoppingCart.addItem(new ShoppingCartItem(articleId, 1, article.getPriceInCents()));

        database.saveShoppingCart(shoppingCart);
    }

    private ShoppingCart createNewShoppingCart(UserId userId) {
        ShoppingCart cart = ShoppingCart.createNew(userId, emptyList());
        return database.saveShoppingCart(cart);
    }

    @Override
    public void removeArticle(UserId userId, ArticleId articleId) {
        requireNonNull(userId);
        requireNonNull(articleId);

        ShoppingCart shoppingCart = database.loadShoppingCartByUserId(userId)
                .orElseThrow();

        shoppingCart.removeItem(articleId);

        database.saveShoppingCart(shoppingCart);
    }

    @Override
    public void resetCart(UserId userId) {
        requireNonNull(userId);

        ShoppingCart shoppingCart = database.loadShoppingCartByUserId(userId)
                .orElseThrow();

        shoppingCart.reset();

        database.saveShoppingCart(shoppingCart);
    }

    @Override
    public void checkout(UserId userId) {
        requireNonNull(userId);

        ShoppingCart shoppingCart = database.loadShoppingCartByUserId(userId)
                .orElseThrow();

        shoppingCartEventPublisher.publishCheckoutEvent(shoppingCart);

        resetCart(userId);
    }

    @Override
    public ShoppingCart getShoppingCart(UserId userId) {
        requireNonNull(userId);

        return database.loadShoppingCartByUserId(userId)
                .orElseThrow();
    }

}
