--name: save-detector!
-- saves a detector slave
INSERT INTO detector_registry
(detector_name, detector_description, url)
VALUES (:name, :description, :url)

--name: load-detectors
-- loads all detector slaves
SELECT *
FROM detector_registry

--name: load-detector
-- loads a detector slave
SELECT *
FROM detector_registry
WHERE detector_name = :name

--name: load-detector-by-id
-- loads a detector by id
SELECT *
FROM detector_registry
WHERE id = :detector_id
