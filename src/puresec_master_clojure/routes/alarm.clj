(ns puresec-master-clojure.routes.alarm
  (:require [puresec-master-clojure.layout :as layout]
            [compojure.core :refer [defroutes GET POST context]]
            [ring.util.response :refer [response redirect content-type]]
            [puresec-master-clojure.service.detector :as detector-service]))

(defn api-register-detector-slave! [request]
  (let [zone_name (:zone_name (:params request))
        zone_description (:zone_description (:params request))]
    (response (detector-service/register-detector-slave zone_name zone_description))))

(defroutes alarm-routes
  (context "/alarm" []
    (GET  "/home" [] (layout/render "home.html" {:detector-slaves (detector-service/get-detector-slaves)}))
    (POST "/register" request (api-register-detector-slave! request))
    ;(POST "/fire" request (api-fire-alarm request))
    ))
