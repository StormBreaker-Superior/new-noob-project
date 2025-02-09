(ns noob-project.handlers
  (:require [clojure.pprint :as pprint]
            [noob-project.utils :as utils]
            [noob-project.db.utils :as dbu]
            [ring.util.response :as response]
            [noob-project.constansts :as constants]))

(defn home [request]
  (let [existingSections (dbu/get-data constants/collection-sections {} {:sectionName 1})
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
  (try
    (let [params (:params request)
          section-name (get params :sectionName)
          category (get params :category)
          collection constants/collection-sections
          section-id (inc (dbu/get-last-task-id))
          data {:sectionName section-name :category category :sectionId section-id}]

      ;; check if request data is empty
      (if (and (not-empty section-name) (not-empty category))
        (let [result (dbu/insert-data collection data)]
          (if result
            (do
              (dbu/update-last-task-id section-id)
              (-> (response/response (str "Section " section-id " created with name: " section-name " and category: " category))
                (response/status 201)))
            (-> (response/response "Error in Inserting data")
                (response/status 500))))
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
          sectionId (Integer/parseInt (:sectionId body))
          taskCollection constants/collection-tasks
          sectionMap (dbu/get-data-map constants/collection-sections {:sectionId sectionId} {})]

      (if (not-empty sectionMap)
        (let [tasksDocumentsMap (dbu/get-data taskCollection {:sectionId sectionId} {:taskName 1 :taskDescription 1})
              taskMapWithFormattedString (map-indexed
                                          (fn [idx document]
                                            (str (+ idx 1) ". " (:taskName document) " => " (get document :taskDescription "Task Not Opened Yet!!"))) tasksDocumentsMap)
              tasksText (clojure.string/join "\n" taskMapWithFormattedString)]

          (if (empty? tasksText)
            (response/response (str "No Task Added for section " sectionId))
            (-> (response/response (str "Showing " (count tasksDocumentsMap) " tasks for section " (:sectionName sectionMap)  "\n\n" tasksText))
                (response/status 200))))

        (-> (response/response "Section Not Found")
            (response/status 404))))
    (catch Exception e
      (->
       (response/response "Internal Server Error")
       (response/status 500)))))


(defn task [request]
  (let [taskId (Integer/parseInt (utils/get-key-from-request request :taskId))
        taskData (dbu/get-data-map constants/collection-tasks {:taskId taskId} {})]
    (pprint/pprint taskData)
    (if (empty? taskData)
      (str "Task Doesn't Exist")
      (-> (response/response (str "Task Name " (:taskName taskData) "\n" "Task Description " (:taskDescription taskData)))
          (response/status 200)))))

(defn create-task [request]
  (try
    (let [body (:params request)
          sectionId (Integer/parseInt (:sectionId body))
          taskName (:taskName body)
          taskDescription (:taskDescription body)
          collection constants/collection-tasks
          task-id (inc (dbu/get-last-task-id))
          data {:sectionId sectionId :taskName taskName :taskDescription taskDescription :taskId task-id}]

      (pprint/pprint (str "Task creation requested for" data))

      (if (not-empty taskName)
          ;; TODO: if section doesn't exist, then what should be the behaviour
           ;; (1) fail the request 
           ;; (2) create section
        (if (dbu/data-exists? constants/collection-sections {:sectionId sectionId})
          (do
            (pprint/pprint "Sections Exist, creating task")
            (let [insertResult (dbu/insert-data collection data)]
              (if insertResult
                (do
                  (dbu/update-last-task-id task-id)
                  (-> (response/response (str "Task " task-id " Added Successfully"))
                      (response/status 201)))
                (-> (response/response "Internal Server Error.Db insertion failed")
                    (response/status 501)))))
          (-> (response/response (str "section \"" sectionId "\" doesn't exist"))
              (response/status 404)))
        (-> (response/response "Bad Request. No sectionId or taskName passed")
            (response/status 400))))
    (catch Exception e
      (do
        (println "Error : " (.getMessage e) (.printStackTrace e))
        (-> (response/response "Internal Server Error")
            (response/status 500))))))

(defn delete-task [request]
  (try
    (let [collection constants/collection-tasks
          body (:params request)
          taskName (:taskId body)
          taskExists (dbu/data-exists? collection {:taskName taskName})]
      (println "body" body " taskName " taskName)
      (if taskExists
        (let [result (dbu/delete-data collection {:taskName taskName})]
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
    (let [sectionsCollection constants/collection-sections
          taskCollection constants/collection-tasks
          body (:params request)
          sectionName (:sectionId body)
          sectionExist (dbu/data-exists? sectionsCollection {:sectionName sectionName})]
      (if sectionExist
        (let [sectionsResult (dbu/delete-data sectionsCollection {:sectionName sectionName})
              tasksResult (dbu/delete-data taskCollection {:sectionName sectionName})]
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
    (let [collection constants/collection-tasks
          body (:params request)
          sectionId (:sectionId body)
          taskName (:taskId body)
          taskExist (dbu/data-exists? collection {:sectionId sectionId :taskName taskName})]
      (pprint/pprint body)
      (if taskExist
        (let [result (dbu/update-data collection {:taskName taskName} (dissoc body :sectionId :taskId))]
          (if result
            (-> (response/response "Task Updated Successsfully"))
            (-> (response/response "Internal Server Error")
                (response/status 501))))
        (-> (response/response "Task Not Found")
            (response/status 404))))
    (catch Exception e
      (-> (response/response "Internal Server Error")
          (response/status 500)))))

(defn udate-section [request]
  "Update section here")