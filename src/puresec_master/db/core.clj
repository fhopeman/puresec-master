(ns puresec-master.db.core
  (:require
    [yesql.core :refer [defqueries]]))

(def db-spec
  {:subprotocol "mysql"
   :subname "//localhost:3306/puresec_master"
   :user "psec_master"
   :password "psec_master"})

(defqueries "sql/detector.sql" {:connection db-spec})
(defqueries "sql/trigger.sql" {:connection db-spec})
(defqueries "sql/detector_trigger.sql" {:connection db-spec})
