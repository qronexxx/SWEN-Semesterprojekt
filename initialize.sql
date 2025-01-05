-- CREATE DATABASE-Befehl (nur erforderlich, wenn du nicht bereits in der Datenbank bist)
-- CREATE DATABASE mtcg_db;

-- Verbinde dich mit der mtcg_db-Datenbank

--docker run -d --rm --name mtcg_db-e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -p 5432:5432 -v pgdata:/var/lib/postgresql/data postgres
--docker exec -it mtcg_db bash
--psql -U postgres

    --OR
--docker exec -it mtcg_db psql -U postgres

-- Tabelle `users` erstellen, wenn sie nicht existiert

-- to quick delete to use the sh script: delete from userdecks; delete from userstacks; delete from packages; delete from cards; delete from users;


CREATE TABLE IF NOT EXISTS users (
    Username VARCHAR(50) PRIMARY KEY,
    Password VARCHAR(50) NOT NULL,
    Name VARCHAR(50),
    Coins INT DEFAULT 20,
    Elo INT DEFAULT 100,
    Wins INT DEFAULT 0,
    Losses INT DEFAULT 0,
    Bio VARCHAR(100),
    Image VARCHAR(50)
    );

CREATE TABLE IF NOT EXISTS Cards (
    CardID UUID PRIMARY KEY,
    Name VARCHAR(100) NOT NULL,
    Damage DECIMAL(5,2) NOT NULL
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

CREATE TABLE IF NOT EXISTS UserDecks (
    Username VARCHAR(50) REFERENCES Users(Username),
    CardPosition INT,
    CardID UUID REFERENCES Cards(CardID),
    PRIMARY KEY (Username, CardPosition)
    );

-- Beispiel-Datensatz einfügen
INSERT INTO user (username, password) VALUES ('test', 'test');
