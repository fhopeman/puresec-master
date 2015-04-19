(ns puresec-master-clojure.service.notification-dispatcher-test
  (:use clojure.test
        puresec-master-clojure.service.notification-dispatcher
        puresec-master-clojure.db.core)
  (:require [clj-http.client :refer [post]]))

(deftest test-dispatch-alarm-notification
  (testing "that the alarm notification is dispatched to the triggers"
    (with-redefs [load-detector-by-id (fn [_] {:id 5 :detector_name "some name 0" :detector_description "some desc 0" :url "http://some/url/0"})
                  load-matching-trigger (fn [_] [{:id 7 :trigger_name "some name 1" :trigger_description "some desc 1" :url "http://some/url/1"}
                                                 {:id 9 :trigger_name "some name 2" :trigger_description "some desc 2" :url "http://some/url/2"}])
                  post (fn [_ _] true)]
      (is (= [7 9]
             (dispatch-alarm-notification 5))))))

(deftest test-notify-trigger
  (testing "that the notification is sent to a trigger host"
    (with-redefs [post (fn [_ _] true)]
      (is (= 11
             (notify-trigger {:id 11 :trigger_name "some name 0" :trigger_description "some descr 0" :url "http://some/url/0"}
                             {:id 13 :detector_name "some name 1" :detector_description "some descr 1" :url "http://some/url/1"}))))))
