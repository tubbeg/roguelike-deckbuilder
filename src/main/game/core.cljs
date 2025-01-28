(ns game.core
  (:require ["./sceneExtension.js" :as xt]
            ["phaser" :as phaser]
            [game.config :as conf]
            [brute.entity :as ent]
            [brute.system :as sys]
            [game.load :as preload]
            [game.create.system :as cs]))


(def game-system
  (-> (ent/create-system)
      atom))

(defn p []
  (this-as this
   (println "PRELOADING")
   (preload/load-deck! this)))

(defn update-world-atom! [system ]
  (swap! game-system (fn [_] system)))

(defn update-world [time delta]
   (-> game-system
       deref
       (sys/process-one-game-tick delta)
       update-world-atom!))

(defn create []
  (this-as this (cs/create-world this game-system)))

(defn launch-game []
  (let [auto phaser/AUTO 
        scene-conf nil
        c create
        u update-world
        s (new xt/SceneExt scene-conf p c u)
        conf (conf/create-config s auto)]
    (new phaser/Game conf)))


(defn init-fn []
  (println "hello hello hello")
  (launch-game))

