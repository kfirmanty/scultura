(ns scultura.views
  (:require [re-frame.core :as re-frame]
            [scultura.subs :as subs]
            [scultura.events :as events]
            [clojure.string :as s]
            [scultura.toolbox :as t]
            [tonejs]))

(def tool-type->draw-fn
  (array-map :short-hit (t/rect 2 2)
             :block (t/rect 1.5 1.5)
             :dissonant-block (t/rect-cluster)
             :strong-long-atonal-hits (t/rect-bars)
             :reverb-reverb (t/reverb-reverb)
             :regular-multitones (t/regular-multitones)
             :multiple-rapid-glissando (t/multiple-rapid-glissando)
             :two-tones-microtonal-blob (t/two-tones-microtonal-blob)
             :quiet-tiny-impulses (t/quiet-tiny-impulses)
             :short-decay-block (t/short-decay-block)
             :glitch-electronics (t/glitch-electronics)
             :eraser (t/eraser)))

(defn toolbox-entry [tool-type]
  (let [width 25
        height 25
        draw-fn (tool-type->draw-fn tool-type)]
    ^{:key tool-type} [:div {:style {:border-style :solid
                                     ;;:border-width :thin
                                     :display :inline-block}
                             :on-click #(re-frame/dispatch [::events/set-tool tool-type])}
                       [:svg {:width width :height height}
                        (draw-fn width height)]]))

(defn toolbox []
  (let [per-column 2
        toolbox-entries (partition-all per-column (keys tool-type->draw-fn))]
    [:div {:style {:display "flex"
                   :flex-direction "column"}}
     (for [entries toolbox-entries]
       ^{:key entries} [:div {:style {:display "flex"
                                      :flex-direction "row"
                                      :flex-wrap "wrap"}}
                        (map toolbox-entry entries)])]))

(defn attach-on-click [el on-click]
  [:g {:on-click on-click} el])

(defn canvas []
  (let [width 500
        height 500
        element-w 100
        element-h 100
        x-off (/ element-w 2)
        y-off (/ element-h 2)
        current-tool @(re-frame/subscribe [::subs/tool])
        elements @(re-frame/subscribe [::subs/elements])]
    [:div {:style {:flex 7}}
     [:svg {:width width :height height
            :id "paint"
            :on-click (fn [e]
                        (when (not= current-tool :eraser)
                          (let [s (.getElementById js/document "paint")
                                c (.getBoundingClientRect s)
                                left (.-left c)
                                top (.-top c)
                                x (- (.-pageX e) left)
                                y (- (.-pageY e) top)]
                            (re-frame/dispatch [::events/canvas-click x y]))))}
      [:rect {:width width :height height :style {:fill-opacity 0 :stroke-opacity 1 :stroke-width 2 :stroke "rgb(0,0,0)"}}]
      (for [{:keys [x y tool id]} elements]
        (let [x (- x x-off)
              y (- y y-off)]
          (attach-on-click ((tool-type->draw-fn tool) element-w element-h x y)
                           #(when (= current-tool :eraser)
                              (re-frame/dispatch [::events/remove-element id])))))]]))

(defn main-panel []
  (let [   
        playing? @(re-frame/subscribe [::subs/playing?])
        tone-js-initialized?  @(re-frame/subscribe [::subs/tone-js-initialized?])]
    [:article
     [:h1 "Musical Paint"]
     [:p {:class "subtitle"} "based on composition by Bogusław Schaeffer"]
     [:div {:style {:display "flex"
                    :flex-direction "row"}}
      [toolbox {:style {:flex 3}}]
      [canvas]]
     [:br]
     [:div {:on-click #(do (when-not tone-js-initialized?
                             (println "initializing tonejs context")
                             (.resume js/Tone.context))
                           (re-frame/dispatch (if playing?
                                                [::events/stop!]
                                                [::events/play!])))
            :style {:border-style :solid
                    :border-width :thin
                    :display :inline-block}}
      (if playing?
        "Stop playing"
        "Start playing")]]))
