(ns cowbird.core
  (:require [cowbird.initdb   :refer [initdb]]
            [cowbird.psql     :refer [psql]]
            [cowbird.pg-ctl   :refer [pg-ctl]]
            [cowbird.postgres :refer [postgres]]))

(defn -main [& args]
  (println args)
  (case (first args)
    "initdb"   (initdb    (rest args))
    "postgres" (postgres  (rest args))
    "pg-ctl"   (pg-ctl    (rest args))
    "psql"     (psql      (rest args))))
