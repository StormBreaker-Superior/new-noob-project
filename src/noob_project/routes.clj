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
