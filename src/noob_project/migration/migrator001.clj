(ns noob-project.migration.migrator001)


(defn -main
  "This is -main function of migrator001.clj"
  [& args]
  (println "Starting migration file")
  (if (migration-utils/is-migration-needed?)
    (do
      (println "Migration Needeed."))
    (println "Migration not required.")))