(ns noob-project.routes
  (:require [noob-project.handlers :as handlers]
            [compojure.core :as compojure]
            [compojure.route :as route]))

(compojure/defroutes app
  (compojure/GET "/" params handlers/home)
  (compojure/context "/tasks" []
    (compojure/GET "/:taskId" params handlers/task)
    (compojure/POST "/" params handlers/create-task)
    (compojure/PATCH "/:taskId" params handlers/update-task))
  (compojure/context "/sections" []
    (compojure/GET "/:sectionId" params handlers/get-tasks)
    (compojure/POST "/" params handlers/create-section)
    (compojure/DELETE "/:sectionId" params handlers/delete-section))
  (compojure/context "/users" []
    (compojure/POST "/" params handlers/create-user))
   (compojure/OPTIONS "*" []
     {:status 200
      :headers {}
      :body "OK"})
  (route/not-found "Page Not Found !"))

(defn wrap-headers-response [handler]
  (fn [request]
     (let [response (handler request)]
       (update response :headers merge {"Access-Control-Allow-Origin"  "http://localhost:4000"
                                "Access-Control-Allow-Methods" "GET, POST, OPTIONS"
                                "Access-Control-Allow-Headers" "Content-Type"}))))

(defn print-response [handler]
  (fn [request]
    (let [start-time (System/currentTimeMillis)  ;; Capture request start time
          response (handler request)             ;; Call the actual handler
          end-time (System/currentTimeMillis)    ;; Capture end time
          duration (- end-time start-time)       ;; Calculate duration
          status (:status response)
          headers (:headers response)
          body (:body response)]

      ;; Print request details
      (println "\n================ REQUEST =================")
      (println "Method:" (:request-method request))
      (println "Route:" (:uri request))
      (println "Query Params:" (:query-params request))
      (println "Body:" (if (string? (:body request))
                         (:body request)
                         "<Non-string body>")) ;; Prevent issues with non-string bodies
      (println "------------------------------------------")

      ;; Print response details
      (println "================ RESPONSE =================")
      (println "Status:" status)
      (println "Body:" (cond
                         (string? body) body   ;; Print plain strings as-is
                         (map? body) (with-out-str (clojure.pprint/pprint body)) ;; Pretty-print maps
                         (coll? body) (with-out-str (clojure.pprint/pprint body)) ;; Pretty-print collections
                         :else "<Non-printable body>"))
      (println "Time Taken:" duration "ms")
      (println "===========================================")

      response)))
