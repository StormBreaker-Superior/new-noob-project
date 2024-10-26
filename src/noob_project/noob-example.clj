(ns noob-project.noob-example)

(+ 1 2)

(print "Hello")

(println "Enter your name : ")
(def Name "Chetan")

(println "Enter your age : ")
(def age 12)

(println Name "Your age is" age)

(/ 22.0 7.0)

(quot 22.0 7.0)

(rem 23 7)

(def my-list `(1 :2 3 4 5))
(def my-vector [1 2 3 4 5])
(def my-map {"one" 1 ,"two" 2 ,"three" 3})
(def my-set #{1 2 3 4 5 6})

(println my-list)
(println my-vector)
(println my-map)
(println my-set)

(cons my-list `(1))
(conj my-list `(1) `(2))

(count (cons my-list `(1)))

(get my-map 2 "non-found")

(my-map "two")
