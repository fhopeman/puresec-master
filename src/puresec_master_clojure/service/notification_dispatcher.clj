(ns puresec-master-clojure.service.notification-dispatcher
  (:require [puresec-master-clojure.db.core :as db]))

(defn notify-trigger [trigger]
  (print "calling trigger ..")
  (:id trigger))

(defn dispatch-alarm-notification [id]
  (let [triggers (db/load-matching-trigger {:id id})]
    (map (fn [trigger] (notify-trigger trigger)) triggers)))
