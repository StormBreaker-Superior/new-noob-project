(ns noob-project.noob-examples-utils)

(+ 1 2)


(def hex-words {\a \A \b \B \c \B \d \D \e \E \f \F \o \0 \s \5})

(def words `("avc" "abc" "oose" "def"))

(defn word-contains-letters [word allowed-letters]
  (every? (fn [letter]
            (contains? allowed-letters letter)) word))


(word-contains-letters "avcg" hex-words)
(word-contains-letters "adosb" hex-words)

(defn get-hex-words [words hex-letters]
  (filter
   (fn [word] (word-contains-letters word hex-letters)) words))


(defn word-to-hex-digits [words hex-letters]
  (let [hex-word (get-hex-words words hex-letters)
        hex-digit (map (fn [word]
                         (apply str "Ox" (map hex-letters word))) hex-word)]
    (println hex-word)
    (println hex-digit)))

(word-to-hex-digits words  hex-words)


;; /////////// CLOSURES \\\\\\\\\\\

(defn get-retirement-calulator [retirement-age]
  (fn [age] (- age retirement-age)))

(def gen-z-retierment (get-retirement-calulator 56))

(gen-z-retierment 66)

;; // /// // Square Root // // / / / / /


(defn near= [num1 num2 acceptable-difference]
  (<= (Math/abs (- num1 num2)) acceptable-difference))

(defn square-root-helper [start end number-to-reach]
  (let [guess (/ (+ start end) 2.0)
        guess-squared (* guess guess)]
    (println start end guess guess-squared)
    (cond
      (near= guess-squared number-to-reach 0.1)
      (do
        (println "number found")
        guess)  ; Returning the found number

      (> guess-squared number-to-reach)
      (do
        (println "going left")
        (recur start guess number-to-reach))  ; Recursive call when going left

      :else
      (do
        (println "going right")
        (recur guess end number-to-reach)))))  ; Recursive call when going right


(defn find-square-root [number]
  (square-root-helper 1 number number))

(find-square-root 25)


;; ///// / // // /////
(defn get-range [limit]
  (println " Realizing" limit)
  (lazy-seq
   (cons limit (get-range (+ limit 1)))))

(def my-range (get-range 5))

(def first-five (take 5 my-range))

(def first-ten (take 10 my-range))

(println first-five)

(println first-ten)

;; ///////// MACROS /////////// //// 

(defn get-temperature-type [temp normal-water-utils hot-water-utils boiled-water-utils ]
  (cond (<= 100 temp) normal-water-utils
        (<= 300 temp) hot-water-utils
        :else boiled-water-utils))

(defmacro get-temperature-type-macro [temp normal-water hot-water boiled-water] 
  `(let [temp# ~temp]
     (cond (<= 100 temp#) ~normal-water
           (<= 300 temp#) ~hot-water
           :else ~boiled-water)))

(get-temperature-type 33 (println "Normal Water") (println "Hot Water") (println "Boiled Water"))
(println (macroexpand-1 `(get-temperature-type-macro 33 (println "Normal Water") (println "Hot Water") (println "Boiled Water"))))

;; 12 3 