CREATE TABLE meal_items
(
    id         UUID  NOT NULL,
    meal_id    UUID  NOT NULL,
    product_id UUID  NOT NULL,
    quantity   FLOAT NOT NULL,
    unit       VARCHAR(255),
    CONSTRAINT pk_meal_items PRIMARY KEY (id)
);

CREATE TABLE meals
(
    id      UUID NOT NULL,
    user_id UUID NOT NULL,
    type    VARCHAR(255),
    date    date,
    CONSTRAINT pk_meals PRIMARY KEY (id)
);

ALTER TABLE meals
    ADD CONSTRAINT FK_MEALS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE meal_items
    ADD CONSTRAINT FK_MEAL_ITEMS_ON_MEAL FOREIGN KEY (meal_id) REFERENCES meals (id);

ALTER TABLE meal_items
    ADD CONSTRAINT FK_MEAL_ITEMS_ON_PRODUCT FOREIGN KEY (product_id) REFERENCES products (id);


CREATE INDEX idx_meals_user_date ON meals (user_id, date);

CREATE INDEX idx_meal_items_meal_id ON meal_items (meal_id);

CREATE INDEX idx_meal_items_product_id ON meal_items (product_id);