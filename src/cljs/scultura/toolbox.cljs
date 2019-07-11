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

(def rect-cluster-points
  (for [i (range 5)]
    [:rect {:width (rand)
            :height (rand)
            :x (rand)
            :y (rand)
            :style style}]))

(defn rect-cluster []
  (fn draw
    ([width height]
     (draw width height 0 0))
    ([width height x-off y-off]
     (map (fn [[t m]]
            [t (-> m
                   (assoc :transform (->transform x-off y-off))
                   (update :width * width)
                   (update :height * height)
                   (update :x * width)
                   (update :y * height))]) rect-cluster-points))))

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
           rows 3
           hrow (/ height rows)
           hrow2 (/ hrow 2)]
       (for [r (range rows)]
         (for [i (range bars)]
           (let [x (* i ws)]
             [:line {:x1 x :x2 x
                     :y1 (* r hrow) :y2 (+ (* r hrow) hrow2)
                     :style style :transform (->transform x-off y-off)}])))))))

(defn two-tones-microtonal-blob []
    (fn draw
    ([width height]
     (draw width height 0 0))
    ([width height x-off y-off]
     (let [bars 10
           ws (/ width bars)
           hw (/ width 2)
           hh (/ height 2)]
       (for [i (range bars)]
         (let [x (* i ws)
               dist (Math/abs (- x hw))
               len (- hh dist)]
           [:line {:x1 x :x2 x
                   :y1 (+ hh len) :y2 (- hh len)
                   :style style :transform (->transform x-off y-off)}]))))))

(def quiet-tiny-impulses-points
  (let [circles 5]
    (apply concat (for [c (range circles)]
                    (let [per-circle (- 50 (* c 10))]
                      (for [i (range per-circle)]
                        (let [angle (-> (/ Math/PI per-circle) (* i) (* 2))
                              r (- 0.5 (/ c circles 2))
                              x (-> angle Math/cos (* r))
                              y (-> angle Math/sin (* r))]
                          [:rect {:x (+ 0.5 x)
                                  :y (+ 0.5 y)
                                  :width 1
                                  :height 1
                                  :style style}])))))))

(defn quiet-tiny-impulses []
  (fn draw
    ([width height]
     (draw width height 0 0))
    ([width height x-off y-off]
     (map (fn [[t m]]
            [t (-> m
                   (assoc :transform (->transform x-off y-off))
                   (update :x * width)
                   (update :y * height))]) quiet-tiny-impulses-points))))

(defn short-decay-block []
  (fn draw
    ([width height]
     (draw width height 0 0))
    ([width height x-off y-off]
     (let [ww (/ width 2)
           hh (/ height 2)
           p (->polygon-string [[0 0] [ww hh] [width 0]])]
       [:polygon {:points p :style style :transform (->transform x-off y-off)}]))))

(defn glitch-electronics []
  (fn draw
    ([width height]
     (draw width height 0 0))
    ([width height x-off y-off]
     (let [hw (/ width 2)
           hh (/ height 2)
           div-x 2
           bars 3
           step (/ height bars)
           yh (/ step 2)]
       (cons [:rect {:width hw
                     :height height
                     :x (- hw (/ hw div-x))
                     :y 0
                     :style style
                     :transform (->transform x-off y-off)}]
             (for [i (range bars)]
               (let [y (+ (* step i) (/ yh 2))]
                 [:line {:x1 hw :y1 y
                         :x2 hw :y2 (+ y yh)
                         :style style
                         :transform (->transform x-off y-off)}])))))))

(defn eraser []
  (fn [width height]
    [:text {:y 10 :style {:font-size "11px"}} "eraser"]))
