(ns noob-project.server
  (:require [ring.adapter.jetty :as jetty]
            [noob-project.routes :as routes]
            [ring.middleware.reload :refer [wrap-reload]]
            [ring.middleware.stacktrace :refer [wrap-stacktrace]]
            [ring.middleware.keyword-params :as rmkp]
            [ring.middleware.json :refer [wrap-json-response wrap-json-body]]
            [ring.middleware.multipart-params :refer [wrap-multipart-params]]))

(defn start-server [config]
  (let [port (:port config)]
    (jetty/run-jetty
      (-> routes/app
          wrap-reload
          wrap-stacktrace
          (wrap-json-body {:key-fn keyword})
          rmkp/wrap-keyword-params
          wrap-multipart-params
          routes/wrap-headers-response
          wrap-json-response
          routes/print-response)
      {:port          port
       :join?         true
       :auto-refresh? true})))