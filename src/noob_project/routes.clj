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
    (compojure/DELETE ":sectionId" params handlers/delete-section))
  ;; (compojure/DELETE "/:sectionId/tasks/:taskId" params handlers/delete-task)
  (route/not-found "Page Not Found !"))