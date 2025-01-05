(ns game.config)



(defn create-config [scenes type]
  (let [gravity #js {:y 200}
        arcade #js {:gravity gravity}]
    #js {:type type
         :width 800
         :height 600
         :scene scenes
         :physics #js {:default "arcade"
                       :arcade arcade}}))
