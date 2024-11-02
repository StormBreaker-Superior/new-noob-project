(ns noob-project.server
  (:require [ring.adapter.jetty :as jetty]
            [noob-project.routes :as routes]
            [ring.middleware.reload :refer [wrap-reload]]
            [ring.middleware.stacktrace :refer [wrap-stacktrace]]
            [ring.middleware.keyword-params :as rmkp]
            [ring.middleware.multipart-params :refer [wrap-multipart-params]]))

(defn start-server [config]
  (let [port (:port config)]
    (jetty/run-jetty
     (-> routes/app
         rmkp/wrap-keyword-params
         wrap-multipart-params
         wrap-reload
         wrap-stacktrace)
     {:port port
      :join? true
      :auto-refresh? true})))