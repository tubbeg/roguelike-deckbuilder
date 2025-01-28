(ns game.create.card-entity
  (:require [game.load :as ld]
            [brute.entity :as bentity]
            [game.components :as component]
            [game.config :as conf]))

(defn add-image! [this id x y]
  (.. this -add (image x y id)))

(def default-deck-pos
  (let [[x y] conf/def-size]
    [(/ x 1.3) (/ y 1.5)]))

(defn rand-pos []
  (let [[x y] conf/def-size]
    [(rand-int x) (rand-int y)]))

(defn add-ptr-down [sprite fn]
  (. sprite (on "pointerdown" fn)))

(defn toggle-select [ptr]
  (this-as this
   (let [e (. this (getData "entity"))
         is-clicked? (. this (getData "is-clicked?"))
         toggle (not is-clicked?)]
     ;(println "Clicked?" is-clicked?)
     (println "Entity: " e)
     (. this (setData "is-clicked?" true))
     (comment (if (not is-clicked?)
       (.. this -postFX (addGlow))
       (.. this -postFX (clear)))
     ))))

(defn add-sprite [this x y id entity]
  (let [s (.. this -add (sprite x y id))
        d #js {:draggable true}] 
    (. s (setInteractive d))
    (. s (setData "is-clicked?" false))
    (. s (setData "entity" entity)) 
    (add-ptr-down s toggle-select)
    s))

(defn create-card-entity [world this rank suit]
  (let [[x y] (rand-pos)
        id (str suit rank)
        e (bentity/create-entity)
        sprte (add-sprite this x y id e) 
        sp (component/->SpriteComp sprte)
        rnk (component/->RankComp rank)
        st (component/->SuitComp suit)
        dcomp (component/->DeckComp)]
    (-> world
        (bentity/add-entity e)
        (bentity/add-component e rnk)
        (bentity/add-component e st)
        (bentity/add-component e dcomp)
        (bentity/add-component e sp))))

(defn create-ents-suit [system this suit]
  (let [f #(create-card-entity %1 this %2 suit)]
    (->> (suit ld/default-52-deck)
         (reduce f system)))) ; reduce <3

(defn create-card-entities [system this]
  (-> system
      (create-ents-suit this :spades)
      (create-ents-suit this :clubs)
      (create-ents-suit this :diamonds)
      (create-ents-suit this :hearts)))
