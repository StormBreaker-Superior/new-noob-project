(ns noob-project.routes
  (:require [noob-project.handlers :as handlers]
            [compojure.core :as compojure]
            [compojure.route :as route]))

(compojure/defroutes app
  (compojure/GET "/" params handlers/home)
  (compojure/GET "/:sectionId" params handlers/sections)
  (compojure/GET "/:sectionId/:taskId" params handlers/task)
  (route/not-found "Page Not Found"))