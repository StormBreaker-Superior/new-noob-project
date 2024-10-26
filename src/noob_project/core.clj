(ns noob-project.core
  (:gen-class)
   (:require [ring.adapter.jetty :as jetty]
             [clojure.pprint]
             [compojure.core :as compojure]
             [compojure.route :as route]))

(compojure/defroutes app
    (compojure/GET "/" [] "Welcome to compojure 2.0") 
    (route/not-found "Page Not Found") )

;; Ring Handler
;; (defn app [request]
;;   (clojure.pprint/pprint request)
;;   {:status 200
;;    :headers {"Content-Type" "text/html" "Custom Header" "Cusotm Header Value" "Another Header Key1 " "Another Header key2"}
;;    :body "Welcome to Noob Project 3.0 with Hot"})

(defn -main
  "This is -main function of core.clj"
  [& args]
  (jetty/run-jetty app
                   {:port 3000
                    :join? true}))
