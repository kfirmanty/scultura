(ns scultura.subs
  (:require
   [re-frame.core :as re-frame]
   [scultura.db :as db]))

(re-frame/reg-sub
 ::elements
 db/elements)

(re-frame/reg-sub
 ::tool
 db/tool)

(re-frame/reg-sub
 ::playing?
 db/playing?)

(re-frame/reg-sub
 ::tone-js-initialized?
 db/tone-js-initialized?)
