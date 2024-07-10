CREATE TABLE if not exists T_USER
(
    id         INT AUTO_INCREMENT,
    username   VARCHAR(255) NOT NULL,
    email      VARCHAR(255) NOT NULL,

    first_name VARCHAR(255) NOT NULL,
    last_name  VARCHAR(255) NOT NULL,
    phone      VARCHAR(255) NOT NULL,

    street     VARCHAR(255) NOT NULL,
    city       VARCHAR(255) NOT NULL,
    state      VARCHAR(2)   NOT NULL,
    zip        VARCHAR(10)  NOT NULL,
    country    VARCHAR(255) NOT NULL,

    password   VARCHAR(20)  NOT NULL DEFAULT SUBSTRING(MD5(RAND()) FROM 1 FOR 20),
    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (id),
    CONSTRAINT UC_UserName UNIQUE (username),
    CONSTRAINT UC_Email UNIQUE (email)
);