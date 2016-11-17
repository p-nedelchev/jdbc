CREATE TABLE addresses (
address_id          SERIAL,
address_line        varchar(50),
city                varchar(20),
post_code           int,
PRIMARY KEY(address_id)
)