(ns scultura.toolbox)

(def style {:fill-opacity 0 :stroke "rgb(0,0,0)" :stroke-opacity 1})

(defn ->transform [x y]
  (str "translate(" x " " y ")"))

(defn rect
  ([]
   (rect 2 2))
  ([div-x div-y]
   (fn draw
     ([width height]
      (draw width height 0 0))
     ([width height x-off y-off]
      (let [hw (/ width 2)
            hh (/ height 2)]
        [:rect {:width (/ width div-x)
                :height (/ height div-y)
                :x (- hw (/ hw div-x))
                :y (- hh (/ hh div-y))
                :style style
                :transform (->transform x-off y-off)}])))))

(defn rect-cluster []
  (fn draw
    ([width height]
     (draw width height 0 0))
    ([width height x-off y-off]
     (for [i (range 5)]
       [:rect {:width (rand width)
               :height (rand height)
               :x (rand width)
               :y (rand height)
               :style style
               :transform (->transform x-off y-off)}]))))

(defn rect-bars []
  (fn draw
    ([width height]
     (draw width height 0 0))
    ([width height x-off y-off]
     (let [hw (/ width 2)
           hh (/ height 2)
           div-y 1.1
           div-x 2
           y (- hh (/ hh div-y))
           rh (/ height div-y)
           bars 6]
       (cons [:rect {:width hw
                     :height rh
                     :x (- hw (/ hw div-x))
                     :y y
                     :style style
                     :transform (->transform x-off y-off)}]
             (for [i (range bars)]
               (let [x (+ (- hw (/ hw div-x)) (* i (/ hw bars)))]
                 [:line {:x1 x :y1 y :x2 x :y2 (+ y rh)
                         :style style :transform (->transform x-off y-off)}])))))))

(defn reverb-reverb []
  (fn draw
    ([width height]
     (draw width height 0 0))
    ([width height x-off y-off]
     (let [lines-num 8
           lh (/ height (* 2 lines-num))
           x (/ width 2)]
       (for [i (range lines-num)]
         (let [y (* i (* 2 lh))]
           [:line {:x1 x :x2 x :y1 y :y2 (+ y lh)
                   :style style :transform (->transform x-off y-off)}]))))))

(defn ->polygon-string [p]
  (->> p (map (partial clojure.string/join ",")) (clojure.string/join " ")))

(defn regular-multitones []
  (fn draw
    ([width height]
     (draw width height 0 0))
    ([width height x-off y-off]
     (let [w3 (/ width 3)
           w6 (/ w3 2)
           p1 (->polygon-string [[0 height] [w6 0] [w3 height]])
           p2 (->polygon-string [[(* 2 w3) 0] [(+ (* 2 w3) w6) height] [width 0]])]
       (for [p [p1 p2]]
         [:polygon {:points p :style style :transform (->transform x-off y-off)}])))))

(defn multiple-rapid-glissando []
    (fn draw
    ([width height]
     (draw width height 0 0))
    ([width height x-off y-off]
     (let [bars 10
           ws (/ width bars)
           ws2 (/ ws 2)
           rows 3
           hrow (/ height rows)
           hrow2 (/ hrow 2)]
       (for [r (range rows)]
         (for [i (range bars)]
           [:line :x1 (* i ws) :x2 (* i ws) :y1 (* r hrow) :y2 (+ (* r hrow) hrow2) :style style]))))))

(defn eraser []
  (fn [width height]
    [:text {:y 10 :style {:font-size "11px"}} "eraser"]))
