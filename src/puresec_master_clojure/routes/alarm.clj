(ns puresec-master-clojure.routes.alarm
  (:require [puresec-master-clojure.layout :as layout]
            [compojure.core :refer [defroutes GET context]]
            [puresec-master-clojure.db.core :as db]
            [ring.util.response :refer [redirect]]))

;(defn home-page []
;  (layout/render
;    "home.html" {:docs (-> "docs/docs.md" io/resource slurp)}))

(defn register-slave-detect! []
    (db/register-slave-detect!)
    (redirect "/"))

(defn page-alarm []
  (layout/render "alarm.html"))

(defroutes alarm-routes
  (context "/alarm" []
    (GET "/status" [] (page-alarm))
    (GET "/register" [] (register-slave-detect!))))
