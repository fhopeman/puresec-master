--name: save-detector-slave!
-- saves a detector slave
INSERT INTO registry_slave_detect
(zone_name, zone_description)
VALUES (:zone_name, :zone_description)

--name: load-detector-slaves
-- loads all detector slaves
SELECT *
FROM registry_slave_detect

--name: load-detector-slave
-- loads a detector slave
SELECT *
FROM registry_slave_detect
WHERE zone_name = :zone_name
