(ns game.core
  (:require ["./sceneExtension.js" :as xt]
            ["phaser" :as phaser]
            [game.config :as conf]
            [brute.entity :as ent]
            [brute.system :as sys]
            [game.load :as ld]
            [game.components :as c]
            [game.create.card-entity :as ce]))


(def game-system
  (-> (ent/create-system)
      atom))

(defn p [this]
  (println "PRELOADING")
  (ld/load-deck! this))

(defn update-world [this time delta]
  (let [sw! #(swap! game-system (fn [_] %))]
   (-> game-system
       deref
       (sys/process-one-game-tick delta)
       sw!)))

(defn launch-game []
  (let [auto phaser/AUTO
        scene-conf nil
        create #(ce/create-world % game-system)
        s (new xt/SceneExt scene-conf p create update-world)
        conf (conf/create-config s auto)]
    (new phaser/Game conf)))


(defn init-fn []
  (println "hello hello hello")
  (launch-game))

