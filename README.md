# puresec-master-clojure

This is the master of a microservice alarm system called puresec. With these several completely independent microservices I
try to create a simple solution of alarm alerting.

## Prerequisites

You will need [Leiningen](https://github.com/technomancy/leiningen) 2.0 or above installed.

## Running

To start a web server for the application, run:

    `lein ring server` or `lein ring server-headless`

## API
### Master
```
;; current alarm state and further information
GET     /alarm/home
;; registers a detector
POST    /alarm/register/detector
        {:name "someName" :description "someDescr" :url "http//some/url"}
;; registers a trigger
POST    /alarm/register/trigger
        {:name "someName" :description "someDescr" :url "http//some/url"}
;; notifies the master that alarm was detected
POST    /alarm/notify
        {:detector_id "idOfDetector"}
```

```
;; maps trigger to detector
POST    /admin/notification/map
        {:detector_id "idOfDetector" :trigger_id "idOfTrigger"}
```

### Detector
```
;; health check
GET     /health
```

### Trigger
```
;; health check
GET     /health
;; notify trigger to start action
POST    /notify
        {:detector_name "someName" :detector_description "someDescr"}
```

### Admin Console
```
;; health check
GET     /health
```

## Planned features
- state checking of slaves
- configuration of slaves (delete, ..)
- slaves which fires an alarm
- logging
