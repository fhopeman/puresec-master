(ns puresec-master-clojure.service.settings
  (:require [puresec-master-clojure.db.core :as db]))

(defn map-trigger [detector-id trigger-id]
  ;; dont use detector-id and trigger-id as map keys, there is a bug in the query translation code ..
  (db/save-trigger-mapping! {:detector detector-id :trigger trigger-id}))
