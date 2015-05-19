(ns cowbird.postgres
  (:require [clojure.tools.cli :refer [parse-opts]]
            [aleph.tcp :as tcp]
            [manifold.stream :as s]
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

(defn handler [s info]
  (println "START")
  (println (type s) (.sink s) (.source s))
  (println (b/print-bytes (.sink s)))
  ;(println (b/convert s String))
  (println "STOP")
  (s/connect s s))

(defn start-server
  [{:keys [port host]}]
  (tcp/start-server handler {:port port}))

(defn postgres [args]
  (let [args (-> (parse-opts args postgres-cli-options)
                 (assoc-in [:options :port] (Integer/parseInt (System/getenv "PGPORT"))))]
    (println args)
    (start-server (:options args))))
