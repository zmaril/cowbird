(ns cowbird.postgres
  (:require [clojure.tools.cli :refer [parse-opts]]
            [com.stuartsierra.component :as component]
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

(def encode-packet  #(gio/encode regular-packet %))

(defn handle-startup-message [{:keys [to-client] :as m} msg]
  (let [response {:type :auth-request
                  :payload [12 :md5 "haha"]}]
    (stream/put! to-client (encode-packet response))
    (-> m
        (assoc :client-info (startup-packet->map msg))
        (assoc :sent-auth-request? true))))


(defmulti handle-regular-message (fn [_ msg] (:type msg)))

(defmethod handle-regular-message :default [& params]
  (println "Cannot handle " params)
  (/ 1 'asshole))


(defmethod handle-regular-message :password
  [{:keys [to-client] :as m} {[_ query-status-code] :payload}]
  (let [sure-ok  {:type :auth-request :payload [12 :ok]}
        response {:type :query-status :payload [5 :idle]}]
    (stream/put! to-client (encode-packet sure-ok))
    (stream/put! to-client (encode-packet response))
    m))

(defmethod handle-regular-message :query
  [{:keys [to-client] :as m} {[_ query] :payload}]
  (println query))

;;TODO: handle cancel requests. They have a different structure than
;;startup/regular packets. 
(defn handle-message [{:keys [sent-auth-request?] :as m} msg]
  (b/print-bytes msg)
  (if sent-auth-request?
    (handle-regular-message m (gio/decode regular-packet msg))
    (handle-startup-message m msg)))

(defn handler [s info]
  (stream/reduce handle-message
                 {:sent-auth-request? false
                  :to-client s
                  :info info
                  :id (rand-int 10000)}
                 s))

(defrecord Postgres [port server]
  component/Lifecycle

  (start [component]
    (println "Starting tcp server")
    (assoc component :server (tcp/start-server handler {:port port})))

  (stop [component]
    (println "Stoping tcp server")
    (.close server)
    (assoc component :server nil)))


(defn new-database [args]
  (let [args (-> (parse-opts args postgres-cli-options)
                 (assoc-in [:options :port] (Integer/parseInt (System/getenv "PGPORT"))))
        postgres (map->Postgres (:options args))]
    (.start postgres)))

