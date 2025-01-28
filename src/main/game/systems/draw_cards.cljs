(ns game.systems.draw-cards
  (:require [brute.entity :as bent]
            [game.components :as c]))

(comment
  "Be extremely careful when using an alias name. If you use
   the same name as a let binding the you will experience some
   very painful bugs"
  )


(comment
  "For now, the default hand size will be 5. I will
   change this to a variable value later.")

(defn get-deck-ents [system]
  (-> system
      (bent/get-all-entities-with-component c/DeckComp)))

(def default-hand-size 5)

(defn get-hand-ents [system]
  (-> system
      (bent/get-all-entities-with-component c/HandComp)))

(defn has-full-hand? [system]
  (let [ents (get-hand-ents system)
        ;ents-deck (get-deck-ents system)
        result (>= (count ents) default-hand-size)]
    result))

(defn remove-deck-comp [system e] 
  (let [d (-> system (bent/get-component e c/DeckComp))]
    (-> system (bent/remove-component e d))))

(defn add-hand-comp [system [order entity]]
  (let [h (c/->HandComp order)]
    (bent/add-component system entity h)))

(defn remove-deck-entities [system entities]
  (let [f #(remove-deck-comp %1 %2)]
    (->> entities (reduce f system))))

(defn get-rank [system entity]
  (-> system
      (bent/get-component entity c/RankComp)
      :rank))

(defn rank-to-value [rank]
  (if (number? rank)
    rank
    (case rank
      :ace 14
      :king 13
      :queen 12
      :jack 11
      2)))

(defn compare-rank-fn [system entity]
  (-> system (get-rank entity) rank-to-value))

(defn create-order [system entities]
  (let [coll (sort-by #(compare-rank-fn system %) entities)]
    (for [i (-> coll count range)]
      [i (nth coll i)]))) 

(defn add-hand-entities [system entities] 
  (let [coll (create-order system entities)
        f #(add-hand-comp %1 %2)]
     (->> coll (reduce f system))))

(defn change-comps [system entities]
  (-> system
      (remove-deck-entities entities)
      (add-hand-entities entities)))

(defn log-entities [system]
  (let [ents (get-hand-ents system)]
    (println "NR OF HAND ENTS" (count ents))
    system))

(defn add-hand-comps [system]
  (let [mx default-hand-size
        ents (get-deck-ents system)]
    (->> ents
         shuffle
         (take mx)
         (change-comps system)
         log-entities)))

(defn draw-cards [system delta]
  (if (has-full-hand? system)
    ;(-> system log-entities)
    system
    (add-hand-comps system)))