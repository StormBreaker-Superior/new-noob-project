(ns noob-project.core
  (:gen-class)
   (:require [ring.adapter.jetty :as jetty]
             [noob-project.routes :as routes]))

;; Ring Handler
;; (defn app [request]
;;   (clojure.pprint/pprint request)
;;   {:status 200
;;    :headers {"Content-Type" "text/html" "Custom Header" "Cusotm Header Value" "Another Header Key1 " "Another Header key2"}
;;    :body "Welcome to Noob Project 3.0 with Hot"})

(defn -main
  "This is -main function of core.clj"
  [& args]
  (jetty/run-jetty routes/app
                   {:port 3000
                    :join? true}))