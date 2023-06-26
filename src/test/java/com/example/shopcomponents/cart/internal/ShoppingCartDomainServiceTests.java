package com.example.shopcomponents.cart.internal;

import com.example.shopcomponents.cart.api.Article;
import com.example.shopcomponents.cart.api.ShoppingCart;
import com.example.shopcomponents.cart.api.ShoppingCartService;
import com.example.shopcomponents.cart.api.UserId;
import com.example.shopcomponents.cart.internal.database.api.ShoppingCartDatabase;
import com.example.shopcomponents.cart.internal.eventpublisher.internal.SpringShoppingCartEventPublisher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import(ShoppingCartConfiguration.class)
@RecordApplicationEvents
class ShoppingCartDomainServiceTests {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private ShoppingCartDatabase shoppingCartDatabase;

    @Autowired
    private ApplicationEvents applicationEvents;

    private static final UserId USER_ID = new UserId(42L);

    List<Article> givenArticlesInDatabase() {
        Article article1 = Article.createNew("Get Your Hands Dirty on Clean Architecture", 20000);
        Article article2 = Article.createNew("Modulithic Applications with Spring", 39000);

        List<Article> articles = new ArrayList<>();
        articles.add(shoppingCartDatabase.addArticle(article1));
        articles.add(shoppingCartDatabase.addArticle(article2));

        return articles;
    }

    @Test
    ShoppingCart createShoppingCart() {
        List<Article> articles = givenArticlesInDatabase();

        shoppingCartService.addArticle(USER_ID, articles.get(0).getId());
        shoppingCartService.addArticle(USER_ID, articles.get(1).getId());
        ShoppingCart cart = shoppingCartService.getShoppingCart(USER_ID);
        assertThat(cart.getItems()).hasSize(2);

        return cart;
    }

    @Test
    void modifyShoppingCart() {
        ShoppingCart cart = createShoppingCart();
        assertThat(cart.getItems()).hasSize(2);

        shoppingCartService.removeArticle(USER_ID, cart.getItems().stream().findFirst().get().articleId());
        cart = shoppingCartService.getShoppingCart(USER_ID);
        assertThat(cart.getItems()).hasSize(1);
    }

    @Test
    void resetShoppingCart() {
        ShoppingCart cart = createShoppingCart();
        assertThat(cart.getItems()).hasSize(2);

        shoppingCartService.resetCart(USER_ID);
        cart = shoppingCartService.getShoppingCart(USER_ID);
        assertThat(cart.getItems()).isEmpty();
    }

    @Test
    void checkoutShoppingCart() {
        ShoppingCart cart = createShoppingCart();

        shoppingCartService.checkout(USER_ID);
        assertThat(applicationEvents.stream(SpringShoppingCartEventPublisher.CheckoutEvent.class)
                .count()).isEqualTo(1);
    }

}