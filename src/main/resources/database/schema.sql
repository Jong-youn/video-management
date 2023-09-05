CREATE TABLE IF NOT EXISTS role
(
    id    BIGINT NOT NULL AUTO_INCREMENT,
    name  VARCHAR(100),
    PRIMARY KEY (id)
);

INSERT INTO role (name)
    SELECT 'ROLE_USER'
    FROM dual WHERE NOT exists(SELECT * FROM role WHERE name = 'ROLE_USER');

INSERT INTO role (name)
    SELECT 'ROLE_ADMIN'
    FROM dual WHERE NOT exists(SELECT * FROM role WHERE name = 'ROLE_ADMIN');

CREATE TABLE IF NOT EXISTS user
(
    id             BIGINT NOT NULL AUTO_INCREMENT,
    role_id        BIGINT NOT NULL DEFAULT 1,
    email          VARCHAR(300) NOT NULL,
    password       VARCHAR(500) NOT NULL,
    name           VARCHAR(300),
    phone_number   VARCHAR(300),
    created_at     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),
    updated_at     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),
    deleted_at     DATETIME,
    PRIMARY KEY (id),
    FOREIGN KEY (role_id) REFERENCES role(id)
);

CREATE TABLE IF NOT EXISTS video
(
    id             BIGINT NOT NULL AUTO_INCREMENT,
    title          VARCHAR(100),
    user_id        BIGINT NOT NULL DEFAULT 1,
    created_at     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),
    deleted_at     DATETIME,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES user(id)
);