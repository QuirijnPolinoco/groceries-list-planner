(ns groceries-list-planner.core
  (:require [clojure.string :as str]
            [groceries-list-planner.ingredients :as ingredients]
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

(defn meal-input [input name->id]
  (when-let [parts (and input (seq (str/split (str/trim input) #",")))]
    (let [name-part (str/trim (first parts))
          people-part (str/trim (second parts))
          recipe-id (get name->id (str/lower-case name-part))
          people (when people-part (parse-long people-part))]
      (when (and recipe-id people (pos? people))
        {:recipe recipe-id :people people}))))