CREATE TABLE detector_registry
(id INTEGER PRIMARY KEY AUTO_INCREMENT,
 detector_name VARCHAR(30) NOT NULL UNIQUE,
 detector_description VARCHAR(256) NOT NULL);