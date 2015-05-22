(ns cowbird.pg-ctl
  (:require [clojure.tools.cli :refer [parse-opts]]
            [aleph.tcp :as tcp]
            [manifold.stream :as s]
            [clojure.java.io :as io]))

(def pg-ctl-cli-options
  [["-D" "--pgdata PGDATADIR" nil ]
   [nil "--noclean" nil]
   [nil "--nosync" nil]])

(defn pg-ctl [args]
  (System/exit 0))
