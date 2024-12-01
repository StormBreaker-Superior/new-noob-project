(ns noob-project.db.utils 
  (:require
   [monger.collection :as mc]
   [noob-project.db.context :as db]))

;; 1) make atomic getter for lsat-task-id
;; 2) inititally, get the value from db
;; 3) then, use atom variable
;; 4) when update is called, first update the db and then swap the atom

(defn insert-data [collection data]
  (mc/insert-and-return (db/get-db) collection data))

(defn data-exists? [collection data]
  (boolean (mc/find-one (db/get-db) collection data)))

(defn get-count [collection]
  (mc/count (db/get-db) collection))

(defn get-data [collection projection filter]
  (mc/find-maps (db/get-db) collection projection filter))

(defn delete-data [collection projection]
  (mc/remove (db/get-db) collection projection))

(defn update-data [collection document updates]
  (mc/update (db/get-db) collection document updates))