(ns puresec-master-clojure.routes.alarm
  (:require [puresec-master-clojure.layout :as layout]
            [compojure.core :refer [defroutes GET POST context]]
            [puresec-master-clojure.db.core :as db]
            [ring.util.response :refer [response redirect content-type]]))

; ring.util.response/content-type
;(defn home-page []
;  (layout/render
;    "home.html" {:docs (-> "docs/docs.md" io/resource slurp)}))

(defn register-slave-detect! [zone_name zone_description]
  ; (db/register-slave-detect!)
  (response {:id zone_name
             :descr zone_description}))

(defn page-alarm []
  (layout/render "alarm.html"))

(defroutes alarm-routes
  (context "/alarm" []
    (GET "/status" [] (page-alarm))
    (POST "/register" req
      (let [zone_name (:zone_name (:params req))
            zone_description (:zone_description (:params req))]
        (register-slave-detect! zone_name zone_description)))))
