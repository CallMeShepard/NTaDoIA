-- если база данных myGame существует, использовать ее и удалить таблицу users, затем удалить саму базу данных
DROP DATABASE IF EXISTS myGame;

CREATE DATABASE myGame;
USE myGame;

CREATE TABLE users (
    id INT NOT NULL AUTO_INCREMENT,
    username VARCHAR(30) NOT NULL,
    password VARCHAR(64) NOT NULL,
    PRIMARY KEY(id)
);

INSERT INTO users (username, password) VALUES ('Player1', 'Pass1');
INSERT INTO users (username, password) VALUES ('Player2', 'Pass2');