--liquibase formatted sql
--changeset l8xwe@proton.me:202502220023 splitStatements:false
CREATE TYPE state AS enum ('DONE','IN_PROGRESS');
CREATE TABLE task
(
    id     bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    state  state   NOT NULL,
    count  integer NOT NULL,
    max    integer NOT NULL,
    min    integer NOT NULL,
    result integer array
);
CREATE UNIQUE INDEX ind_task_unique ON task (state, count, max, min) WHERE state = 'IN_PROGRESS'::state;