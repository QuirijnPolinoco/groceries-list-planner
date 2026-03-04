(ns groceries-list-planner.planning)

(def week-days
  [:monday :tuesday :wednesday :thursday :friday :saturday :sunday])

(def week-plan
  [{:day :monday    :recipe :meat-pasta     :people 2}
   {:day :tuesday   :recipe :green-curry    :people 4}
   {:day :wednesday :recipe :tacos          :people 3}
   {:day :thursday  :recipe :chicken-orzo   :people 2}
   {:day :friday    :recipe :chicken-gnocchi :people 4}
   {:day :saturday  :recipe :honey-chicken  :people 2}
   {:day :sunday    :recipe :chicken-pitas  :people 2}])