(ns cowbird.psql
  (:require [clojure.tools.cli :refer [parse-opts]]
            [aleph.tcp :as tcp]
            [manifold.stream :as s]
            [clojure.tools.logging :as log]
            [manifold.deferred :as d]
            [clojure.java.io :as io]))

(def psql-cli-options
  [["-X" nil "Database name"
    :id :db-name]])

(defn create-client [m]
  (println m)
  (log/info "CREATING CLIENT")
  (-> (tcp/client (select-keys m [:port :host]))
      (d/timeout! 100 :failed-to-connect)
      deref))

(defn psql [args]
  (log/info "WHAT IS GOING ON")
  (let [args (-> (parse-opts args psql-cli-options)
                 (assoc-in [:options :port] (Integer/parseInt (System/getenv "PGPORT")))
                 (assoc-in [:options :host] (System/getenv "PGHOST"))
                 )
        _ (log/info args)
        client (create-client (:options args))]
    (log/info client)
    (if (= client :failed-to-connect)
      (do (println "FAILED TO CONNECT") (System/exit 2))
      (do (println "CONNECTED") (System/exit 0)))
    ))
