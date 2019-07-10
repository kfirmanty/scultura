(ns scultura.db)

(def default-db
  {:tool :short-hit
   :elements []})

(defn add-element [db element]
  (update db :elements conj element))

(defn tool [db]
  (:tool db))

(defn set-tool [db tool]
  (assoc db :tool tool))

(defn elements [db]
  (:elements db))

(defn remove-element [db id]
  (update db :elements (fn [els] (filter #(not= (:id %) id) els))))
