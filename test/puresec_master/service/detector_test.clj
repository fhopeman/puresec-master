(ns puresec-master.service.detector_test
  (:use clojure.test)
  (:require [puresec-master.db.core :as db]
            [puresec-master.service.detector :as detector-service]
            [puresec-master.service.slave :as slave-service]))

(deftest test-register-detector
  (testing "that the registration is successful"
    (with-redefs [slave-service/register-slave (fn [_ _ _ _ _] {:state "SUCCESS" :id 7})
                  detector-service/update-detector-cache (fn [] nil)]
      (is (= {:state "SUCCESS" :id 7}
             (detector-service/register-detector "some name" "some name descr" "http://someUrl")))))

  (testing "that the registration of a detector updates the cache properly"
    (with-redefs [slave-service/register-slave (fn [_ _ _ _ _] {:state "SUCCESS" :id 9})
                  slave-service/get-slaves (fn [_] [{:id 9 :name "some name" :description "some descr"}])]
      (swap! detector-service/detector-cache (fn [_] nil))
      (detector-service/register-detector "some name" "some descr" "http://someUrl")
      (is (= [{:id 9 :name "some name" :description "some descr"}]
             @detector-service/detector-cache)))))

(deftest test-remove-detector
  (testing "that the detector is removed successfully"
    (with-redefs [db/delete-detector! (fn [_] 1)
                  detector-service/update-detector-cache (fn [] (reset! detector-service/detector-cache nil))]
      (reset! detector-service/detector-cache [{:id 11 :name "name 0" :description "name 0 descr"}])
      (is (= (detector-service/remove-detector 11)
             true))
      (is (= nil
             @detector-service/detector-cache)))))

(deftest test-get-detectors
  (testing "that list of detectors is returned"
    (with-redefs [detector-service/update-detector-cache (fn [] [{:id 11 :name "name 0" :description "name 0 descr"}
                                                                 {:id 13 :name "name 1" :description "name 1 descr"}])]
      (swap! detector-service/detector-cache (fn [_] nil))
      (is (= [{:id 11 :name "name 0" :description "name 0 descr"}
              {:id 13 :name "name 1" :description "name 1 descr"}]
             (detector-service/get-detectors))))))

(deftest test-update-cache
  (testing "that the cache is properly updated"
    (with-redefs [slave-service/get-slaves (fn [_] [{:id 11 :name "name 0" :description "name 0 descr"}
                                      {:id 13 :name "name 1" :description "name 1 descr"}])]
      (swap! detector-service/detector-cache (fn [_] nil))
      (is (= nil
             @detector-service/detector-cache))
      (is (= [{:id 11 :name "name 0" :description "name 0 descr"}
              {:id 13 :name "name 1" :description "name 1 descr"}]
             (detector-service/update-detector-cache)))
      (is (= [{:id 11 :name "name 0" :description "name 0 descr"}
              {:id 13 :name "name 1" :description "name 1 descr"}]
             @detector-service/detector-cache)))))
