CREATE TABLE STOCK(
NAME  VARCHAR(10) NOT NULL,
PRICE   DOUBLE(7,1) NOT NULL PRIMARY KEY,
QUANTITY DOUBLE(10,1) NOT NULL
);

CREATE TABLE STOCK_HISTORY(
NAME VARCHAR(20) NOT NULL,
PRICE DOUBLE(10,1) NOT NULL,
QUANTITY DOUBLE(10,1) NOT NULL
);

DELIMITER $$
CREATE TRIGGER quantityChangeHistory BEFORE UPDATE ON STOCK
FOR EACH ROW
BEGIN
INSERT INTO STOCK_HISTORY VALUES(OLD.NAME, OLD.PRICE, OLD.QUANTITY);
end$$
DELIMITER ;