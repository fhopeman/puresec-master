(ns puresec-master-clojure.routes.home
  (:require [compojure.core :refer [defroutes GET]]
            [ring.util.response :refer [redirect]]))

(defn page-home []
  (redirect "/alarm/status"))

(defroutes home-routes
    (GET "/" [] (page-home)))
