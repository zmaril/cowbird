(ns cowbird.protocol-test
  (:require [clojure.test :refer :all]
            [cowbird.protocol :refer :all]
            [gloss.io :refer :all]
            [gloss.core :refer :all]
            [byte-streams :refer [print-bytes convert]]))

(defn packet-map->ints [m]
  (as-> m $
    (encode regular-packet $)
    (contiguous $)
    (convert $ String)
    (seq $)
    (map int $)))

(defn ints->packet-map [is]
  (as-> is $
    (map char $)
    (apply str $)
    (convert $  java.nio.ByteBuffer)
    (decode regular-packet $)))

(defmacro testing-conversions  
  [doc & args]
 `(testing ~doc
       (are [x y] (= x (ints->packet-map y) x) ~@args)
       (are [x y] (= (packet-map->ints x) y) ~@args)))

(deftest a-test
  (testing-conversions "Simple query"
         {:type :query
          :payload "a;"}
         [81 0 0 0 6 97 59]

         {:type :password
          :payload "password"}
         [112 0 0 0 12 112 97 115 115 119 111 114 100]

         {:type :query-status
          :payload :idle}
         [90 0 0 0 5 73]

         {:type :query
          :payload "select * from example;"}
         [81 0 0 0 26 115 101 108 101 99 116 32 42 32 102 114 111 109 32 101 120
          97 109 112 108 101 59]
         
         {:type :auth-request
          :payload [:md5 "hahaha"]}
         [82 0 0 0 14 0 0 0 5 104 97 104 97 104 97]
         
         ))


