(ns puresec-master.service.alarm-state-test
  (:use clojure.test
        ring.mock.request
        puresec-master.handler)
  (:require [puresec-master.service.alarm-state :as alarm-state]))

(deftest test-add-fired
  (testing "that a fired handler will be added to the state"
    (reset! alarm-state/handler-fired {})
    (is (= (alarm-state/add-fired 7) {7 true}))
    (let [handler (alarm-state/enhance-handler-with-state {:id 7})]
      (is (= handler {:id 7 :fired true}))))

  (testing "that a second fired handler will be added to the state"
    (reset! alarm-state/handler-fired {})
    (is (= (alarm-state/add-fired 9) {9 true}))
    (is (= (alarm-state/add-fired 11) {9 true
                                       11 true}))
    (let [handler (alarm-state/enhance-handler-with-state {:id 9})
          handler2 (alarm-state/enhance-handler-with-state {:id 11})]
      (is (= handler {:id 9 :fired true}))
      (is (= handler2 {:id 11 :fired true}))))

  (testing "that a not known handler has no fired state"
    (reset! alarm-state/handler-fired {})
    (is (= (alarm-state/add-fired 13) {13 true}))
    (let [handler (alarm-state/enhance-handler-with-state {:id 13})
          handler2 (alarm-state/enhance-handler-with-state {:id 15})]
      (is (= handler {:id 13 :fired true}))
      (is (= handler2 {:id 15 :fired false})))))
