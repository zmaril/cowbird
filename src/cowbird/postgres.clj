(ns cowbird.postgres
  (:require [clojure.tools.cli :refer [parse-opts]]
            [cowbird.protocol :refer [startup-packet regular-packet]]
            [gloss.io :as gio]
            [gloss.core :as gloss]
            [aleph.tcp :as tcp]
            [gloss.core.formats :as formats]
            [manifold.stream :as stream]
            [gloss.data.bytes :as bytes]
            [byte-streams :as b]
            [clojure.java.io :as io]))

(def postgres-cli-options
  [["-D" "--pgdatadir DIR" "The directory in which data is meant to be stored."
    :id :pgdatadir
    :default ""]
   ["-F" nil "Don't file sync."
    :id :disable-fsync]
   ["-c" "--c DIR""Set a named parameter."
    :id :named-parameter] ;;TODO there can be multiple of these
    ])

(defn startup-packet->map [packet]
  (as-> (gio/decode startup-packet packet) $
    (nth $ 2)
    (clojure.string/split $ #"\x00")
    (partition 2 $)
    (mapv vec $)
    (into {} $)))

(def last-msg (atom nil))

(defn handle-startup [to-client start-packet]
  (println "Handling startup")
  )

(defn handle-regular [to-client msg]
  (println "Handling regular")
  )
(defn consumer [to-client msg]
  (reset! last-msg msg)
  (b/print-bytes msg)
  (if (zero? (first msg))
    (handle-startup to-client (startup-packet->map msg))
    (handle-regular to-client msg)))

(def encode-packet  #(gio/encode regular-packet %))

(defn handle-message [{:keys [sent-auth-request? id info client-info to-client] :as m} msg]
  (println "---------------------")
  (println "Handle message")
  (println "Connection id " id)
  (println "sent-auth-request?" sent-auth-request?)
  (println to-client)
  (b/print-bytes msg)
  (if-not sent-auth-request?
    (let [response {:type :auth-request
                    :payload [12 :md5 "haha"]}]      
      (println "Startup")
      (println "Sent md5 password request")
      (b/print-bytes (encode-packet response))
      (println (stream/put! to-client (encode-packet response)))
      (println " ")
      (-> m
          (assoc :client-info (startup-packet->map msg))
          (assoc :sent-auth-request? true)))
    (do 
      (let [sure-ok {:type :auth-request
                     :payload [12 :ok]}
            response {:type :query-status
                      :payload [5 :idle]}]
        (println "Sure, password looks fine")
        (b/print-bytes (encode-packet sure-ok))
        (stream/put! to-client (encode-packet sure-ok))
        (println "Sending idle status")
        (b/print-bytes (encode-packet response))
        (stream/put! to-client (encode-packet response))
        (println "Sent okay")
        (println "")
        m))))

(defn handler [s info]
  (stream/reduce handle-message
                 {:sent-auth-request? false
                  :to-client s
                  :info info
                  :id (rand)}
                 s))

(defn start-server
  [port]
  (tcp/start-server handler {:port port}))

(defn postgres [args]
  (let [args (-> (parse-opts args postgres-cli-options)
                 (assoc-in [:options :port] (Integer/parseInt (System/getenv "PGPORT"))))]
    (println args)
    (start-server (-> args :options :port))))

