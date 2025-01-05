(ns game.load)


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