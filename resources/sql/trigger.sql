--name: save-trigger!
-- saves a trigger
INSERT INTO trigger_registry
(trigger_name, trigger_description, url)
VALUES (:name, :description, :url)

--name: load-triggers
-- loads all triggers
SELECT *
FROM trigger_registry

--name: load-trigger
-- loads a trigger
SELECT *
FROM trigger_registry
WHERE trigger_name = :name
