(ns puresec-master.service.handler-test
  (:use clojure.test)
  (:require [puresec-master.db.core :as db]
              [puresec-master.service.handler :as handler-service]
              [puresec-master.service.slave :as slave-service]))

(deftest test-register-handler
  (testing "that the registration of a handler is successful"
    (with-redefs [slave-service/register-slave (fn [_ _ _ _ _] {:state "SUCCESS" :id 9})
                  handler-service/update-handler-cache (fn [] nil)]
      (swap! handler-service/handler-cache (fn [_] nil))
      (is (= {:state "SUCCESS" :id 9}
             (handler-service/register-handler "some name" "some descr" "http://someUrl")))))

  (testing "that the registration of a handler updates the cache properly"
    (with-redefs [slave-service/register-slave (fn [_ _ _ _ _] {:state "SUCCESS" :id 9})
                  slave-service/get-slaves (fn [_] [{:id 9 :name "some name" :description "some descr"}])]
      (swap! handler-service/handler-cache (fn [_] nil))
      (handler-service/register-handler "some name" "some descr" "http://someUrl")
      (is (= [{:id 9 :name "some name" :description "some descr"}]
             @handler-service/handler-cache)))))

(deftest test-remove-handler
  (testing "that the handler is removed successfully"
    (with-redefs [db/delete-handler! (fn [_] 1)
                  handler-service/update-handler-cache (fn [] (reset! handler-service/handler-cache nil))]
      (reset! handler-service/handler-cache [{:id 11 :name "name 0" :description "name 0 descr"}])
      (is (= (handler-service/remove-handler 11)
             true))
      (is (= nil
             @handler-service/handler-cache)))))

(deftest test-get-handlers
  (testing "that list of handlers is returned"
    (with-redefs [handler-service/update-handler-cache (fn [] [{:id 5 :name "name 0" :description "name 0 descr"}
                                                               {:id 7 :name "name 1" :description "name 1 descr"}])]
      (swap! handler-service/handler-cache (fn [_] nil))
      (is (= [{:id 5 :name "name 0" :description "name 0 descr"}
              {:id 7 :name "name 1" :description "name 1 descr"}]
             (handler-service/get-handlers))))))

(deftest test-update-cache
  (testing "that the cache is properly updated"
    (with-redefs [slave-service/get-slaves (fn [_] [{:id 11 :name "name 0" :description "name 0 descr"}
                                                    {:id 13 :name "name 1" :description "name 1 descr"}])]
      (swap! handler-service/handler-cache (fn [_] nil))
      (is (= nil
             @handler-service/handler-cache))
      (is (= [{:id 11 :name "name 0" :description "name 0 descr"}
              {:id 13 :name "name 1" :description "name 1 descr"}]
             (handler-service/update-handler-cache)))
      (is (= [{:id 11 :name "name 0" :description "name 0 descr"}
              {:id 13 :name "name 1" :description "name 1 descr"}]
             @handler-service/handler-cache)))))
