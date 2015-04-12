(ns puresec-master-clojure.routes.alarm
  (:require [puresec-master-clojure.layout :as layout]
            [compojure.core :refer [defroutes GET POST context]]
            [ring.util.response :refer [response redirect content-type]]
            [puresec-master-clojure.service.detector :as detector-service]
            [puresec-master-clojure.service.trigger :as trigger-service]))

(defn api-register-detector! [request]
  (let [name (:name (:params request))
        description (:description (:params request))]
    (response (detector-service/register-detector name description))))

(defn api-register-trigger! [request]
  (let [name (:name (:params request))
        description (:description (:params request))]
    (response (trigger-service/register-trigger name description))))

(defroutes alarm-routes
  (context "/alarm" []
    (GET  "/home" [] (layout/render "home.html" {:detectors (detector-service/get-detectors)}))
    (POST "/register/detector" request (api-register-detector! request))
    (POST "/register/trigger" request (api-register-trigger! request))
    ;(POST "/trigger" request (api-fire-alarm request))
    ))
