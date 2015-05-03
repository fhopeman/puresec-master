--name: save-handler-mapping!
-- saves a detector/handler association
INSERT INTO detector_handler
(detector_id, handler_id)
VALUES (:detector_id, :handler_id)

--name: remove-handler-mapping!
-- deletes a detector-handler association
DELETE FROM detector_handler
WHERE detector_id = :detector_id AND handler_id = :handler_id

--name: load-matching-handlers
-- loads handlers which are associated to specified detector
SELECT t.id, t.handler_name, t.handler_description, t.url
FROM detector_handler AS dt JOIN handler_registry AS t ON (dt.handler_id = t.id)
WHERE dt.detector_id = :detector_id

--name: load-handler-mapping
-- loads a mapping of detector to handler
SELECT *
FROM detector_handler
WHERE detector_id = :detector_id AND handler_id = :handler_id
