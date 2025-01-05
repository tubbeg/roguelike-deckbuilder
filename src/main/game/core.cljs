(ns game.core
  (:require ["./sceneExtension.js" :as xt]
            ["phaser" :as phaser]
            [game.config :as conf]
            [brute.entity :as ent]
            [brute.system :as sys]))


(def game-system
  (-> (ent/create-system)
      atom))

(def default-asset-path "./assets/CARDS/")

(defn load-image! [this id path]
  (.. this -load (image id path)))

(def default-suit [2 3 4 5 6 7 8 9 10 :jack :queen :king :ace])

(def default-52-deck
  {:diamonds default-suit
   :spades default-suit
   :hearts default-suit
   :clubs default-suit})

(defn suit-to-str-path [suit]
  (case suit
    :diamonds "Diamonds"
    :spades "Spades"
    :clubs "Clubs"
    "Hearts"))

(defn r-number? [n]
  (and (>= n 2)
       (<= n 10)))

(defn rank-to-str-path [rank]
  (if (r-number? rank)
    (str rank)
    (case rank
      :ace "ACE"
      :king "K"
      :queen "Q"
      "J")))

(defn rank-suit-to-path [rank suit]
  (let [s (suit-to-str-path suit)
        r (rank-to-str-path rank)]
    (str default-asset-path s "_" r ".png")))
 
(defn load-suit! [this suit deck]
  (doseq [rank (suit deck)]
    (let [p (rank-suit-to-path rank suit)
          id (str suit rank)] 
      (load-image! this id p))))

(defn load-deck! [this]
  (load-suit! this :diamonds default-52-deck)
  (load-suit! this :spades default-52-deck)
  (load-suit! this :clubs default-52-deck)
  (load-suit! this :hearts default-52-deck))

(defn p [this]
  (println "PRELOADING")
  (load-deck! this))

(defn add-image! [this id x y]
  (.. this -add (image x y id)))

(defn c [this]
  (add-image! this (str :clubs 8) 400 400))

(defn update-world [this time delta]
  (let [sw #(swap! game-system (fn [_] %))]
   (-> game-system
       deref
       (sys/process-one-game-tick delta)
       sw)))


(defn launch-game []
  (let [auto phaser/AUTO
        scene-conf nil
        s (new xt/SceneExt scene-conf p c update-world)
        conf (conf/create-config s auto)]
    (new phaser/Game conf)))


(defn init-fn []
  (println "hello hello hello")
  (launch-game))

