(ns groceries-list-planner.planning)

;; Vector of keywords representing the days of the week.
;; Used by collect-week-plan to know for which days it should ask the user for a meal.
(def week-days
  [:monday :tuesday :wednesday :thursday :friday :saturday :sunday])