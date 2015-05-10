(ns cowbird.core
  (:require [clojure.tools.cli :refer [parse-opts]]
            [clojure.java.io :as io])
  (:import [org.newsclub.net.unix AFUNIXSocket AFUNIXServerSocket AFUNIXSocketAddress]))

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

(def postgres-cli-options
  [["-D" "--pgdatadir DIR" "The directory in which data is meant to be stored."
    :id :pgdatadir
    :default ""]
   ["-F" nil "Don't file sync."
    :id :disable-fsync]
   ["-c" nil "Set a named parameter."
    :id :named-parameter] ;;TODO there can be multiple of these
   ["-k" "--domain-sockt-directory DIR" "Create a socket in a particular place.."
    :id :domain-socket-directory]])

;; Copied from https://github.com/monsanto/nreplds/blob/master/nreplds/src/nreplds/core.clj#L25
(defn start-server
  "Starts a Unix domain socket-based nREPL server. Configuration options include:
  * :path — the file system path to the domain socket"
  [path]
  (println path)
  (let [file (io/file path)
        socket-address (AFUNIXSocketAddress. file)
        ss (AFUNIXServerSocket/bindOn socket-address)]
    ss))

(defn postgres [args]
  (let [args (parse-opts args postgres-cli-options)]
    (println args)
    (start-server (get-in args [:options :domain-socket-directory]))
    args))

(defn -main [& args]
  (println args)
  (case (first args)
    "initdb"   (initdb (rest args))
    "postgres" (postgres (rest args))))

;(start-server "/tmp/pg_regress-IoDcNEa")
;(AFUNIXSocketAddress. (io/file "/tmp/testingpg"))
