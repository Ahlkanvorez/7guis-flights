(ns flights.validation
  (:require [flights.format :as format]))

(def two-date-flights #{"return flight"})
(def one-date-flights #{"one-way flight"})
(def flight-types (into (sorted-set)
                        (concat two-date-flights one-date-flights)))

(defn return-flight? [flight]
  (-> flight :type two-date-flights))

(defn date? [d]
  (try (= d (format/date d))
       (catch :default _ false)))

(defn date-pair? [leave return]
  (and (date? leave) (date? return)
       (< (js/Date. leave) (js/Date. return))))

(defn return-date? [leave return]
  (and (date? return)
       (or (not (date? leave))
           (date-pair? leave return))))

(defn flight? [flight]
  (or (and (not (return-flight? flight))
           (date? (:leave flight)))
      (and (date-pair? (:leave flight) (:return flight)))))

