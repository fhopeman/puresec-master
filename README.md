# puresec-master-clojure

FIXME

## Prerequisites

You will need [Leiningen][1] 2.0 or above installed.

[1]: https://github.com/technomancy/leiningen

## Running

To start a web server for the application, run:

    lein ring server

## API
### Master
```
GET     /alarm/home                 current alarm state and further information
POST    /alarm/register/detector    registers a detector
POST    /alarm/register/trigger     registers a trigger
POST    /alarm/notify               notify the master that alarm was detected
```

```
POST    /admin/trigger/map          maps trigger to detector
```

### Detector
```
GET     /detector/health            health check
```

### Trigger
```
GET     /trigger/health             health check
```

### Admin Console
```
GET     /console/health             health check
```

## Planned features
- state checking of slaves
- configuration of slaves (delete, ..)
- slaves which fires an alarm
- logging
