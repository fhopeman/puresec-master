(ns puresec-master.service.settings-test
  (:use clojure.test
        puresec-master.db.core)
  (:require [puresec-master.service.settings :as settings]))

(deftest test-enable-alarm
  (testing "that the alarm can be enabled"
    (is (= false
           (settings/is-alarm-enabled)))
    (settings/enable-alarm)
    (is (= true
           (settings/is-alarm-enabled)))
    (reset! settings/alarm-enabled false)))

(deftest test-disable-alarm
  (testing "that the alarm can be disabled"
    (reset! settings/alarm-enabled true)
    (settings/disable-alarm)
    (is (= false
           (settings/is-alarm-enabled)))))

(deftest test-map-handler
  (testing "that the handler is mapped to a detector"
    (with-redefs [load-handler-mapping (fn [_] [])
                  save-handler-mapping! (fn [_] 1)]
      (is (= true
             (settings/map-handler 7 9))))
    (reset! settings/handler-mapping-cache nil))

  (testing "that the handler isn't mapped to a detector if mapping already exists"
    (with-redefs [load-handler-mapping (fn [_] [{:detector_id 5 :handler_id 7}])
                  save-handler-mapping! (fn [_] 1)]
      (is (= false
             (settings/map-handler 5 7))))
    (reset! settings/handler-mapping-cache nil)))

(deftest test-unmap-handler
  (testing "that the handler is unmapped from the detector"
    (with-redefs [remove-handler-mapping! (fn [_] 1)]
      (is (= true
             (settings/unmap-handler 11 13))))
    (reset! settings/handler-mapping-cache nil))

  (testing "that the ummap fn returns false if mapping doesn't exist"
    (with-redefs [remove-handler-mapping! (fn [_] 0)]
      (is (= false
             (settings/unmap-handler 15 17))))
    (reset! settings/handler-mapping-cache nil)))

(deftest test-get-handler-mapping
  (testing "that correct handler mapping is calculated"
    (with-redefs [load-detectors (fn [] [{:id 3 :detector_name "someName1" :detector_description "someDesc1" :url "http://some/url/1"}
                                         {:id 5 :detector_name "someName2" :detector_description "someDesc2" :url "http://some/url/2"}])
                  load-handlers (fn [] [{:id 7 :handler_name "someName3" :handler_description "someDesc3" :url "http://some/url/3"}
                                        {:id 9 :handler_name "someName4" :handler_description "someDesc4" :url "http://some/url/4"}])
                  load-matching-handlers (fn [_] [{:id 7 :handler_name "someName3" :handler_description "someDesc3" :url "http://some/url/3"}])]
      (is (= [{:id 3 :detector_name "someName1" :detector_description "someDesc1" :url "http://some/url/1"
               :handlers [{:id 7 :handler_name "someName3" :handler_description "someDesc3" :url "http://some/url/3" :mapped true}
                          {:id 9 :handler_name "someName4" :handler_description "someDesc4" :url "http://some/url/4" :mapped false}]}
              {:id 5 :detector_name "someName2" :detector_description "someDesc2" :url "http://some/url/2"
               :handlers [{:id 7 :handler_name "someName3" :handler_description "someDesc3" :url "http://some/url/3" :mapped true}
                         {:id 9 :handler_name "someName4" :handler_description "someDesc4" :url "http://some/url/4" :mapped false}]}]
             (settings/get-handler-mapping))))
    (reset! settings/handler-mapping-cache nil))

  (testing "that correct handler mapping is calculated if no handler is mapped to detector"
    (with-redefs [load-detectors (fn [] [{:id 3 :detector_name "someName1" :detector_description "someDesc1" :url "http://some/url/1"}])
                  load-handlers (fn [] [{:id 7 :handler_name "someName3" :handler_description "someDesc3" :url "http://some/url/3"}])
                  load-matching-handlers (fn [_] [])]
      (is (= [{:id 3 :detector_name "someName1" :detector_description "someDesc1" :url "http://some/url/1"
               :handlers [{:id 7 :handler_name "someName3" :handler_description "someDesc3" :url "http://some/url/3" :mapped false}]}]
             (settings/get-handler-mapping))))
    (reset! settings/handler-mapping-cache nil)))
