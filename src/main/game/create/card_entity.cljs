(ns game.create.card-entity
  (:require [game.load :as ld]
            [brute.entity :as ent]
            [game.components :as c]
            [game.config :as conf]))

(defn add-image! [this id x y]
  (.. this -add (image x y id)))

(def default-deck-pos
  (let [[x y] conf/def-size]
    [(/ x 1.3) (/ y 1.5)]))

(defn rand-pos []
  (let [[x y] conf/def-size]
    [(rand-int x) (rand-int y)]))

(defn create-card-entity [system this rank suit]
  (let [entity (ent/create-entity)
        r (c/->RankComp rank)
        s (c/->SuitComp suit)
        [x y] (rand-pos)
        id (str suit rank)
        sprite (.. this -add (sprite x y id))
        sp (c/->SpriteComp sprite)]
    (-> system
        (ent/add-component entity r)
        (ent/add-component entity s)
        (ent/add-component entity sp))))

(defn create-ents-suit [system this suit]
  (let [f #(create-card-entity %1 this %2 suit)]
    (->> suit
         ld/default-52-deck
         (reduce f system))))

(defn create-card-entities [system this]
  (create-ents-suit system this :spades)
  (create-ents-suit system this :clubs)
  (create-ents-suit system this :diamonds)
  (create-ents-suit system this :hearts))

(defn create-world [this system-atom]
  (let [sw! #(swap! system-atom (fn [_] %))]
    (add-image! this (str :clubs 8) 400 400)
    (-> system-atom
        deref
        (create-card-entities this)
        sw!)))