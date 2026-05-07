CREATE TABLE verification_token
(
    id      UUID                        NOT NULL,
    token   VARCHAR(255)                NOT NULL,
    type    VARCHAR(255)                NOT NULL,
    user_id UUID                        NOT NULL,
    expires TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_verificationtoken PRIMARY KEY (id),
    CONSTRAINT uc_verificationtoken_user UNIQUE (user_id),
    CONSTRAINT FK_VERIFICATIONTOKEN_ON_USER FOREIGN KEY (user_id) REFERENCES users (id)
);
