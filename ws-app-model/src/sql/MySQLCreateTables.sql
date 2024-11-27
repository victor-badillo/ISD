-- ----------------------------------------------------------------------------
-- Model
-------------------------------------------------------------------------------
DROP TABLE Purchase;
DROP TABLE Game;

------------------------------------ Game ------------------------------------
CREATE TABLE Game ( gameId BIGINT NOT NULL AUTO_INCREMENT,
    visitantName VARCHAR(255) COLLATE latin1_bin NOT NULL,
    celebrationDate DATETIME NOT NULL,
    priceGame DOUBLE NOT NULL,
    ticketMaxCount INT NOT NULL,
    creationDate DATETIME NOT NULL,
    soldTickets INT DEFAULT 0,
    CONSTRAINT GamePK PRIMARY KEY(gameId),
    CONSTRAINT ValidPriceGame CHECK ( priceGame > 0 ),
    CONSTRAINT ValidTicketMaxCount CHECK ( ticketMaxCount > 0) ) ENGINE = InnoDB;

------------------------------------ Purchase ------------------------------------
CREATE TABLE Purchase ( purchaseId BIGINT NOT NULL AUTO_INCREMENT,
    gameId BIGINT NOT NULL,
    userEmail VARCHAR(40) COLLATE latin1_bin NOT NULL,
    creditCardNumber VARCHAR(16),
    units INT DEFAULT 1,
    purchaseDate DATETIME NOT NULL,
    pickedUp BIT DEFAULT 0 NOT NULL,
    CONSTRAINT PurchasePK PRIMARY KEY(purchaseId),
    CONSTRAINT PurchaseGameIdFK FOREIGN KEY(gameId)
            REFERENCES Game(gameId) ON DELETE CASCADE ) ENGINE = InnoDB;
