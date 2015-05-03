# puresec-master

This is the master of a microservice alarm system called puresec. With these several completely independent microservices I
try to create a simple solution of alarm alerting.

## Available Microservices

| Name   |  Description |
|----------|-------------|
| [puresec-master](https://github.com/fhopeman/puresec-master) | Master server to dispatch events |
| [puresec-detect-mock](https://github.com/fhopeman/puresec-detect-mock)  | Mock implementation for test purposes |
| [puresec-handler-mock](https://github.com/fhopeman/puresec-handler-mock) | Mock implementation for test purposes |
| [puresec-handler-mail](https://github.com/msch4/puresec-handler-mail) | Sends a mail on notification events |

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
;; registers a handler
POST    /alarm/register/handler
        {:name "someName" :description "someDescr" :url "http//some/url"}
;; notifies the master that alarm was detected
POST    /alarm/notify
        {:detector_id "idOfDetector"}
```

```
;; maps handler to detector
POST    /admin/notification/map
        {:detector_id "idOfDetector" :handler_id "idOfHandler"}
```

### Detector
```
;; health check
GET     /health
```

### Handler
```
;; health check
GET     /health
;; notify handler to start action
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
