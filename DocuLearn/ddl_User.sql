CREATE TABLE user
(
    id         INT AUTO_INCREMENT     NOT NULL,
    username   VARCHAR(255)           NOT NULL,
    name       VARCHAR(255)           NOT NULL,
    birthday   date                   NULL,
    gender     VARCHAR(255)           NOT NULL,
    password   VARCHAR(255)           NOT NULL,
    `role`     VARCHAR(255)           NOT NULL,
    bio        LONGTEXT               NULL,
    created_at datetime DEFAULT NOW() NULL,
    update_at  datetime DEFAULT NOW() NULL,
    is_active  BIT(1)   DEFAULT 0     NULL,
    avatar     VARCHAR(255)           NULL,
    CONSTRAINT pk_user PRIMARY KEY (id)
);