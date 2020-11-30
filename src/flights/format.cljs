(ns flights.format)

(defn date [d]
  (-> d js/Date. .toLocaleString (clojure.string/split #",") first))
