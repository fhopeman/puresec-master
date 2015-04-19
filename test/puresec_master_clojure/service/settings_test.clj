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
