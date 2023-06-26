package com.example.shopcomponents.cart.internal.database.internal;

import com.example.shopcomponents.cart.api.Article;
import com.example.shopcomponents.cart.api.ArticleId;
import com.example.shopcomponents.cart.api.ShoppingCart;
import com.example.shopcomponents.cart.api.ShoppingCartId;
import com.example.shopcomponents.cart.api.ShoppingCartItem;
import com.example.shopcomponents.cart.api.UserId;
import com.example.shopcomponents.cart.internal.database.api.ShoppingCartDatabase;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
class SpringJdbcShoppingCartDatabase implements ShoppingCartDatabase {

    private final ShoppingCartRepository shoppingCartRepository;
    private final ArticleRepository articleRepository;

    SpringJdbcShoppingCartDatabase(
            ShoppingCartRepository shoppingCartRepository,
            ArticleRepository articleRepository) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.articleRepository = articleRepository;
    }

    @Override
    public Optional<ShoppingCart> loadShoppingCartByUserId(UserId userId) {
        return shoppingCartRepository.findByUserId(userId.value())
                .map(this::toDomainObject);
    }

    @Override
    public ShoppingCart saveShoppingCart(ShoppingCart shoppingCart) {
        ShoppingCartDatabaseEntity shoppingCartToSave = fromDomainObject(shoppingCart);
        return toDomainObject(shoppingCartRepository.save(shoppingCartToSave));
    }

    private ShoppingCartDatabaseEntity fromDomainObject(ShoppingCart shoppingCart) {
        Set<ArticleRef> articleRefs = shoppingCart.getItems().stream()
                .map(i -> new ArticleRef(i.articleId().value()))
                .collect(Collectors.toSet());

        return new ShoppingCartDatabaseEntity(shoppingCart.getId().value(), shoppingCart.getUserId().value(), articleRefs);
    }

    private ShoppingCart toDomainObject(ShoppingCartDatabaseEntity shoppingCartDatabaseEntity) {
        Set<Long> articleIds = shoppingCartDatabaseEntity.articles().stream()
                .map(ArticleRef::articleId)
                .collect(Collectors.toSet());

        List<ShoppingCartItem> shoppingCartItems = StreamSupport.stream(articleRepository.findAllById(articleIds).spliterator(), false)
                .map(articleDatabaseEntity -> new ShoppingCartItem(
                        new ArticleId(articleDatabaseEntity.id()),
                        1,
                        articleDatabaseEntity.priceInCents()))
                .toList();

        return ShoppingCart.reconstitute(
                new ShoppingCartId(shoppingCartDatabaseEntity.id()),
                new UserId(shoppingCartDatabaseEntity.userId()),
                shoppingCartItems);
    }


    @Override
    public void updateShoppingCart(ShoppingCart shoppingCart) {
        shoppingCartRepository.save(fromDomainObject(shoppingCart));
    }

    @Override
    public Article addArticle(Article article) {
        ArticleDatabaseEntity savedArticle = articleRepository.save(fromDomainObject(article));
        return toDomainObject(savedArticle);
    }

    @Override
    public Optional<Article> loadArticle(ArticleId articleId) {
        return articleRepository.findById(articleId.value())
                .map(this::toDomainObject);
    }

    private Article toDomainObject(ArticleDatabaseEntity savedArticle) {
        return Article.reconstitute(new ArticleId(savedArticle.id()), savedArticle.name(), savedArticle.priceInCents());
    }

    private ArticleDatabaseEntity fromDomainObject(Article article) {
        return new ArticleDatabaseEntity(article.getId().value(), article.getName(), article.getPriceInCents());
    }

}
