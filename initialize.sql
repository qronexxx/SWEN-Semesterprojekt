-- CREATE DATABASE-Befehl (nur erforderlich, wenn du nicht bereits in der Datenbank bist)
-- CREATE DATABASE MCTG_DB;

-- Verbinde dich mit der MCTG_DB-Datenbank

--docker run -d --rm --name mctg_db -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -p 5432:5432 -v pgdata:/var/lib/postgresql/data postgres
--docker exec -it mctg_db bash
--psql -U postgres

    --OR
--docker exec -it mctg_db psql -U postgres

-- Tabelle `person` erstellen, wenn sie nicht existiert

CREATE TABLE IF NOT EXISTS person (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(50) NOT NULL
    );

-- Beispiel-Datensatz einfügen
INSERT INTO person (username, password) VALUES ('kienboec', 'daniel');
