(ns noob-project.migration.utils
  (:require
   [noob-project.db :as db]))

(def local-db-name "meta-data")

(defn is-migration-needed? []
  (let [db-meta-data-list (db/get-data local-db-name {} {})
        db-meta-data (nth db-meta-data-list 0 {})
        database-version (:database-version db-meta-data 0)
        migration-version (:migration-version db-meta-data 0)]
    (if (> database-version migration-version)
      true
      false)))