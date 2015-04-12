(ns puresec-master-clojure.routes.alarm_test
  (:use clojure.test
        ring.mock.request
        puresec-master-clojure.handler
        puresec-master-clojure.routes.alarm
        puresec-master-clojure.service.detector))

(deftest test-alarm-home-page
  (testing "that request to alarm home page returns 200"
    (let [response (app (request :get "/alarm/home"))]
      (is (= 200 (:status response))))))

(deftest test-api-register-detector-slave
  (testing "that call to the detector slave registration api works"
    (with-redefs [register-detector-slave (fn [_ _] true)]
      (let [response (app (request :post "/alarm/register"))]
        (is (= 200 (:status response)))
        (is (= "application/json; charset=utf-8" (get (:headers response) "Content-Type")))))))
