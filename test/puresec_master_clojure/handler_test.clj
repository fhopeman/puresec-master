(ns puresec-master-clojure.handler_test
  (:use clojure.test
        ring.mock.request
        puresec-master-clojure.handler))

(deftest test-handler
   (testing "that resource route returns static file"
       (let [response (app (request :get "/css/screen.css"))]
         (is (= 200 (:status response))))))
