(ns noob-project.handlers
  (:require [clojure.pprint :as pprint]
            [noob-project.utils :as utils]
            [monger.collection :as mc]
            [noob-project.db :as db]
            [ring.util.response :as response]))

(defn home [request]
  "Welcome to Home Page")

(defn create-section [request]
  "" " 1. get sectionName and category from request
   2. If they are empry/null, then it is Bad Request
   3. Else insert data in db
   4. If db insertion fails then 500 : Error in inserting data
   4. Else 201 " ""
  (pprint/pprint request)
  ;; Extract the parameters from the request
  (try
    (let [params (:params request)
          section-name (get params :sectionName)
          category (get params :category)
          collection "sections"
          data {:sectionName section-name :category category}]

      ;; check if request data is empty
      (if (and (not-empty section-name) (not-empty category))
        ;; if data already exists, then fail the request with conflict status : 409
        (if (db/data-exists? collection data)
          (->
           (response/response "section already exists. Visit http://localhost:3000/ to see existing sections ")
           (response/status 409))
          ;; insert data
          (let [result (db/insert-data collection data)]
            (if result
              (-> (response/response (str "Section created with name: " section-name " and category: " category))
                  (response/status 201))
              (-> (response/response "Error in Inserting data")
                  (response/status 500)))))
        (-> (response/response (str "Either name or category is invalid" section-name category))
            (response/status 400))))
    (catch Exception e
      ;; e to used when logs are being added
      (->
       (response/response "Internal Server Error")
       (response/status 500)))))


(defn sections [request]
  ;; (println request)
  (let [sectionId (utils/get-key-from-request request :sectionId)]
    (str "Welcome to section " sectionId)))

(defn task [request]
  (let [sectionId (utils/get-key-from-request request :sectionId)
        taskId (utils/get-key-from-request request :taskId)]
    (str "Showing description for " taskId " in section " sectionId)))