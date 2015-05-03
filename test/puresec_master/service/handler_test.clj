(ns puresec-master.service.handler-test
  (:use clojure.test
        puresec-master.service.handler
        puresec-master.service.slave
        puresec-master.db.core))

(deftest test-register-handler
  (testing "that the registration of a handler is successful"
    (with-redefs [register-slave (fn [_ _ _ _ _] {:state "SUCCESS" :id 9})
                  update-handler-cache (fn [] nil)]
      (swap! handler-cache (fn [_] nil))
      (is (= {:state "SUCCESS" :id 9}
             (register-handler "some name" "some descr" "http://someUrl")))))

  (testing "that the registration of a handler updates the cache properly"
    (with-redefs [register-slave (fn [_ _ _ _ _] {:state "SUCCESS" :id 9})
                  get-slaves (fn [_] [{:id 9 :name "some name" :description "some descr"}])]
      (swap! handler-cache (fn [_] nil))
      (register-handler "some name" "some descr" "http://someUrl")
      (is (= [{:id 9 :name "some name" :description "some descr"}]
             @handler-cache)))))

(deftest test-get-handlers
  (testing "that list of handlers is returned"
    (with-redefs [update-handler-cache (fn [] [{:id 5 :name "name 0" :description "name 0 descr"}
                                      {:id 7 :name "name 1" :description "name 1 descr"}])]
      (swap! handler-cache (fn [_] nil))
      (is (= [{:id 5 :name "name 0" :description "name 0 descr"}
              {:id 7 :name "name 1" :description "name 1 descr"}]
             (get-handlers))))))

(deftest test-update-cache
  (testing "that the cache is properly updated"
    (with-redefs [get-slaves (fn [_] [{:id 11 :name "name 0" :description "name 0 descr"}
                                      {:id 13 :name "name 1" :description "name 1 descr"}])]
      (swap! handler-cache (fn [_] nil))
      (is (= nil
             @handler-cache))
      (is (= [{:id 11 :name "name 0" :description "name 0 descr"}
              {:id 13 :name "name 1" :description "name 1 descr"}]
             (update-handler-cache)))
      (is (= [{:id 11 :name "name 0" :description "name 0 descr"}
              {:id 13 :name "name 1" :description "name 1 descr"}]
             @handler-cache)))))
