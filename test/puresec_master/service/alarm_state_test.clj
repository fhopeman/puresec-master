(ns puresec-master.service.alarm-state-test
  (:use clojure.test
        ring.mock.request)
  (:require [puresec-master.service.alarm-state :as alarm-state]))

(deftest test-add-fired
  (testing "that a fired detector will be added to the state"
    (reset! alarm-state/detectors-fired {})
    (is (= (alarm-state/add-fired 7) {7 true}))
    (is (= @alarm-state/detectors-fired {7 true})))

  (testing "that a second fired detector will be added to the state"
    (reset! alarm-state/detectors-fired {})
    (is (= (alarm-state/add-fired 9) {9 true}))
    (is (= (alarm-state/add-fired 11) {9 true 11 true}))
    (is (= @alarm-state/detectors-fired {9 true 11 true}))))

(deftest test-enhance-detector-with-state
  (testing "that a known detector has a fired state"
    (reset! alarm-state/detectors-fired {})
    (alarm-state/add-fired 23)
    (let [detector (alarm-state/enhance-detector-with-state {:id 23})]
      (is (= detector {:id 23 :fired true}))))

  (testing "that a not known detector has no fired state"
    (reset! alarm-state/detectors-fired {})
    (alarm-state/add-fired 13)
    (let [detector (alarm-state/enhance-detector-with-state {:id 13})
          detector2 (alarm-state/enhance-detector-with-state {:id 15})]
      (is (= detector {:id 13 :fired true}))
      (is (= detector2 {:id 15 :fired false})))))

(deftest test-enhande-detectors-with-state
  (testing "that the detectors will be enhanced with their state"
    (reset! alarm-state/detectors-fired {})
    (alarm-state/add-fired 17)
    (alarm-state/add-fired 19)
    (let [detectors (alarm-state/enhance-detectors-with-state [{:id 17}
                                                            {:id 19}
                                                            {:id 21}])]
      (is (= detectors [{:id 17 :fired true}
                       {:id 19 :fired true}
                       {:id 21 :fired false}])))))

(deftest test-clear-state
  (testing "that the fired state is cleared"
    (reset! alarm-state/detectors-fired {})
    (alarm-state/add-fired 7)
    (alarm-state/clear-state)
    (is (= @alarm-state/detectors-fired {}))))
