(ns puresec-master-clojure.service.notification-dispatcher-test
  (:use clojure.test
        puresec-master-clojure.db.core)
  (:require [clj-http.client :refer [post]]
            [puresec-master-clojure.service.notification-dispatcher :as dispatcher]))

(deftest test-dispatch-alarm-notification
  (testing "that the alarm notification is dispatched if alarm is enabled"
    (with-redefs [load-detector-by-id (fn [_] {:id 5 :detector_name "some name 0" :detector_description "some desc 0" :url "http://some/url/0"})
                  load-matching-triggers (fn [_] [{:id 7 :trigger_name "some name 1" :trigger_description "some desc 1" :url "http://some/url/1"}
                                                 {:id 9 :trigger_name "some name 2" :trigger_description "some desc 2" :url "http://some/url/2"}])
                  post (fn [_ _] true)]
      (reset! dispatcher/alarm-enabled true)
      (is (= [7 9]
             (dispatcher/dispatch-alarm-notification 5)))))

  (testing "that the alarm notification isn't dispatched if alarm is disabled"
    (with-redefs [load-detector-by-id (fn [_] {:id 5 :detector_name "some name 0" :detector_description "some desc 0" :url "http://some/url/0"})
                  load-matching-triggers (fn [_] [{:id 7 :trigger_name "some name 1" :trigger_description "some desc 1" :url "http://some/url/1"}
                                                  {:id 9 :trigger_name "some name 2" :trigger_description "some desc 2" :url "http://some/url/2"}])
                  post (fn [_ _] true)]
      (reset! dispatcher/alarm-enabled false)
      (is (= []
             (dispatcher/dispatch-alarm-notification 5))))))

(deftest test-notify-trigger
  (testing "that the notification is sent to a trigger host"
    (with-redefs [post (fn [_ _] true)]
      (is (= 11
             (dispatcher/notify-trigger {:id 11 :trigger_name "some name 0" :trigger_description "some descr 0" :url "http://some/url/0"}
                                        {:id 13 :detector_name "some name 1" :detector_description "some descr 1" :url "http://some/url/1"}))))))

(deftest test-enable-alarm
  (testing "that the alarm can be enabled"
    (is (= false
           @dispatcher/alarm-enabled))
    (dispatcher/enable-alarm)
    (is (= true
           @dispatcher/alarm-enabled))
    (reset! dispatcher/alarm-enabled false)))

(deftest test-disable-alarm
  (testing "that the alarm can be disabled"
    (reset! dispatcher/alarm-enabled true)
    (dispatcher/disable-alarm)
    (is (= false
           @dispatcher/alarm-enabled))))
