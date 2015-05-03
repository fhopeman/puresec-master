(ns puresec-master.service.notification-dispatcher-test
  (:use clojure.test
        puresec-master.db.core)
  (:require [clj-http.client :refer [post]]
            [puresec-master.service.notification-dispatcher :as dispatcher]
            [puresec-master.service.settings :as settings]))

(deftest test-dispatch-alarm-notification
  (testing "that the alarm notification is dispatched if alarm is enabled"
    (with-redefs [load-detector-by-id (fn [_] {:id 5 :detector_name "some name 0" :detector_description "some desc 0" :url "http://some/url/0"})
                  load-matching-handlers (fn [_] [{:id 7 :handler_name "some name 1" :handler_description "some desc 1" :url "http://some/url/1"}
                                                 {:id 9 :handler_name "some name 2" :handler_description "some desc 2" :url "http://some/url/2"}])
                  post (fn [_ _] true)
                  settings/is-alarm-enabled (fn [] true)]
      (is (= [7 9]
             (dispatcher/dispatch-alarm-notification 5)))))

  (testing "that the alarm notification isn't dispatched if alarm is disabled"
    (with-redefs [load-detector-by-id (fn [_] {:id 5 :detector_name "some name 0" :detector_description "some desc 0" :url "http://some/url/0"})
                  load-matching-handlers (fn [_] [{:id 7 :handler_name "some name 1" :handler_description "some desc 1" :url "http://some/url/1"}
                                                  {:id 9 :handler_name "some name 2" :handler_description "some desc 2" :url "http://some/url/2"}])
                  post (fn [_ _] true)
                  settings/is-alarm-enabled (fn [] false)]
      (is (= []
             (dispatcher/dispatch-alarm-notification 5))))))

(deftest test-notify-handler
  (testing "that the notification is sent to a handler host"
    (with-redefs [post (fn [_ _] true)]
      (is (= 11
             (dispatcher/notify-handler {:id 11 :handler_name "some name 0" :handler_description "some descr 0" :url "http://some/url/0"}
                                        {:id 13 :detector_name "some name 1" :detector_description "some descr 1" :url "http://some/url/1"}))))))
