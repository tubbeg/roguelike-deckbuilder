(ns game.create.system
  (:require [brute.system :as sys]
            [game.systems.draw-cards :as draw]
            [game.create.card-entity :as ce]
            [game.systems.position-cards :as pc] 
            ;[game.systems.drag-cards :as dragc]
            [game.systems.select-cards :as sel]
            ))



(defn add-systems [system]
  (-> system
      (sys/add-system-fn sel/remove-sel)
      (sys/add-system-fn sel/add-sel)
      (sys/add-system-fn draw/draw-cards)
      (sys/add-system-fn pc/position-cards!)))


(defn update-world! [system atm]
  (swap! atm (fn [_] system)))

(defn create-world [this system-atom]
  ;(dragc/add-drag-fn! this)
    (-> system-atom
        deref
        (ce/create-card-entities this)
        add-systems
        (update-world! system-atom)))

