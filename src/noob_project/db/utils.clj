(ns noob-project.db.utils 
  (:require
   [monger.collection :as mc]
   [noob-project.constansts :as constants]
   [monger.util :as mu]
   [noob-project.db.context :as dbc]))

;; 1) make atomic getter for lsat-task-id
;; 2) inititally, get the value from db
;; 3) then, use atom variable
;; 4) when update is called, first update the db and then swap the atom

(defn insert-data [collection data]
  (mc/insert-and-return (dbc/get-db) collection data))

(defn data-exists? [collection data]
  (boolean (mc/find-one (dbc/get-db) collection data)))

(defn get-count [collection]
  (mc/count (dbc/get-db) collection))

(defn get-data
  ([collection projection filter]
   (mc/find-maps (dbc/get-db) collection projection filter))
  ([collection]
   (get-data collection {} {}))
  ([collection projection]
   (get-data collection projection {}))
  )

(defn get-data-map
  ([collection projection filter]
   (mc/find-one-as-map (dbc/get-db) collection projection filter))
  ([collection projection]
   (mc/find-one-as-map (dbc/get-db) collection projection {})))

(defn delete-data [collection projection]
  (mc/remove (dbc/get-db) collection projection))

(defn update-data [collection document updates]
  (mc/update (dbc/get-db) collection document updates))

(defn get-last-task-id
  "Get last task/section id"
  []
  (let [meta-data-list (get-data constants/collection-meta-data {} {})
        meta-data (nth meta-data-list 0 {})]
    (:last-id meta-data 0)))

(defn update-last-task-id [new-id]
  (update-data constants/collection-meta-data {} {:$set {:last-id new-id}}))

(defn generate-random-uuid []
  (mu/random-uuid))

(defn get-or-generate-id [collection filter-key filter-value id-key]
  (let [existing-data-map (get-data-map collection {filter-key filter-value})]
    (if (not-empty existing-data-map)
      {:id ((keyword id-key) existing-data-map ) :generated false}
      (let [generated-id (generate-random-uuid)]
        {:id generated-id :generated true}))
    ))

