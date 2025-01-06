(ns game.systems.draw-cards
  (:require [brute.entity :as bent]
            [game.components :as c]))

(comment
  "Discovered a fairly simple error. Do not attempt to
   use ent as a binding. Basically clojure will confuse
   the binding with the alias. Extremely simple error, but
   very time consuming"
  )
(defrecord Position [x y])
(defrecord Velocity [x y])



(defn test-this [system]
  (let [entity (bent/create-entity)
        pos (->Position 5 5)
        vel (->Velocity 10 10)]
    (-> system
        (bent/add-entity entity)
        (bent/add-component entity pos)
        (bent/add-component entity vel)
        (bent/remove-component entity pos))))


(-> (bent/create-system)
    (test-this))


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
    (println "DECK INSTANCE IS" d)
    (println "ENT" e)
    (println "System" system) 
    (-> system (bent/remove-component e d))))

(defn add-hand-comp [system entity]
  (let [h (c/HandComp. "foo")]
    (bent/add-component system entity h)))

(defn remove-deck-entities [system entities]
  (let [f #(remove-deck-comp %1 %2)]
    (->> entities (reduce f system))))

(defn add-hand-entities [system entities] 
  (let [f #(add-hand-comp %1 %2)]
     (->> entities (reduce f system))))

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
    (println "NR OF DECK ENTS" (count ents))
    (->> ents
         shuffle
         (take mx)
         (change-comps system)
         log-entities)))


(defn draw-cards [system delta]
  (if (has-full-hand? system)
    system
     (add-hand-comps system)
    ))