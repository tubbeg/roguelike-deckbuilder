(ns game.core
  (:require ["./sceneExtension.js" :as xt]
            ["phaser" :as phaser]))



(defn create-config [scenes]
  (let [gravity #js {:y 200}
        arcade #js {:gravity gravity}]
   #js {:type phaser/AUTO
        :width 800
        :height 600
        :scene scenes
        :physics #js {:default "arcade"
                      :arcade arcade}}))


(defn p [this]
  (println "PRELOADING"))

(defn c [this])

(defn u [this time delta])


(defn launch-game []
  (let [scene-conf nil
        s (new xt/SceneExt scene-conf p c u)
        conf (create-config s)]
    (new phaser/Game conf)))


(defn init-fn []
  (println "hello hello hello")
  (launch-game))

