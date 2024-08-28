CREATE TABLE regions
(
    id          bigserial PRIMARY KEY,
    region_name varchar(100) NOT NULL UNIQUE,
    region_code bigint       NOT NULL UNIQUE
);

CREATE TABLE users
(
    id         bigserial PRIMARY KEY,
    first_name varchar(50)  NOT NULL,
    last_name  varchar(50)  NOT NULL,
    email      varchar(100) NOT NULL UNIQUE,
    region_id  bigint,
    CONSTRAINT fk_region FOREIGN KEY (region_id) REFERENCES regions (id)
);
