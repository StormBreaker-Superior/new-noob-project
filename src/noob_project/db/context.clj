(ns noob-project.db
  (:require [mount.core :refer [defstate]]
            [monger.core :as mg]
            [monger.collection :as mc]
            [noob-project.constansts :as consts]))

(defstate conn
  :start (mg/connect)
  :stop (mg/disconnect conn))

(defn get-db [] (mg/get-db conn consts/database-second-brain))

(defn insert-data [collection data]
  (mc/insert-and-return (get-db) collection data))

(defn data-exists? [collection data]
  (boolean (mc/find-one (get-db) collection data)))

(defn get-count [collection]
  (mc/count (get-db) collection))

(defn get-data [collection projection filter]
  (mc/find-maps (get-db) collection projection filter))

(defn delete-data [collection projection]
  (mc/remove (get-db) collection projection))

(defn update-data [collection document updates] 
  (mc/update (get-db) collection document updates))