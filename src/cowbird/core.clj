(ns cowbird.core
  (:require [clojure.tools.cli :refer [parse-opts]]
            [clojure.java.io :as io]))

(def cli-options
  ;; An option with a required argument
  [["-p" "--port PORT" "Port number"
    :default 80
    :parse-fn #(Integer/parseInt %)
    :validate [#(< 0 % 0x10000) "Must be a number between 0 and 65536"]]
   ;; A non-idempotent option
   ["-v" nil "Verbosity level"
    :id :verbosity
    :default 0
    :assoc-fn (fn [m k _] (update-in m [k] inc))]
   ;; A boolean option defaulting to nil
   ["-h" "--help"]])

(def initdb-cli-options
  [["-D" "--pgdata PGDATADIR" nil ]
   [nil "--noclean" nil]
   [nil "--nosync" nil]])

(defn initdb [args]
  (let [args (parse-opts args initdb-cli-options)
        sample-conf
        (io/file (io/resource "postgres/src/backend/utils/misc/postgresql.conf.sample"))
        conf-file
        (io/file (str (get-in args [:options :pgdata]) "/postgresql.conf"))]
    (println args)
    (io/make-parents conf-file)
    (io/copy sample-conf conf-file)))


(defn -main [& args]
  (println args)
  (case (first args)
    "initdb" (initdb (rest args))))
