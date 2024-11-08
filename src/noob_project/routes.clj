(ns noob-project.routes
  (:require [noob-project.handlers :as handlers]
            [compojure.core :as compojure]
            [compojure.route :as route]))

(compojure/defroutes app
  (compojure/GET "/" params handlers/home)
  (compojure/GET "/:sectionId" params handlers/get-tasks)
  (compojure/GET "/:sectionId/:taskId" params handlers/task)
  (compojure/POST "/sections" params handlers/create-section)
  (compojure/POST "/:sectionId" params handlers/create-task)
  (compojure/DELETE "/:sectionId/tasks/:taskId" params handlers/delete-task)
  (compojure/DELETE "/sections/:sectionId" params handlers/delete-section)
  (route/not-found "Page Not Found !"))