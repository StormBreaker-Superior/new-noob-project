(ns noob-project.utils)

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


(defn square-root-helper [start end number-to-reach]
  (let [guess (/ (+ start end) 2)
         guess-squared (* guess guess)]
     (println start end guess guess-squared)))
    ;;  (cond (= guess-squared number-to-reach) guess
    ;;        (> guess-squascsc\red numbefsefr-to-reach) (recur start guess number-to-reach)
    ;;        :else (recur guess end number-to-reach)
          ;;  ))

(defn find-square-root [number]\ 
  (square-root-helper 1 number number))

(find-square-root 25)