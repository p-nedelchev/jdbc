CREATE TABLE vendor_history (
vendor_id       int,
name            varchar(50)     NOT NULL,
rating          smallint        NOT NULL,
website         varchar(50)     NOT NULL,
active_flag     smallint             NOT NULL,
date_changed    timestamp       NOT NULL
)