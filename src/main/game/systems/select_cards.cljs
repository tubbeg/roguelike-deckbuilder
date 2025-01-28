(ns game.systems.select-cards
  (:require
   [brute.entity :as bent]
   [game.components :as c]))


(defn get-sprite-ents [system]
  (-> system
      (bent/get-all-entities-with-component c/SpriteComp)))

(defn get-sprite [system entity]
  (-> system (bent/get-component entity c/SpriteComp) :sprite))

(defn is-clicked? [sprite]
   (. sprite (getData "is-clicked?")))

(defn reset-clicked! [sprite]
  (. sprite (setData "is-clicked?" false)))

(defn set-glow! [sprite]
  (.. sprite -postFX (addGlow)))

(defn clear-glow! [sprite]
  (.. sprite -postFX (clear)))

(defn has-sel-comp? [system entity]
  (let [selcomp (bent/get-component system entity c/SelComp)]
    (-> selcomp nil? not)))

(defn has-no-deck-comp? [system entity]
  (let [d (-> system (bent/get-component entity c/DeckComp))]
    (nil? d)))

(defn clicked-unsel? [system entity]
  (let [s (get-sprite system entity)]
    (and (is-clicked? s)
         (not (has-sel-comp? system entity))
         (has-no-deck-comp? system entity))))

(defn clicked-sel? [system entity]
  (let [s (get-sprite system entity)]
    (and (is-clicked? s)
         (has-sel-comp? system entity)
         (has-no-deck-comp? system entity))))

(defn add-sel-comp [system entity]
  (let [s (get-sprite system entity)
        cmp (c/->SelComp)]
    (if (clicked-unsel? system entity)
      (do
        (reset-clicked! s)
        (set-glow! s)
        (-> system (bent/add-component entity cmp)))
      system)))


(defn remove-sel-entity [system entity] 
  (let [s c/SelComp
        instance (-> system (bent/get-component entity s))]
    (bent/remove-component system entity instance)))


(defn remove-sel-comp [system entity]
  (let [s (get-sprite system entity)]
    (if (clicked-sel? system entity)
      (do
        (clear-glow! s)
        (reset-clicked! s)
        (remove-sel-entity system entity))
      system)))

(defn remove-sel [system delta]
  (let [ents (get-sprite-ents system)
        f #(remove-sel-comp %1 %2)]
    (reduce f system ents)))

(defn add-sel [system delta]
  (let [ents (get-sprite-ents system)
        f #(add-sel-comp %1 %2)]
    (reduce f system ents)))

