package com.example.shopcomponents.cart.api;

import org.springframework.data.relational.core.mapping.Table;

public class Article {

    private final ArticleId id;
    private final String name;
    private final long priceInCents;

    private Article(ArticleId id,
                    String name,
                    long priceInCents) {
        this.id = id;
        this.name = name;
        this.priceInCents = priceInCents;
    }

    /**
     * Use to create a new article that has never been stored in the database.
     */
    public static Article createNew(String name, long priceInCents) {
        return new Article(ArticleId.NONE, name, priceInCents);
    }

    /**
     * Use to reconstitute an article from the database (i.e. with an existing ID).
     */
    public static Article reconstitute(ArticleId id, String name, long priceInCents) {
        return new Article(id, name, priceInCents);
    }

    public ArticleId getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getPriceInCents() {
        return priceInCents;
    }
}
