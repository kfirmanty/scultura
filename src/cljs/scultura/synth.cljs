(ns scultura.synth
  (:require [tonejs]
            [clojure.string :as s]))

(defn to-master [n]
  (.toMaster n))

(def tool-type->samples {:short-hit [1 5 6]
                         :block [1 4 5]
                         :dissonant-block [1 5 9 12]
                         :strong-long-atonal-hits [1 5 6 8]
                         :reverb-reverb [1 6 9 11]
                         :regular-multitones [1 2]
                         :multiple-rapid-glissando [1 3 5 6]
                         :two-tones-microtonal-blob [1 4 5 9 13]
                         :quiet-tiny-impulses [1 3 5 6 8]
                         :short-decay-block [1 2 3 4]
                         :glitch-electronics [1 2 3 4 10 14]})

(defn create-synth! [sample]
  (println "initializing: " sample)
  (let [synth (-> ;;(new js/Tone.Sampler (clj->js {"C3" sample}) #(println "LOADED: " sample))
               (new js/Tone.Player sample #(println "loaded: " sample))
               to-master)]
    ;; (set! (.-volume synth) -32)
    ;;(.log js/console synth)
    synth))

(defn set-master-defaults! []
  (set! (.-volume js/Tone.Master) -12)) 

(defn play-note! [synth]
  ;;(.triggerAttack synth "C3")
  (.start synth))

(defn release-note! [synth]
  (.triggerRelease synth))

(defn pad-num [num]
  (let [snum (str num)
        zeroes (- 3 (count snum))]
    (str (apply str (repeat zeroes "0")) snum)))

(defn ->proper-name [tool-type sample-num]
  (let [tool-name (s/replace (name tool-type) "-" "_")]
    (str "samples/" tool-name "/"  tool-name "_" (pad-num sample-num) ".wav.mp3")))

(defn init-synths! []
  (into {}
        (for [tool-type (keys tool-type->samples)]
          (let [samples (get tool-type->samples tool-type)]
            [tool-type (doall (map
                               create-synth! 
                               (doall (map (partial ->proper-name tool-type) samples))))]))))
