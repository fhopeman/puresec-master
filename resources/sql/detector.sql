--name: save-detector!
-- saves a detector slave
INSERT INTO detector_registry
(detector_name, detector_description)
VALUES (:name, :description)

--name: load-detectors
-- loads all detector slaves
SELECT *
FROM detector_registry

--name: load-detector
-- loads a detector slave
SELECT *
FROM detector_registry
WHERE detector_name = :name
