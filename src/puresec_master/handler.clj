(ns puresec-master.handler
  (:require [compojure.core :refer [defroutes routes]]
            [puresec-master.routes.home :refer [home-routes]]
            [puresec-master.routes.alarm :refer [alarm-routes]]
            [puresec-master.routes.admin :refer [admin-routes]]
            [puresec-master.middleware :refer [development-middleware production-middleware]]
            [puresec-master.service.health :as health-service]
            [schejulure.core :as cron]
            [puresec-master.session :as session]
            [compojure.route :as route]
            [taoensso.timbre :as timbre]
            [taoensso.timbre.appenders.rotor :as rotor]
            [selmer.parser :as parser]
            [environ.core :refer [env]]
            [cronj.core :as cronj]))

(defroutes base-routes
  (route/resources "/")
  (route/not-found "Not Found"))

(defn init
  "init will be called once when
   app is deployed as a servlet on
   an app server.
   Put any initialization code here"
  []
  (timbre/set-config!
    [:appenders :rotor]
    {:min-level :info
     :enabled? true
     :async? false ; should be always false for rotor
     :max-message-per-msecs nil
     :fn rotor/appender-fn})

  (timbre/set-config!
    [:shared-appender-config :rotor]
    {:path "puresec_master.log" :max-size (* 512 1024) :backlog 10})

  (if (env :dev) (parser/cache-off!))
  ;;start the expired session cleanup job
  (cronj/start! session/cleanup-job)
  (timbre/info "\n-=[ puresec-master started successfully"
               (when (env :dev) "using the development profile") "]=-")

  ;; start detector/handler health check loop
  (cron/schedule {:minute (range 0 60 1)} health-service/check-health))

(defn destroy
  "destroy will be called when your application
   shuts down, put any clean up code here"
  []
  (timbre/info "puresec-master is shutting down...")
  (cronj/shutdown! session/cleanup-job)
  (timbre/info "shutdown complete!"))

(def app
  (-> (routes
        home-routes
        alarm-routes
        admin-routes
        base-routes)
      development-middleware
      production-middleware))
