(ns noob-project.core
  (:gen-class)
  (:require [mount.core :as mount]
            [noob-project.server :as server]))

(defn start-server []
  (mount/start)
  (println "Starting server")
  (server/start-server {:port 3000}))

(defn -main
  "This is -main function of core.clj"
  [& args]
  (start-server))