(ns groceries-list-planner.core
  (:require [groceries-list-planner.ingredients :as ingredients]
            [groceries-list-planner.recipe :as recipe]
            [groceries-list-planner.planning :as planning]))

(defn printing-shopping-list [shopping-list]
  (doseq [cat (sort (keys shopping-list))]
    (println (str "== " (name cat) " =="))
    (doseq [i (get shopping-list cat)]
      (println (str "  " (:amount i) " " (name (:unit i)) " " (:name i))))
    (println)))

(defn recipe-name->id-map [recipes]
  (into {}
        (map (fn [[id recipe]]
               [(.toLowerCase ^String (:name recipe)) id])
             recipes)))

(defn print-recipes []
  (println "Recipes:")
  (doseq [[_ r] recipe/recipes]
    (println (str " - " (:name r)))))