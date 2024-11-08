(ns noob-project.handlers
  (:require [clojure.pprint :as pprint]
            [noob-project.utils :as utils]
            [monger.collection :as mc]
            [noob-project.db :as db]
            [ring.util.response :as response]))

(defn home [request]
  (let [existingSections (mc/find-maps (db/get-db) "sections" {} {:sectionName 1})
        existingSectionsWithNumber (map-indexed (fn [idx document] (str (+ idx 1) ". " (:sectionName document))) existingSections)
        sectionCount (count existingSections)
        sectionText (clojure.string/join "\n" existingSectionsWithNumber)]

    (pprint/pprint existingSectionsWithNumber)
    (if (empty? existingSectionsWithNumber)
      (response/response "Welcome to HomePage! No sections Added")
      (response/response (str "Welcome to HomePage!" "\n\n" sectionCount " sections are added" "\n" sectionText)))))

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


(defn get-tasks [request]
  (try
    (let [body (:params request)
          sectionName (:sectionId body)
          taskCollection "tasks"
          sectionExists (db/data-exists? "sections" {:sectionName sectionName})]

      (if sectionExists
        (let [tasksDocumentsMap (db/get-data taskCollection {:sectionName sectionName} {:taskName 1 :taskDescription 1})
              taskMapWithFormattedString (map-indexed
                                          (fn [idx document]
                                            (str (+ idx 1) ". " (:taskName document) " => " (get document :taskDescription "Task Not Opened Yet!!"))) tasksDocumentsMap)
              tasksText (clojure.string/join "\n" taskMapWithFormattedString)]

          (if (empty? tasksText)
            (response/response (str "No Task Added for section " sectionName))
            (response/response (str (count tasksDocumentsMap) " Tasks added for section " sectionName  "\n\n" tasksText))))

        (-> (response/response "Section Not Found")
            (response/status 404))))
    (catch Exception e
      (->
       (response/response "Internal Server Error")
       (response/status 500)))))


(defn task [request]
  (let [sectionId (utils/get-key-from-request request :sectionId)
        taskId (utils/get-key-from-request request :taskId)]
    (str "Showing description for " taskId " in section " sectionId)))

(defn create-task [request]
  (try
    (let [body (:params request)
          sectionName (:sectionId body)
          taskName (:taskName body)
          taskDescription (:taskDescription body)
          collection "tasks"
          data {:sectionName sectionName :taskName taskName :taskDescription taskDescription}]

      (pprint/pprint (str "body" body "\n" "Task creation requested for" data))
      (if
       (and (not-empty sectionName) (not-empty taskName))
          ;; TODO: if section doesn't exist, then what should be the behaviour
           ;; (1) fail the request 
           ;; (2) create section
       (if (db/data-exists? "sections" {:sectionName sectionName})
         (let [insertResult (db/insert-data collection data)]
           (if insertResult
                 ;; TODO: allow only string value in taskName
             (-> (response/response "Task Added Successfully")
                 (response/status 201))
             (-> (response/response "Internal Server Error.Db insertion failed")
                 (response/status 501))))
         (-> (response/response (str "section \"" sectionName "\" doesn't exist"))
             (response/status 404)))
       (-> (response/response "Bad Request. No sectionId or taskName passed")
           (response/status 400))))
    (catch Exception e
      (-> (response/response "Internal Server Error")
          (response/status 500)))))

(defn delete-task [request]
  (try
    (let [collection "tasks"
          body (:params request)
          taskName (:taskId body)
          taskExists (db/data-exists? collection {:taskName taskName})]
      (println "body" body " taskName " taskName) 
      (if taskExists
        (let [result (db/delete-data collection {:taskName taskName})]
          (if result
            (-> (response/response "Task Deleted Successfully"))
            (-> (response/response "Internal Server Error")
                (response/status 501))))
        (-> (response/response "Task Not Found")
            (response/status 404))))
    (catch Exception e
      (pprint/pprint (.printStackTrace e))
      (-> (response/response "Internal Server Error")
          (response/status 500)))))


(defn delete-section [request]
  (try
    (let [sectionsCollection "sections"
          taskCollection "tasks"
          body (:params request)
          sectionName (:sectionId body)
          sectionExist (db/data-exists? sectionsCollection {:sectionName sectionName})]
      (if sectionExist
        (let [sectionsResult (db/delete-data sectionsCollection {:sectionName sectionName})
              tasksResult (db/delete-data taskCollection {:sectionName sectionName})]
          (if sectionsResult
            ;; TODO : Handle eventual consistency or tasks deletion failures
            (-> (response/response "Task Deleted Successsfully"))
            (-> (response/response "Internal Server Error")
                (response/status 501))))
        (-> (response/response "Section Not Found")
            (response/status 404))))
    (catch Exception e
      (-> (response/response "Internal Server Error")
          (response/status 500)))))

(defn update-task [request]
  (try
    (let [collection "tasks"
          body (:params request)
          sectionName (:sectionId body)
          taskName (:taskId body)
          taskExist (db/data-exists? collection {:sectionName sectionName :taskName taskName})]
      (pprint/pprint body)
      (if taskExist
        (let [result (db/update-data collection {:taskName taskName} (dissoc body :sectionId :taskId))]
          (if result
            (-> (response/response "Task Updated Successsfully"))
            (-> (response/response "Internal Server Error")
                (response/status 501))))
        (-> (response/response "Task Not Found")
            (response/status 404))))
    (catch Exception e
      (-> (response/response "Internal Server Error")
          (response/status 500)))))