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

(deftest test-map-trigger
  (testing "that the trigger is mapped to a detector"
    (with-redefs [load-trigger-mapping (fn [_] [])
                  save-trigger-mapping! (fn [_] 1)]
      (is (= true
             (settings/map-trigger 7 9)))))

  (testing "that the trigger isn't mapped to a detector if mapping already exists"
    (with-redefs [load-trigger-mapping (fn [_] [{:detector_id 5 :trigger_id 7}])
                  save-trigger-mapping! (fn [_] 1)]
      (is (= false
             (settings/map-trigger 5 7))))))

(deftest test-unmap-trigger
  (testing "that the trigger is unmapped from the detector"
    (with-redefs [remove-trigger-mapping! (fn [_] 1)]
      (is (= true
             (settings/unmap-trigger 11 13)))))

  (testing "that the ummap fn returns false if mapping doesn't exist"
    (with-redefs [remove-trigger-mapping! (fn [_] 0)]
      (is (= false
             (settings/unmap-trigger 15 17))))))

(deftest test-get-trigger-mapping
  (testing "that correct trigger mapping is calculated"
    (with-redefs [load-detectors (fn [] [{:id 3 :detector_name "someName1" :detector_description "someDesc1" :url "http://some/url/1"}
                                         {:id 5 :detector_name "someName2" :detector_description "someDesc2" :url "http://some/url/2"}])
                  load-triggers (fn [] [{:id 7 :trigger_name "someName3" :trigger_description "someDesc3" :url "http://some/url/3"}
                                        {:id 9 :trigger_name "someName4" :trigger_description "someDesc4" :url "http://some/url/4"}])
                  load-matching-triggers (fn [_] [{:id 7 :trigger_name "someName3" :trigger_description "someDesc3" :url "http://some/url/3"}])]
      (is (= [{:id 3 :detector_name "someName1" :detector_description "someDesc1" :url "http://some/url/1"
               :triggers [{:id 7 :trigger_name "someName3" :trigger_description "someDesc3" :url "http://some/url/3" :mapped true}
                          {:id 9 :trigger_name "someName4" :trigger_description "someDesc4" :url "http://some/url/4" :mapped false}]}
              {:id 5 :detector_name "someName2" :detector_description "someDesc2" :url "http://some/url/2"
               :triggers [{:id 7 :trigger_name "someName3" :trigger_description "someDesc3" :url "http://some/url/3" :mapped true}
                         {:id 9 :trigger_name "someName4" :trigger_description "someDesc4" :url "http://some/url/4" :mapped false}]}]
             (settings/get-trigger-mapping)))))

  (testing "that correct trigger mapping is calculated if no trigger is mapped to detector"
    (with-redefs [load-detectors (fn [] [{:id 3 :detector_name "someName1" :detector_description "someDesc1" :url "http://some/url/1"}])
                  load-triggers (fn [] [{:id 7 :trigger_name "someName3" :trigger_description "someDesc3" :url "http://some/url/3"}])
                  load-matching-triggers (fn [_] [])]
      (is (= [{:id 3 :detector_name "someName1" :detector_description "someDesc1" :url "http://some/url/1"
               :triggers [{:id 7 :trigger_name "someName3" :trigger_description "someDesc3" :url "http://some/url/3" :mapped false}]}]
             (settings/get-trigger-mapping))))))
