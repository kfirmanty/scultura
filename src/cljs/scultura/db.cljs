(ns scultura.db)

(def default-db
  {:tool :short-hit
   :elements []
   :playing? false
   :tone-js-initialized? false
   :steps-per-tick 4})

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

(defn synth [db tool]
  (get-in db [:synth tool]))

(defn synth-for-tool [db tool]
  (let [tool-synths (synth db tool)]
    (nth tool-synths (-> tool-synths count rand-int))))

(defn playing? [db]
  (:playing? db))

(defn set-tone-js-initialized [db]
  (assoc db :tone-js-initialized? true))

(defn tone-js-initialized? [db]
  (:tone-js-initialized? db))

(defn steps-per-tick [db]
  (:steps-per-tick db))
