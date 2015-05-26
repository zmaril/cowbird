(ns cowbird.protocol
  (require [gloss.core :refer :all]
           [gloss.io   :refer :all]))
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
   [:int32 auth-request-codes (string :utf-8 :length 4)]
   ))

(defcodec query-status-codes
  (enum :int32 {:idle \I}))

(defcodec query-status
  (ordered-map
   :type :query-status
   :payload
   [:int32 query-status-codes]))

(defcodec tag (enum :byte {:auth-request \R
                            :query-status \Z}))
(defcodec regular-packet
  (header
   tag
   {:auth-request  auth-request
    :query-status  query-status}
   :type
   ))

;;(byte-streams/print-bytes  (encode regular-packet {:type :auth-request :payload [:ok ""]}))
