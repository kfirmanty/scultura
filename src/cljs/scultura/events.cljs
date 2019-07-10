(ns scultura.events
  (:require
   [re-frame.core :as re-frame]
   [scultura.db :as db]
   [day8.re-frame.tracing :refer-macros [fn-traced defn-traced]]
   [scultura.synth :as synth]))

(re-frame/reg-event-fx
 ::initialize-db
 (fn-traced [_ _]
            {:db db/default-db}))

(re-frame/reg-event-db
 ::set-tool
 (fn [db [_ tool]]
   (db/set-tool db tool)))

(re-frame/reg-event-db
 ::canvas-click
 (fn [db [_ x y]]
   (db/add-element db {:x x :y y :tool (db/tool db) :id (random-uuid)})))

(re-frame/reg-event-db
 ::remove-element
 (fn [db [_ id]]
   (db/remove-element db id)))
