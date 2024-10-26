(ns noob-project.functions)


(def get-name (fn [name] (str "Hello " name)))
;; (def name "Chetan")

(get-name name)


;; binding function with defn
(defn get-name-and-initial [first-name second-name last-name]
  (str "Hello, " first-name "#" second-name " " (nth last-name 0)))

(get-name-and-initial "Chetan " "Shankar" "Urkude")

;; using if form
(defn is-pass-or-fail? [grade] 
  ( if (> grade 75) true false))

(is-pass-or-fail? 77)

;; using cond form
(defn get-grade [grade]
  (cond (> grade 90) "A"
        (> grade 80) "B"
        (> grade 70) "C"
        :else "D"))

(get-grade 77)


;; (defn get-grade-results-using-for-loop [grades]
;;   (for [grade grades]
;;     (get-complete-grade-string grade)))

;; (defn get-grade-results [grades]
;;   (let [all-complete-grade-string (map get-complete-grade-string grades)]
;;     (apply str all-complete-grade-string)))
    

;; ( get-grade-results `(99 25 86 75 66))
;; 

 
(defn get-complete-grade-string [grade]
  (str grade " " (get-grade grade) " " (if (is-pass-or-fail? grade) "pass" "fail") "\n"))


;; final studenet report 
( defn get-student-report [] 
 ())
  ;; get full-name
  ;; or get full-name with grade
  ;; get average-marks
  



;; ////// ///// Fizz Buzz
(defn is-divisible-by-3 [num]
  (= (rem num 3) 0))

(defn is-divisible-by-5 [num]
  (= (rem num 5) 0))

(defn is-divisible-by-3-and-5 [num]
  (and (is-divisible-by-3 num) (is-divisible-by-5 num)))

(is-divisible-by-3 33)
(is-divisible-by-5 30)
(is-divisible-by-3-and-5 30)

(defn fizz-buzz [number]
  (cond (is-divisible-by-3-and-5 number) "fizz-buzz" 
        (is-divisible-by-3 number) "fizz"
        (is-divisible-by-5 number) "buzz"
        :else "number"))

(fizz-buzz 3)
(fizz-buzz 25)
(fizz-buzz 30)
(fizz-buzz 31)

(defn get-fizz-buzz-for-range [start end]
  (let [nums-in-range (range start end)]
    (for [num nums-in-range]
      (apply str num " : " (fizz-buzz num)))))

(get-fizz-buzz-for-range 29 36)

;; //// / ///// / //// create and print box object

(defn create-box [height width]
  {:height height :width width})

(defn print-box [box] 
  (str "Box( height : " (box :height) " , width : " (box :width) ")"))

(def box (create-box 4 6))

(print-box box)

(defn get-top-bottom-row-text [box]
  (let [star-character-sequence (repeat (box :width) "*")]
    (apply str star-character-sequence)))

(defn get-middle-row-text [box]
  (let [star-character-sequence (repeat (- (box :width) 2) " ")]
    (str "*" (apply str star-character-sequence) "*")))

(get-top-bottom-row-text box)
(get-middle-row-text box)

;; (defn print-box-with-stars [box] 
;;   (let [top-bottom-row (get-top-bottom-row-text box)
;;          middle-rows (get-middle-row-text box)]
;;      (println top-bottom-row) ;; top row
;;      (doseq [_ (box :height)] 
;;        (println middle-rows)) ;; middle rows
;;      (println top-bottom-row) ;; bottom row
;;      ))
  
;; (print-box-with-stars box)