(ns puresec-master.service.handler
  (:require [puresec-master.db.core :as db]
            [puresec-master.service.slave :as slave-service]
            [clojure.tools.logging :as log])
  (:import (java.sql BatchUpdateException)))

(def handler-cache (atom nil))

(defn update-handler-cache []
  (swap! handler-cache (fn [_] (slave-service/get-slaves db/load-handlers))))

(defn register-handler [name description url]
  "registers a handler. If a slave with this name is already registered, the existing id is returned"
  (let [response (slave-service/register-slave name description url db/load-handler db/save-handler!)]
    (update-handler-cache)
    response))

(defn remove-handler [id]
  (try
    (let [response (db/delete-handler! {:handler_id id})]
      (update-handler-cache)
      (= 1 response))
  (catch BatchUpdateException e
    (log/error "error while deleting handler" e)
    false)))

(defn get-handlers []
   "loads all handlers"
  (if @handler-cache
    @handler-cache
    (update-handler-cache)))
