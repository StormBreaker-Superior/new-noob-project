(ns noob-project.migration.migrator001
  (:gen-class)
  (:require
   [noob-project.migration.utils :as migration-utils]
   [noob-project.db.utils :as dbu]
   [noob-project.db.context :as dbc]
   [mount.core :as mount]
   [monger.collection :as mc]
   [noob-project.constansts :as consts]))

(defn migrate-sections []
  (try
    (let [existing-section-docs (dbu/get-data consts/collection-sections {:taskId {:$exists false}} {})
          previous-id (atom (dbu/get-last-task-id))]
      (doseq [section-doc existing-section-docs]
        (let [new-id (swap! previous-id inc)
              result (mc/update-by-id (dbc/get-db) consts/collection-sections (:_id section-doc) {:$set {:sectionId new-id}})]
          (if (not result)
            (swap! previous-id dec))))
      (if (not-empty existing-section-docs)
        (do
          (println "Updating section to" @previous-id)
          (dbu/update-last-task-id @previous-id))))
    (catch Exception e
      (println "Error in migrating sections"))))


(defn migrate-tasks []
  (try
    (let [existing-tasks-docs (dbu/get-data consts/collection-tasks {:sectionId {:$exists false}} {})
          previous-id (atom (dbu/get-last-task-id))]
      (doseq [task-document existing-tasks-docs]
        (let [new-id (swap! previous-id inc)
              result (mc/update-by-id (dbc/get-db) consts/collection-tasks (:_id task-document) {:$set {:taskId new-id}})]
          (if (not result)
            (swap! previous-id dec))))
      (if (not-empty existing-tasks-docs)
        (do
          (println "Updating task to" @previous-id)
          (dbu/update-last-task-id @previous-id))))
    (catch Exception e
      (println "Error in migrating tasks"))))


(defn start-migration
  []
  (println "starting migration .. ")
  (migrate-sections)
  (migrate-tasks)
  (dbu/update-data consts/collection-meta-data {} {:$inc {:migration-version 1}})
  (println "Migration Successful .. "))


(defn -main
  "This is -main function of migrator001.clj"
  [& args]
  (mount/start)
  (println "Starting migration file")
  (if (migration-utils/is-migration-needed?)
    (do
      (println "Migration Needeed.")
      (start-migration))
    (println "Migration not required.")))