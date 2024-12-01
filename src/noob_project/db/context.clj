(ns noob-project.db.context
  (:require [mount.core :refer [defstate]]
            [monger.core :as mg]
            [noob-project.constansts :as consts]))

(defstate conn
  :start (mg/connect)
  :stop (mg/disconnect conn))

(defn get-db [] (mg/get-db conn consts/database-second-brain))