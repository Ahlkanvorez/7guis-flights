(ns flights.view
  (:require [flights.format :as format]
            [flights.validation :as valid]))

(defn display-flight-booking [flight]
  (js/alert (str "You have booked a " (:type flight)
                 (when (valid/return-flight? flight)
                   (str " on " (:return flight)
                        " originally departing"))
                 " on " (:leave flight))))

(defn flight-type-selector [current]
  [:select {:value (:type @current)
            :on-change #(swap! current assoc :type (.. % -target -value))}
   (for [[idx type] (map-indexed vector valid/flight-types)]
     [:option {:key idx} type])])

(defn date-picker [{:keys [current title prop valid?]}]
  (let [disabled (and (= prop :return) (not (valid/return-flight? @current)))
        valid (valid? (prop @current))]
    [:div {:style {:display :flex}}
     [:label {:style {:flex "1 0" :margin-left "5px" :margin-right "5px"}}
      title]
     [:input {:style (when (and (not disabled) (not valid))
                      {:background-color :red})
             :type :text
             :required true
             :disabled disabled
             :title title
             :value (prop @current)
             :on-change #(swap! current assoc prop (.. % -target -value))}]]))

(defn flight-booker [current]
  [:div {:style {:display :flex :justify-content :center}}
   [:form {:style {:display :flex :flex-direction :column}}
    [flight-type-selector current]
    [date-picker {:current current
                  :title "Departure"
                  :prop :leave
                  :valid? valid/date?}]
    [date-picker {:current current
                  :title "Return"
                  :prop :return
                  :valid? #(valid/return-date? (:leave @current) %)}]
    [:input {:type :submit
             :value "Book"
             :disabled (not (valid/flight? @current))
             :on-click (fn [e]
                          (.preventDefault e)
                          (display-flight-booking @current))}]]])
