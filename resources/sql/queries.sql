--name: save-detection-slave!
-- saves a detection slave
INSERT INTO registry_slave_detect
(zone_name, zone_description)
VALUES (:zone_name, :zone_description)

--name: load-detection-slaves
-- loads all detection slaves
SELECT *
FROM registry_slave_detect

--name: load-detection-slave
-- loads a detection slave
SELECT *
FROM registry_slave_detect
WHERE zone_name = :zone_name
