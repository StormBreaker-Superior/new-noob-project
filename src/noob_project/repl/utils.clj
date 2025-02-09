(ns noob-project.repl.utils)

(defn get-methods-for-objects [obj]
  (map #(.getName %)) (-> obj class .getMethods))