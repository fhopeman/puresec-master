--name: save-handler!
-- saves a handler
INSERT INTO handler_registry
(handler_name, handler_description, url)
VALUES (:name, :description, :url)

--name: load-handlers
-- loads all handlers
SELECT *
FROM handler_registry

--name: load-handler
-- loads a handler
SELECT *
FROM handler_registry
WHERE handler_name = :name
