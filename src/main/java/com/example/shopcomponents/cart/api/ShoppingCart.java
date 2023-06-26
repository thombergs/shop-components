package com.example.shopcomponents.cart.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;
import static java.util.function.UnaryOperator.identity;

public class ShoppingCart {

    private final ShoppingCartId id;

    private final UserId userId;

    private final Map<ArticleId, ShoppingCartItem> shoppingCartItems;

    private ShoppingCart(ShoppingCartId id, UserId userId, List<ShoppingCartItem> items) {
        requireNonNull(id);
        requireNonNull(userId);
        requireNonNull(items);
        this.id = id;
        this.userId = userId;
        this.shoppingCartItems = items.stream()
                .collect(Collectors.toMap(ShoppingCartItem::articleId, identity()));
    }

    /**
     * Use to create a new shopping cart that has never been stored in the database.
     */
    public static ShoppingCart createNew(UserId userId, List<ShoppingCartItem> items) {
        return new ShoppingCart(ShoppingCartId.NONE, userId, items);
    }

    /**
     * Use to reconstitute a shopping cart from the database (i.e. with an existing ID).
     */
    public static ShoppingCart reconstitute(ShoppingCartId id, UserId userId, List<ShoppingCartItem> items) {
        return new ShoppingCart(id, userId, items);
    }

    public ShoppingCartId getId() {
        return id;
    }

    public UserId getUserId() {
        return userId;
    }

    public void addItem(ShoppingCartItem item) {
        requireNonNull(item);

        // random business validation
        if (this.shoppingCartItems.size() >= 10) {
            throw new IllegalStateException("shopping cart may not contain more than 10 items at a time!");
        }

        this.shoppingCartItems.put(item.articleId(), item);
    }

    public void reset() {
        this.shoppingCartItems.clear();
    }

    public void removeItem(ArticleId articleId) {
        requireNonNull(articleId);
        this.shoppingCartItems.remove(articleId);
    }

    public Collection<ShoppingCartItem> getItems() {
        return this.shoppingCartItems.values();
    }

}
