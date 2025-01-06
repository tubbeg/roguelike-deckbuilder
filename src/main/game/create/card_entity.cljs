(ns game.create.card-entity
  (:require [game.load :as ld]
            [brute.entity :as bentity]
            [game.components :as component]
            [game.config :as conf]))

(defrecord Mycomponent [x y])

(defrecord Position [x y])
(defrecord Velocity [x y])

(defn test-this [system]
  (let [entity (bentity/create-entity)
        pos (->Position 5 5)
        vel (->Velocity 10 10)]
    (-> system
        (bentity/add-entity entity)
        (bentity/add-component entity pos)
        (bentity/add-component entity vel)
        (bentity/remove-component entity pos))))

(-> (bentity/create-system) test-this)

(let [sys (bentity/create-system)
      e (bentity/create-entity)
      comp (->Mycomponent 2 3)]
  (-> sys
      (bentity/add-entity e)
      (bentity/add-component e comp)
      (bentity/remove-component e comp)))


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

; there is something seriously wrong with this
; function, but I can't figure out what it is
(defn create-card-entit2y [s this rank suit]
  (let [e (bentity/create-entity)
        r (component/->RankComp rank)
        st (component/->SuitComp suit)
        dc (component/->DeckComp )
        test-comp (component/->RankComp :stuff)
        comp2 (->Mycomponent 2 3)
        ;sprte (.. this -add (sprite x y id))
        ;sp (->SpriteComp sprte)
        ]
    (-> s
        (bentity/add-component e comp2) 
        (bentity/add-component e r)
        (bentity/add-component e st)
        ;(bentity/add-component entity sp)
        (bentity/add-component e dc))))

(-> (bentity/create-system)
    (create-card-entit2y nil :some :stuff))

; this function works just fine
; but the above function is completely broken
; for some magic reason
(defn create-card-entity [world this rank suit]
  (let [[x y] (rand-pos)
        id (str suit rank)
        sprte (.. this -add (sprite x y id)) 
        sp (component/->SpriteComp sprte)
        e (bentity/create-entity)
        comp (->Mycomponent 2 3)
        rnk (component/->RankComp rank)
        st (component/->SuitComp suit)
        dcomp (component/->DeckComp)]
    (-> world
        (bentity/add-entity e)
        (bentity/add-component e comp)
        (bentity/remove-component e comp)
        (bentity/add-component e rnk)
        (bentity/add-component e st)
        (bentity/add-component e dcomp)
        (bentity/add-component e sp)
        )))

;(-> (create-system)
;    (create-card-entity nil 8 :Diamonds))

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
