CREATE TABLE Bank
(
    id               BIGINT NOT NULL PRIMARY KEY,
    name             VARCHAR NOT NULL,
    individual_fee   NUMERIC NOT NULL DEFAULT 0,
    legal_entity_fee NUMERIC NOT NULL DEFAULT 0
);

CREATE TABLE Client
(
    id   BIGINT PRIMARY KEY NOT NULL,
    name VARCHAR NOT NULL,
    type VARCHAR NOT NULL
);

CREATE TABLE Account
(
    id        BIGINT NOT NULL PRIMARY KEY,
    bank_id   BIGINT NOT NULL,
    client_id BIGINT,
    currency  VARCHAR NOT NULL,
    balance   NUMERIC NOT NULL DEFAULT 0,

    FOREIGN KEY (bank_id) REFERENCES Bank (id),
    FOREIGN KEY (client_id) REFERENCES Client (id)
);

CREATE TABLE Transaction
(
    id              BIGINT NOT NULL PRIMARY KEY,
    from_account_id BIGINT NOT NULL,
    to_account_id   BIGINT NOT NULL,
    amount          NUMERIC NOT NULL,
    currency        VARCHAR,
    date_time       TIMESTAMPTZ,
    FOREIGN KEY (from_account_id) REFERENCES Account (id),
    FOREIGN KEY (to_account_id) REFERENCES Account (id)
);