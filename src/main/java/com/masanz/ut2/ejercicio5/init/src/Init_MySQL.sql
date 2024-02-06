USE app_server;

CREATE TABLE IF NOT EXISTS Users_(
	id INTEGER AUTO_INCREMENT NOT NULL PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    user_ VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL,
    password_ VARCHAR(255) NOT NULL,
    creation_date DATETIME NOT NULL,
    modification_date DATETIME DEFAULT NULL,
    balance DECIMAL(65, 2) NOT NULL DEFAULT 0.0
);

CREATE TABLE IF NOT EXISTS Products(
	id INTEGER AUTO_INCREMENT PRIMARY KEY,
    name_ VARCHAR(255) NOT NULL,
    value_ DECIMAL(65, 2) NOT NULL DEFAULT 0.0,
    userID INTEGER
);

CREATE TABLE IF NOT EXISTS Orders(
	id INTEGER AUTO_INCREMENT PRIMARY KEY,
    objectID INTEGER NOT NULL,
    userC INTEGER NOT NULL,
    userV INTEGER NOT NULL,
    date_order DATETIME DEFAULT NULL
);

ALTER TABLE Products ADD CONSTRAINT user_id_ref FOREIGN KEY (userID) REFERENCES Users_(id);

ALTER TABLE Orders ADD CONSTRAINT product_id_ref FOREIGN KEY (objectID) REFERENCES Products(id);
ALTER TABLE Orders ADD CONSTRAINT userC_id_ref FOREIGN KEY (userC) REFERENCES Users_(id);
ALTER TABLE Orders ADD CONSTRAINT userV_id_ref FOREIGN KEY (userV) REFERENCES Users_(id);

DROP TRIGGER IF EXISTS check_duplicate_users;
DROP TRIGGER IF EXISTS add_on_update_now;
DROP TRIGGER IF EXISTS add_date_order;
DROP FUNCTION IF EXISTS create_order;

/* Trigger if user is duplicate + add creation date */
DELIMITER %%
CREATE TRIGGER check_duplicate_users BEFORE INSERT ON Users_ FOR EACH ROW
BEGIN
    IF ((SELECT COUNT(*) FROM Users_ U WHERE U.user_ = NEW.user_) != 0) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = "Usuario duplicado";
    END IF;

    IF(NEW.creation_date IS NULL) THEN
		SET NEW.creation_date = CURRENT_TIME;
    END IF;
END%%
DELIMITER ;

/* Trigger when user is added */
DELIMITER %%
CREATE TRIGGER add_on_update_now BEFORE UPDATE ON Users_ FOR EACH ROW
BEGIN
    SET NEW.modification_date = CURRENT_TIME;
END%%
DELIMITER ;

/* Trigger add creation order */
DELIMITER %%
CREATE TRIGGER add_date_order BEFORE INSERT ON Orders FOR EACH ROW
BEGIN
    IF(NEW.date_order IS NULL) THEN
		SET NEW.date_order = CURRENT_TIME;
    END IF;
END%%
DELIMITER ;

/* Create function for order creations */
DELIMITER %%
CREATE FUNCTION create_order(product_id INTEGER UNSIGNED, user_buyer INTEGER UNSIGNED, user_seller INTEGER UNSIGNED) RETURNS BOOLEAN DETERMINISTIC
BEGIN    
    DECLARE balance_buyer DECIMAL(65, 2);
    DECLARE price_product DECIMAL(65, 2);
	DECLARE balance_seller DECIMAL(65, 2);
    
	IF((SELECT EXISTS (SELECT id FROM Users_ WHERE id = user_buyer)) = 0 OR (SELECT EXISTS (SELECT id FROM Users_ WHERE id = user_seller)) = 0 OR (SELECT EXISTS (SELECT id FROM Products WHERE id = product_id)) = 0) THEN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = "Uno o varios de los parámetros no son válidos";
    END IF;
    
    SELECT balance INTO balance_buyer FROM Users_ WHERE id = user_buyer;
    SELECT value_ INTO price_product FROM Products WHERE id = product_id;
	SELECT balance INTO balance_seller FROM Users_ WHERE id = user_seller;
    
	IF (price_product > balance_buyer) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = "Balance insuficiente";
    END IF;
    
    IF((SELECT userID FROM Products WHERE id = product_id) = user_buyer) THEN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = "El comprador no puede ser igual al propietario";
	END IF;
    
	INSERT INTO Orders(objectID, userC, userV) VALUES(product_id, user_buyer, user_seller);
	UPDATE Products SET userID = user_buyer WHERE id = product_id;
	UPDATE Users_ SET balance = (balance_buyer - price_product) WHERE id = user_buyer;
	UPDATE Users_ SET balance = (balance_seller + price_product) WHERE id = user_seller;

    RETURN TRUE;
END%%
DELIMITER ;