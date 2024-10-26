(ns noob-project.utils)

(defn get-key-from-request [request keyName]
  ;;Returns the value of key in requsts :param
  ;; Must send the keyword as keyName
  (let [params (:params request)
        keyValue (get params keyName)]
    (println "prams for" keyName params "keyValue" keyValue)
    keyValue))