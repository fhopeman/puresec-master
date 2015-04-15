(ns puresec-master-clojure.service.settings
  (:require [puresec-master-clojure.db.core :as db]))

(defn map-trigger [detector-id trigger-id]
  (db/save-trigger-mapping {:detector-id detector-id :trigger-id trigger-id}))
