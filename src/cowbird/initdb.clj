(ns cowbird.initdb
  (:require [clojure.tools.cli :refer [parse-opts]]
            [aleph.tcp :as tcp]
            [manifold.stream :as s]
            [clojure.java.io :as io]))

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
    (io/copy sample-conf conf-file)
    (System/exit 0)))
