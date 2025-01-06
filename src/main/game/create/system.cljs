(ns game.create.system
  (:require [brute.system :as sys]
            [game.systems.draw-cards :as draw]
            [game.create.card-entity :as ce]))

(defn add-systems [system]
  (-> system
      (sys/add-system-fn draw/draw-cards)))


(defn update-world! [system atm]
  (swap! atm (fn [_] system)))

(defn create-world [this system-atom]
  (-> system-atom
      deref
      (ce/create-card-entities this)
      add-systems
      (update-world! system-atom)))
