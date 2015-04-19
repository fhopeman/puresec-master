(ns puresec-master-clojure.service.trigger
  (:require [puresec-master-clojure.db.core :as db]
            [puresec-master-clojure.service.slave :as slave-service]))

(defn register-trigger [name description url]
  "registers a trigger. If a slave with this name is already registered, the existing id is returned"
  (slave-service/register-slave name description url db/load-trigger db/save-trigger!))

(defn get-triggers []
   "loads all triggers"
  (slave-service/get-slaves db/load-triggers))
