(ns puresec-master-clojure.service.notification-dispatcher-test
  (:use clojure.test
        puresec-master-clojure.service.notification-dispatcher
        puresec-master-clojure.db.core))

(deftest test-dispatch-alarm-notification
  (testing "that the alarm notification is dispatched to the triggers"
    (with-redefs [load-matching-trigger (fn [_] [{:id 7 :trigger_name "some name 1" :trigger_description "some desc 1"}
                                                 {:id 9 :trigger_name "some name 2" :trigger_description "some desc 2"}])]
      (is (= [7 9]
             (dispatch-alarm-notification 5))))))
