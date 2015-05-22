(ns cowbird.postgres
  (:require [clojure.tools.cli :refer [parse-opts]]
            [cowbird.protocol :refer [startup-packet]]
            [gloss.io :as gio]
            [aleph.tcp :as tcp]
            [manifold.stream :as stream]
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

(defonce a (atom nil))

(defn handler [s info]
  (stream/consume (partial reset! a)  s))

(defn start-server
  [port]
  (tcp/start-server handler {:port port}))

(defn postgres [args]
  (let [args (-> (parse-opts args postgres-cli-options)
                 (assoc-in [:options :port] (Integer/parseInt (System/getenv "PGPORT"))))]
    (println args)
    (start-server (-> args :options :port))))
