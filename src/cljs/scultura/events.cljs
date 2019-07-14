(ns scultura.events
  (:require
   [re-frame.core :as re-frame]
   [scultura.db :as db]
   [day8.re-frame.tracing :refer-macros [fn-traced defn-traced]]
   [scultura.synth :as synth]))

(re-frame/reg-event-fx
 ::initialize-db
 (fn-traced [_ _]
            {:db (merge db/default-db {:synth (synth/init-synths!)})}))

(re-frame/reg-event-db
 ::set-tool
 (fn [db [_ tool]]
   (db/set-tool db tool)))

(re-frame/reg-event-db
 ::canvas-click
 (fn [db [_ x y]]
   (db/add-element db {:x x :y y :tool (db/tool db) :synth (db/synth-for-tool db (db/tool db)) :id (random-uuid)})))

(re-frame/reg-event-db
 ::remove-element
 (fn [db [_ id]]
   (db/remove-element db id)))

(re-frame/reg-event-fx
 ::play!
 (fn [{:keys [db]} _]
   {:db (-> db (assoc :playing? true)
            db/set-tone-js-initialized)
    :dispatch-later [{:ms 0 :dispatch [::tick! 0]}]}))

(re-frame/reg-event-db
 ::stop!
 (fn [db _]
   (assoc db :playing? false)))

(re-frame/reg-event-fx
 ::tick!
 (fn [{:keys [db]} [_ time]]
   (let [elements (db/elements db)
         steps-per-tick (db/steps-per-tick db)
         to-play (filter #(and (>= (int (:y %)) time)
                               (< (int (:y %)) (+ time steps-per-tick))) elements)]
     (if (db/playing? db)
       (do
         (doseq [el to-play]
           (println "triggering" (:tool el))
           (synth/play-note! (:synth el)))
         {:db db
          :dispatch-later [{:ms 250
                            :dispatch [::tick! (mod (+ time steps-per-tick) 500)]}]})
       {:db  db}))))
