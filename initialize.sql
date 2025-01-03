-- CREATE DATABASE-Befehl (nur erforderlich, wenn du nicht bereits in der Datenbank bist)
-- CREATE DATABASE MCTG_DB;

-- Verbinde dich mit der MCTG_DB-Datenbank

--docker run -d --rm --name mctg_db -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -p 5432:5432 -v pgdata:/var/lib/postgresql/data postgres
--docker exec -it mctg_db bash
--psql -U postgres

    --OR
--docker exec -it mctg_db psql -U postgres

-- Tabelle `users` erstellen, wenn sie nicht existiert

-- to quick delete to use the sh script:         delete from packages; delete from cards; delete from users;

CREATE TABLE IF NOT EXISTS users (
    Username VARCHAR(50) PRIMARY KEY,
    Password VARCHAR(50) NOT NULL,
    Name VARCHAR(50),
    Coins INT DEFAULT 50,
    Elo INT DEFAULT 100,
    Wins INT DEFAULT 0,
    Losses INT DEFAULT 0,
    Bio VARCHAR(100),
    Image VARCHAR(50)
    )

CREATE TABLE IF NOT EXISTS Cards (
    CardID UUID PRIMARY KEY,
    Name VARCHAR(100) NOT NULL,
    Damage DECIMAL(5,2) NOT NULL,
);

CREATE TABLE IF NOT EXISTS Packages (
    PackageID SERIAL PRIMARY KEY,
    Card1 UUID REFERENCES Cards (CardID),
    Card2 UUID REFERENCES Cards (CardID),
    Card3 UUID REFERENCES Cards (CardID),
    Card4 UUID REFERENCES Cards (CardID),
    Card5 UUID REFERENCES Cards (CardID)
);

CREATE TABLE IF NOT EXISTS UserStacks (
    Username VARCHAR(50) REFERENCES Users(Username),
    CardID UUID REFERENCES Cards(CardID),
    PRIMARY KEY (Username, CardID)
);

CREATE TABLE IF NOT EXISTS Decks (
    Username VARCHAR(50) REFERENCES Users(Username) PRIMARY KEY,
    Card1 UUID REFERENCES Cards (CardID),
    Card2 UUID REFERENCES Cards (CardID),
    Card3 UUID REFERENCES Cards (CardID),
    Card4 UUID REFERENCES Cards (CardID)
);


CREATE TABLE IF NOT EXISTS Transactions (
    TransactionID SERIAL PRIMARY KEY,
    Username VARCHAR(50) REFERENCES Users (Username),
    PackageID INT REFERENCES Packages (PackageID),
    Timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);



-- Beispiel-Datensatz einfügen
INSERT INTO user (username, password) VALUES ('test', 'test');
