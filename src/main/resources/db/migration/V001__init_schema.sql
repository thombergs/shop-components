create table article
(
    id             integer auto_increment,
    name           varchar(50),
    price_in_cents integer,
    primary key (id)
);

create table shopping_cart
(
    id      integer auto_increment,
    user_id integer,
    primary key (id)
);

create table shopping_cart_article
(
    article_id       integer,
    shopping_cart_id integer,
    constraint fk_article foreign key (article_id) references article (id),
    constraint fk_shopping_cart foreign key (shopping_cart_id) references shopping_cart (id)
);