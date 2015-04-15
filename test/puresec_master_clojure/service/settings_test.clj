(ns puresec-master-clojure.service.settings-test
  (:use clojure.test
        puresec-master-clojure.db.core
        puresec-master-clojure.service.settings))

(deftest test-map-trigger
  (testing "that the trigger is mapped to a detector"
    (with-redefs [save-trigger-mapping (fn [_] 1)]
      (is (= 1
             (map-trigger 7 9))))))
