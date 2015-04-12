--name: save-detector-slave!
-- saves a detector slave
INSERT INTO detector
(zone_name, zone_description)
VALUES (:zone_name, :zone_description)

--name: load-detector-slaves
-- loads all detector slaves
SELECT *
FROM detector

--name: load-detector-slave
-- loads a detector slave
SELECT *
FROM detector
WHERE zone_name = :zone_name
