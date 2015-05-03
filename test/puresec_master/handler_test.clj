(ns puresec-master.handler_test
  (:use clojure.test
        ring.mock.request
        puresec-master.handler))

(deftest test-handler
   (testing "that resource route returns static file"
       (let [response (app (request :get "/css/main.css"))]
         (is (= 200 (:status response))))))
