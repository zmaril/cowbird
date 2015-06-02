(ns cowbird.protocol-test
  (:require [clojure.test :refer :all]
            [cowbird.protocol :refer :all]
            [gloss.io :refer :all]
            [gloss.core :refer :all]
            [byte-streams :refer [print-bytes]]))

(defn =regular [_ bits d]
  (= bits (encode regular-packet d)))
(deftest a-test
  (testing "Q....a;."
    (is (=
         [0x51 0x00 0x00 0x00 0x07 0x61 0x3B 0x00]
         (encode regular-packet  {:type :query 
                                  :payload [7 "a;"]})))))

(print-bytes  (encode regular-packet  {:type :query 
                                         :payload [7 "a;"]}))
