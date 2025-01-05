(ns game.config)


(def def-size [800 600])


(defn create-config [scenes type]
  (let [gravity #js {:y 200}
        arcade #js {:gravity gravity}
        [x y] def-size]
    #js {:type type
         :width x
         :height y
         :scene scenes
         :physics #js {:default "arcade"
                       :arcade arcade}}))
