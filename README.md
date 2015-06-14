# puresec-master

This is the master of a microservice alarm system called puresec. With these several completely independent microservices we try to create a simple solution of alarm alerting.

The microservices only shares code, if the code is an independent and versioned library.

## Microservice Ecosystem Overview

| Name   |  Description |
|----------|-------------|
| [puresec-master](https://github.com/fhopeman/puresec-master) | Master server to dispatch events |
| [puresec-handler-mail](https://github.com/msch4/puresec-handler-mail) | Sends a mail on notification events |
| [puresec-handler-signal](https://github.com/fhopeman/puresec-handler-signal) | Sends signal on notification events |
| [puresec-handler-mock](https://github.com/fhopeman/puresec-handler-mock) | Mock implementation for test purposes |
| [puresec-detect-infrared](https://github.com/fhopeman/puresec-detect-infrared) | Notifies master on movements |
| [puresec-detect-mock](https://github.com/fhopeman/puresec-detect-mock)  | Mock implementation for test purposes |
| [puresec-microservice-js](https://github.com/fhopeman/puresec-microservice-js)  | nodejs macro architecture definition library |

## Prerequisites

To setup your development system, you can use the `bin/setupLinuxDev.sh` script. It will install leiningen, mysql and other dependencies which you need to start up the master service.

## Running

To start the application, run:

`lein ring server` or `lein ring server-headless`

## Deployment

To deploy the application on your raspberry pi, you can use the `bin/setupServer.sh` script for the initial setup (run it on the raspberry pi). The deployment itself can be done with the `bin/deployServer` script from your development machine.

## API Description
This section describes the API which you need for the communication between the master and other microservices.

### Master API
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

### Detector API
```
;; health check
GET     /health
```

### Handler API
```
;; health check
GET     /health
;; notify handler to start action
POST    /notify
        {:detector_name "someName" :detector_description "someDescr"}
```

### Admin Console (not implemented yet)
```
;; health check
GET     /health
```

## Contribution
Feel free to contribute!
