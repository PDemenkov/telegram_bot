-- liquibase formatted sql

-- changeset pdemenkov:1

CREATE TABLE Notification_table
(
    id INT PRIMARY KEY ,
    chatID INTEGER,
    messageText VARCHAR(255),
    time TIMESTAMP
);