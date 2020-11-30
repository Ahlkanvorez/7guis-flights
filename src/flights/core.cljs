(ns flights.core
  (:require [reagent.core :as r]
            [reagent.dom :as rd]
            [flights.format :as format]
            [flights.validation :as valid]
            [flights.view :as view]))

(defn initial-state []
  (let [now (format/date (js/Date.))]
    (r/atom {:type (first valid/flight-types)
             :leave now
             :return now})))

(defn mount-root []
  (rd/render [view/flight-booker (initial-state)]
             (js/document.getElementById "app-root")))

