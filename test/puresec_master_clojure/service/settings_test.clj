(ns puresec-master-clojure.service.settings-test
  (:use clojure.test
        puresec-master-clojure.db.core
        puresec-master-clojure.service.settings))

(deftest test-map-trigger
  (testing "that the trigger is mapped to a detector"
    (with-redefs [load-trigger-mapping (fn [_] [])
                  save-trigger-mapping! (fn [_] 1)]
      (is (= true
             (map-trigger 7 9)))))

  (testing "that the trigger isn't mapped to a detector if mapping already exists"
    (with-redefs [load-trigger-mapping (fn [_] [{:detector_id 5 :trigger_id 7}])
                  save-trigger-mapping! (fn [_] 1)]
      (is (= false
             (map-trigger 5 7))))))

(deftest test-get-trigger-mapping
  (testing "that correct trigger mapping is calculated"
    (with-redefs [load-detectors (fn [] [{:id 3 :detector_name "someName1" :detector_description "someDesc1" :url "http://some/url/1"}
                                         {:id 5 :detector_name "someName2" :detector_description "someDesc2" :url "http://some/url/2"}])
                  load-matching-triggers (fn [_] [{:id 7 :trigger_name "someName3" :trigger_description "someDesc3" :url "http://some/url/3"}])]
      (is (= [{:id 3 :detector_name "someName1" :detector_description "someDesc1" :url "http://some/url/1"
               :triggers [{:id 7 :trigger_name "someName3" :trigger_description "someDesc3" :url "http://some/url/3"}]}
              {:id 5 :detector_name "someName2" :detector_description "someDesc2" :url "http://some/url/2"
               :triggers [{:id 7 :trigger_name "someName3" :trigger_description "someDesc3" :url "http://some/url/3"}]}]
             (get-trigger-mapping)))))

  (testing "that correct trigger mapping is calculated if no trigger is mapped to detector"
    (with-redefs [load-detectors (fn [] [{:id 3 :detector_name "someName1" :detector_description "someDesc1" :url "http://some/url/1"}])
                  load-matching-triggers (fn [_] [])]
      (is (= [{:id 3 :detector_name "someName1" :detector_description "someDesc1" :url "http://some/url/1" :triggers []}]
             (get-trigger-mapping))))))
