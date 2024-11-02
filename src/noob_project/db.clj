(ns noob-project.db
  (:require [mount.core :refer [defstate]]
            [monger.core :as mg]
            [monger.collection :as mc]))

(defstate conn
  :start (mg/connect)
  :stop (mg/disconnect conn))

(defn get-db [] (mg/get-db conn "second-brain"))

(defn insert-data [collection data]
  (mc/insert-and-return (get-db) collection data))

(defn data-exists? [collection data]
  (boolean (mc/find-one (get-db) collection data)))

(defn get-count [collection]
  (mc/count (get-db) collection))

(defn get-data [collection projection filter]
  (mc/find-maps (get-db) collection projection filter))