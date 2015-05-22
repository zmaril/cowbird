(ns cowbird.protocol
  (require [gloss.core :refer :all]
           [gloss.io   :refer :all]))

;; (def regular-frame
;;   (gloss/compile-frame
;;    [(gloss/string :utf-8 :length 1)
;;     (gloss/finite-frame :int32 (gloss/string :utf-8))]))

(defcodec maybe-startup-packet
  (finite-frame
   :int32
   (ordered-map
    :protocol :int32
    :payload (repeated
              (string :utf-8 :delimiters ["\\0"])
              :delimiters ["\\0"]))))

(defcodec startup-packet
  (ordered-map
   :len :int32-be
   :protocol :int32-be
   :payload  (repeated (string :utf-8 :delimiters ["\\0"])
                       :prefix :none
                       :delimiters ["\\0"]))
                           
  )

;; (gloss/defcodec parameter-status
;;   (gloss/finite-frame
;;    :int32
;;    [(gloss/string :utf-8) (gloss/string :utf-8)]
;;    ))

;; (gio/encode startup-packet
;;             {:protocol 3.0
;;              :payload ["k" "v"]})
