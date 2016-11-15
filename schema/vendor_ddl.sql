CREATE TABLE vendor (
vendor_id       SERIAL,
name            varchar(50)     NOT NULL,
rating          smallint        NOT NULL,
website         varchar(50)     NOT NULL,
active_flag     smallint        NOT NULL,
PRIMARY KEY (vendor_id)
)