CREATE TABLE handler_registry
(id INTEGER PRIMARY KEY AUTO_INCREMENT,
handler_name VARCHAR(30) NOT NULL UNIQUE,
handler_description VARCHAR(256) NOT NULL,
 url VARCHAR(256) NOT NULL);
