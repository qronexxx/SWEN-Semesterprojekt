-- CREATE DATABASE-Befehl (nur erforderlich, wenn du nicht bereits in der Datenbank bist)
-- CREATE DATABASE MCTG_DB;

-- Verbinde dich mit der MCTG_DB-Datenbank

--docker run -d --rm --name mctg_db -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -p 5432:5432 -v pgdata:/var/lib/postgresql/data postgres
--docker exec -it mctg_db bash
--psql -U postgres

    --OR
--docker exec -it mctg_db psql -U postgres

-- Tabelle `person` erstellen, wenn sie nicht existiert

CREATE TABLE IF NOT EXISTS users (
    username VARCHAR(50) PRIMARY KEY,
    password VARCHAR(50) NOT NULL,
    coins INT DEFAULT 50,
    elo INT DEFAULT 100,
    is_admin BOOLEAN DEFAULT FALSE
    );


-- Beispiel-Datensatz einfügen
INSERT INTO user (username, password) VALUES ('kienboec', 'daniel');
