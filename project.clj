(defproject puresec-master "0.1.0-SNAPSHOT"

  :description "puresec master"
  :url "https://github.com/fhopeman/puresec-master"

  :dependencies [[org.clojure/clojure "1.6.0"]
                 [ring-server "0.4.0"]
                 [selmer "0.8.2"]
                 [com.taoensso/timbre "3.4.0"]
                 [com.taoensso/tower "3.0.2"]
                 [markdown-clj "0.9.65"]
                 [environ "1.0.0"]
                 [im.chit/cronj "1.4.3"]
                 [compojure "1.3.3"]
                 [ring/ring-defaults "0.1.4"]
                 [ring/ring-session-timeout "0.1.0"]
                 [ring-middleware-format "0.5.0"]
                 [noir-exception "0.2.3"]
                 [bouncer "0.3.2"]
                 [prone "0.8.1"]
                 [ragtime "0.3.8"]
                 [yesql "0.5.0-rc1"]
                 [mysql/mysql-connector-java "5.1.6"]
                 [clj-http "1.0.1"]
                 [schejulure "1.0.1"]
                 [org.clojure/tools.logging "0.3.1"]
                 [org.slf4j/slf4j-log4j12 "1.7.1"]
                 [log4j/log4j "1.2.17" :exclusions [javax.mail/mail
                                                    javax.jms/jms
                                                    com.sun.jmdk/jmxtools
                                                    com.sun.jmx/jmxri]]]

  :min-lein-version "2.5.1"
  :uberjar-name "puresec-master.jar"
  :repl-options {:init-ns puresec-master.handler}
  :jvm-opts ["-server"]

  :main puresec-master.core

  :plugins [[lein-ring "0.9.1"]
            [lein-environ "1.0.0"]
            [lein-ancient "0.6.5"]
            [ragtime/ragtime.lein "0.3.8"]]
  
  :ring {:handler puresec-master.handler/app
         :init    puresec-master.handler/init
         :destroy puresec-master.handler/destroy
         :uberwar-name "puresec-master.war"}
  
  :ragtime
  {:migrations ragtime.sql.files/migrations
   :database
   "jdbc:mysql://localhost:3306/puresec_master?user=psec_master&password=psec_master"}

  :profiles
  {:uberjar {:omit-source true
             :env {:prod false
                   :dev false}
             
             :aot :all}
   :dev {:dependencies [[ring-mock "0.1.5"]
                        [ring/ring-devel "1.3.2"]
                        [pjstadig/humane-test-output "0.7.0"]
                        ]
         :source-paths ["env/dev/clj"]
         
         
         
         :repl-options {:init-ns puresec-master.repl}
         :injections [(require 'pjstadig.humane-test-output)
                      (pjstadig.humane-test-output/activate!)]
         :env {:dev true}}})
