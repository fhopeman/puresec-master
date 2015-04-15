CREATE TABLE detector_trigger
(detector_id INTEGER,
 trigger_id INTEGER,
 FOREIGN KEY (detector_id) REFERENCES detector_registry(id),
 FOREIGN KEY (trigger_id) REFERENCES trigger_registry(id),
 CONSTRAINT pk_detector_trigger PRIMARY KEY (detector_id, trigger_id));
