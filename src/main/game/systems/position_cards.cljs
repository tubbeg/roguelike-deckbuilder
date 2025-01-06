(ns game.systems.position-cards
  (:require [brute.entity :as bent]
            [game.components :as c]
            [game.config :as config]))


(defn get-deck-ents [s]
  (->> c/DeckComp
      (bent/get-all-entities-with-component s)))

(defn get-hand-ents [s]
  (->> c/HandComp
      (bent/get-all-entities-with-component s)))

(defn has-drag-comp? [s e]
  (let [dc (bent/get-component s e c/DragComp)]
    (not= dc nil)))

(defn has-no-drag-comp? [s e]
  (-> s (has-drag-comp? e) not))

(def default-deck-pos
  (let [[x y] config/def-size]
    [(/ x 1.1) (/ y 1.7)]))

(def default-hand-pos
  (let [[x y] config/def-size]
    [(/ x 1.8) (/ y 1.2)]))

(defn get-sprite-component [s e]
  (bent/get-component s e c/SpriteComp))

(defn log-entity-hand [s e]
  (let [d (bent/get-component s e c/HandComp)]
    (when (not= d nil)
      (println "Entity has hand component!"))))

(defn log-entity-deck [s e]
  (let [d (bent/get-component s e c/DeckComp)]
    (when (not= d nil)
      (println "Entity has deck component!"))))

; change to tweens later
(defn move-sprite! [sprite [x y]]
  (. sprite (setX x))
  (. sprite (setY y)))

(defn move-entity! [s e pos]
  (let [spr (-> s (get-sprite-component e) :sprite)]
    (move-sprite! spr pos)))

(defn position-deck-cards! [s]
  (doseq [entity (get-deck-ents s)]
    ;(log-entity-hand s entity)
    (move-entity! s entity default-deck-pos)))

(defn get-hand-comp [s e]
  (bent/get-component s e c/HandComp))

(defn order-to-pos [system entity]
  (let [oc (get-hand-comp system entity) 
        o (:order oc)
        mult (-> o (+ 1) (* 50))
        [x y] default-hand-pos]
        ;(println "Order: " o) 
    [(+ mult x) y]))

(defn position-hand-cards! [s]
  (doseq [entity (get-hand-ents s)]
    ;(log-entity-deck s entity)
    (let [pos (order-to-pos s entity)]
      (when (has-no-drag-comp? s entity) 
        (move-entity! s entity pos)))))

(defn position-cards! [system delta]
  (position-deck-cards! system)
  (position-hand-cards! system)
  system)