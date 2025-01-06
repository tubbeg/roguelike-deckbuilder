(ns game.create.card-entity
  (:require [game.load :as ld]
            [brute.entity :as bentity]
            [game.components :as component]
            [game.config :as conf]))

(defrecord Mycomponent [x y])

(defn add-image! [this id x y]
  (.. this -add (image x y id)))

(def default-deck-pos
  (let [[x y] conf/def-size]
    [(/ x 1.3) (/ y 1.5)]))

(defn rand-pos []
  (let [[x y] conf/def-size]
    [(rand-int x) (rand-int y)]))

(defn log-system [system]
  (println system)
  system)

(defn create-card-entity [world this rank suit]
  (let [[x y] (rand-pos)
        id (str suit rank)
        sprte (.. this -add (sprite x y id)) 
        sp (component/->SpriteComp sprte)
        e (bentity/create-entity)
        compo (->Mycomponent 2 3)
        rnk (component/->RankComp rank)
        st (component/->SuitComp suit)
        dcomp (component/->DeckComp)]
    (-> world
        (bentity/add-entity e)
        (bentity/add-component e compo)
        (bentity/remove-component e compo)
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
