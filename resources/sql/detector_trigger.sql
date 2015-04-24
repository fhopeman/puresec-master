--name: save-trigger-mapping!
-- saves a detector/trigger association
INSERT INTO detector_trigger
(detector_id, trigger_id)
VALUES (:detector_id, :trigger_id)

--name: remove-trigger-mapping!
-- deletes a detector-trigger association
DELETE FROM detector_trigger
WHERE detector_id = :detector_id AND trigger_id = :trigger_id

--name: load-matching-triggers
-- loads triggers which are associated to specified detector
SELECT t.id, t.trigger_name, t.trigger_description, t.url
FROM detector_trigger AS dt JOIN trigger_registry AS t ON (dt.trigger_id = t.id)
WHERE dt.detector_id = :detector_id

--name: load-trigger-mapping
-- loads a mapping of detector to trigger
SELECT *
FROM detector_trigger
WHERE detector_id = :detector_id AND trigger_id = :trigger_id
