--liquibase formatted sql
--changeset l8xwe@proton.me:202502220023 splitStatements:false
CREATE TABLE pet
(
    id   bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name varchar NOT NULL
        CONSTRAINT pet_natural_key UNIQUE
)