# Testing Document

Student: Quirijn van der Zanden
Studentnr: 2137625
Datum: 13 maart, 2026
Versie: 1
Docent: Michel Koolwaaij
Klas: ITA-CNI-A-f 2025

## White box testing

Voor het witdoostesten heb ik de REPL gebruikt. Door afzonderlijke functies direct in de REPL uit te voeren, kon ik het gedrag van kleine stukjes code controleren, tussenresultaten bekijken en snel verschillende invoerscenario’s proberen zonder de hele applicatie steeds opnieuw te moeten starten. Op deze manier kon ik gericht logische fouten opsporen.

### Test 1 – Genereren van de vlakke ingrediëntenlijst

- **Doel**: controleren dat `week-plan-ingredients` alle ingrediënten uit de weekplanning ophaalt.
- **Relevante functies**: `week-plan-ingredients` in `ingredients.clj`.
- **REPL-commando’s**:

```clojure
(require '[groceries-list-planner.ingredients :as ing]
         '[groceries-list-planner.recipe :as recipe]
         '[groceries-list-planner.planning :as planning])

(def flat (ing/week-plan-ingredients recipe/recipes planning/week-plan))
(count flat)   ;; in de test: 69 ingrediënten
(take 2 flat)  ;; voorbeeld van de eerste twee records
```

### Test 2 – Aggregatie van ingrediënten

- **Doel**: controleren dat dubbele ingrediënten (zelfde naam/unit/categorie) worden samengevoegd.
- **Relevante functies**: `aggregate-ingredients` in `ingredients.clj`.
- **REPL-commando’s**:

```clojure
(def flat (ing/week-plan-ingredients recipe/recipes planning/week-plan))
(def agg  (ing/aggregate-ingredients flat))

(count flat)  ;; 69 voor de vlakke lijst
(count agg)   ;; 36 na aggregatie
```

### Test 3 – Groeperen per categorie en boodschappenlijst-print

- **Doel**: controleren dat ingrediënten per supermarkt-categorie gegroepeerd en netjes geprint worden.
- **Relevante functies**: `group-by-category` en `shopping-list` in `ingredients.clj`,
  en `printing-shopping-list` in `core.clj`.
- **REPL-commando’s**:

```clojure
(def flat    (ing/week-plan-ingredients recipe/recipes planning/week-plan))
(def agg     (ing/aggregate-ingredients flat))
(def grouped (ing/group-by-category agg))

(doseq [cat (sort (keys grouped))]
  (println (str "== " (name cat) " =="))
  (doseq [i (get grouped cat)]
    (println (str "  " (:amount i) " " (name (:unit i)) " " (:name i))))
  (println))
```

Dit leverde bijvoorbeeld categorieën op zoals `canned`, `dairy`, `grains`, `meat`, `oil`, `spice` en `vegetable` met daaronder de juiste hoeveelheden (zoals 240 g green curry paste, 1000 ml coconut milk, 750 g minced beef, enzovoort).

### Test 4 – Hulpfuncties in `core.clj`

- **Doel**: controleren dat de hulpfuncties rond recepten correct werken.
- **Relevante functies**: `recipe-name->id-map` en `print-recipes` in `core.clj`.
- **REPL-commando’s**:

```clojure
(require 'groceries-list-planner.core :reload)
(require '[groceries-list-planner.recipe :as recipe])

(groceries-list-planner.core/recipe-name->id-map recipe/recipes)
(groceries-list-planner.core/print-recipes)
```

De functie `recipe-name->id-map` gaf een map terug waarin namen zoals `"meat pasta"` naar de juiste keyword-id (bijvoorbeeld `:meat-pasta`) worden vertaald. Met `print-recipes` werd gecontroleerd dat alle recepten netjes als lijst in de console werden weergegeven.

### Ruwe REPL-log (bijlage)

Zie: `.calva/repl.calva-repl` (Tests 1–3) en `Groceries-list-planner/.calva/repl.calva-repl` (Test 4 en extra runs).

## Black box testing

Voor het Black box testing heeft mijn docent het programma als eindgebruiker via de console uitgeprobeerd. Bij de vraag wat hij op een bepaalde dag wilde eten en voor hoeveel personen, typte hij Green Curry zonder de verwachte komma en het aantal personen (bijvoorbeeld `Green Curry, 3`). Dit zorgde voor een exception, omdat de invoer niet in het juiste formaat was. Op basis van deze test heb ik de invoerafhandeling aangepast, zodat dit soort foutieve invoer niet meer tot een crash leidt, maar netjes wordt afgewezen en de gebruiker een duidelijke melding krijgt.