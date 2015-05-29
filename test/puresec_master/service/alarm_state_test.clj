(ns puresec-master.service.alarm-state-test
  (:use clojure.test
        ring.mock.request
        puresec-master.handler)
  (:require [puresec-master.service.alarm-state :as alarm-state]))

(deftest test-add-fired
  (testing "that a fired handler will be added to the state"
    (reset! alarm-state/handler-fired {})
    (is (= (alarm-state/add-fired 7) {7 true}))
    (is (= @alarm-state/handler-fired {7 true})))

  (testing "that a second fired handler will be added to the state"
    (reset! alarm-state/handler-fired {})
    (is (= (alarm-state/add-fired 9) {9 true}))
    (is (= (alarm-state/add-fired 11) {9 true 11 true}))
    (is (= @alarm-state/handler-fired {9 true 11 true}))))

(deftest test-enhance-handler-with-state
  (testing "that a known handler has a fired state"
    (reset! alarm-state/handler-fired {})
    (alarm-state/add-fired 23)
    (let [handler (alarm-state/enhance-handler-with-state {:id 23})]
      (is (= handler {:id 23 :fired true}))))

  (testing "that a not known handler has no fired state"
    (reset! alarm-state/handler-fired {})
    (alarm-state/add-fired 13)
    (let [handler (alarm-state/enhance-handler-with-state {:id 13})
          handler2 (alarm-state/enhance-handler-with-state {:id 15})]
      (is (= handler {:id 13 :fired true}))
      (is (= handler2 {:id 15 :fired false})))))

(deftest test-enhande-handlers-with-state
  (testing "that the handlers will be enhanced with their state"
    (reset! alarm-state/handler-fired {})
    (alarm-state/add-fired 17)
    (alarm-state/add-fired 19)
    (let [handlers (alarm-state/enhance-handlers-with-state [{:id 17}
                                                            {:id 19}
                                                            {:id 21}])]
      (is (= handlers [{:id 17 :fired true}
                       {:id 19 :fired true}
                       {:id 21 :fired false}])))))
