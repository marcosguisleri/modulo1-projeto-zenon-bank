CREATE TABLE TRANSACTIONS
(
    id                      BIGINT AUTO_INCREMENT PRIMARY KEY,
    step                    INT            NOT NULL,
    type                    VARCHAR(20)    NOT NULL,
    amount                  DECIMAL(19, 2) NOT NULL,

    origin_name             VARCHAR(100)   NOT NULL,
    origin_old_balance      DECIMAL(19, 2) NOT NULL,
    origin_new_balance      DECIMAL(19, 2) NOT NULL,

    destination_name        VARCHAR(100)   NOT NULL,
    destination_old_balance DECIMAL(19, 2) NOT NULL,
    destination_new_balance DECIMAL(19, 2) NOT NULL,

    fraud                   BOOLEAN        NOT NULL,
    flagged_fraud           BOOLEAN        NOT NULL
);