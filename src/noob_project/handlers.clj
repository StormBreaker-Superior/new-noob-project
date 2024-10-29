(ns noob-project.handlers
  (:require [noob-project.utils :as utils]))

(defn home [request]
  "Welcome to Home Page")

(defn sections [request]
  ;; (println request)
  (let [sectionId (utils/get-key-from-request request :sectionId)]
    (str "Welcome to section " sectionId)))

(defn task [request]
  (let [sectionId (utils/get-key-from-request request :sectionId)
        taskId (utils/get-key-from-request request :taskId)]
    (str "Showing description for " taskId "   in section " sectionId)))