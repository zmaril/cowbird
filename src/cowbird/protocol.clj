(ns cowbird.protocol
  (require [gloss.core :refer :all]
           [gloss.io   :refer :all]))

(def query-int32-prefix
  (prefix :int32
          #(- % 4)
          #(+ % 4)))

;;Can't quite figure out how to encode this the right way. Relies on a transform
;;afterwords.
(defcodec startup-packet
  [:int32 :int16 (string :utf-8)])

;;https://github.com/postgres/postgres/blob/4baaf863eca5412e07a8441b3b7e7482b7a8b21a/src/include/libpq/pqcomm.h#L165
(defcodec auth-request-codes
  (enum :int32 {:ok 0
                :password 3
                :md5 5
                :scm-creds 6
                :gss 7
                :gss-cnt 8
                :sspi 9}))


(defcodec auth-request
  (ordered-map
   :type :auth-request
   :payload
   (finite-frame query-int32-prefix
                 [auth-request-codes (string :utf-8)])))

(defcodec query-status-codes
  (enum :byte {:idle \I}))

(defcodec query
  (ordered-map
   :type :query
   :payload
   (finite-frame query-int32-prefix (string :utf-8))))


(defcodec query-status
  (ordered-map
   :type :query-status
   :payload
   (finite-frame query-int32-prefix query-status-codes)))

(defcodec password
  (ordered-map
   :type :password
   :payload
   (finite-frame query-int32-prefix (string :utf-8))))

(defcodec tag
  (enum :byte {:auth-request \R
               :password     \p
               :query        \Q
               :query-status \Z}))

(defcodec regular-packet
  (header
   tag
   {:auth-request  auth-request
    :password      password
    :query         query
    :query-status  query-status}
   :type))
