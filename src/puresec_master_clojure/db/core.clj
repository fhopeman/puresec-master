(ns puresec-master-clojure.db.core
  (:require
    [yesql.core :refer [defqueries]]))

(def db-spec
  {:subprotocol "mysql"
   :subname "//localhost:3306/puresec_master_clojure"
   :user "psec_master_cl"
   :password "psec_master_cl"})

(defqueries "sql/queries.sql" {:connection db-spec})