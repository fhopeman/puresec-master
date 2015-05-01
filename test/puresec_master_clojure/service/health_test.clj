(ns puresec-master-clojure.service.health-test
  (:use clojure.test)
  (:require [puresec-master-clojure.service.health :as health-service]
            [clj-http.client :as client]))

(deftest test-get-health
  (testing "that the status of lave is TRUE if slave response = 200"
    (with-redefs [client/get (fn [_] {:status 200})]
      (is (= true
             (health-service/get-health {:url "http://some/url"})))))

  (testing "that the status of lave is FALSE if slave response != 200"
    (with-redefs [client/get (fn [_] {:status 500})]
      (is (= false
             (health-service/get-health {:url "http://some/url"})))))

  (testing "that the status of slave is FALSE if exception occurs"
    (with-redefs [client/get (fn [_] (throw Exception))]
      (is (= false
             (health-service/get-health {:url "http://some/url"}))))))

(deftest test-check-health-and-update-cache
  (testing "that the health of the slaves is checked"
    (with-redefs [health-service/get-health (fn [_] true)]
      (is (= {}
             @health-service/detector-health-cache))
      (health-service/check-health-and-update-cache [{:id 7 :url "http://some/url"}
                                              {:id 9 :url "http://some/url"}]
                                             health-service/detector-health-cache)
      (is (= {7 true
              9 true}
             @health-service/detector-health-cache))
      (reset! health-service/detector-health-cache {}))))
