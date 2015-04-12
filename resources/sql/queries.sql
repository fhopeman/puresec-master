--name: register-slave-detect!
-- saves a detection slave
INSERT INTO registry_slave_detect
(zone_name, zone_description)
VALUES (:zone_name, :zone_description)

--name: load-slave-detect
-- loads a detection slave
SELECT *
FROM registry_slave_detect
WHERE zone_name = :zone_name
