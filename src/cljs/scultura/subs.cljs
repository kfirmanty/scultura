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
