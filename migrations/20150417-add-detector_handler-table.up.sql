CREATE TABLE detector_handler
(detector_id INTEGER,
 handler_id INTEGER,
 FOREIGN KEY (detector_id) REFERENCES detector_registry(id),
 FOREIGN KEY (handler_id) REFERENCES handler_registry(id),
 CONSTRAINT pk_detector_handler PRIMARY KEY (detector_id, handler_id));
