(ns noob-project.core
  (:gen-class)
   (:require [ring.adapter.jetty :as jetty]
             [clojure.pprint]
             [compojure.core :as compojure]
             [compojure.route :as route]))

(defn get-key-from-request [request keyName]
  "Returns the value of key in requsts :param
   Must send the keyword as keyName"
  (let [params (:params request)
        keyValue (get params keyName)]
    (println "prams for" keyName params "keyValue" keyValue)
    keyValue))

(defn home [request]
  "Welcome to Home Page")

(defn sections [request]
  ;; (println request)
  (let [sectionId (get-key-from-request request :sectionId)]
    (str "Welcome to section " sectionId)))

(defn task [request]
  (let [sectionId (get-key-from-request request :sectionId)
        taskId (get-key-from-request request :taskId)]
    (str "Showing description for " taskId " in section " sectionId)))

;; Ring Handler
;; (defn app [request]
;;   (clojure.pprint/pprint request)
;;   {:status 200
;;    :headers {"Content-Type" "text/html" "Custom Header" "Cusotm Header Value" "Another Header Key1 " "Another Header key2"}
;;    :body "Welcome to Noob Project 3.0 with Hot"})

(compojure/defroutes app
  (compojure/GET "/" params home)
  (compojure/GET "/:sectionId" params sections)
  (compojure/GET "/:sectionId/:taskId" params task)
  (route/not-found "Page Not Found"))

(defn -main
  "This is -main function of core.clj"
  [& args]
  (jetty/run-jetty app
                   {:port 3000
                    :join? true}))