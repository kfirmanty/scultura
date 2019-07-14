(ns scultura.synth
  (:require [tonejs]
            [clojure.string :as s]))

(defn to-master [n]
  (.toMaster n))

(def tool-type->samples {:short-hit ["short_hit_001"]
                         :block ["block_01"]
                         :dissonant-block ["dissonant_block_001"]
                         :strong-long-atonal-hits ["strong_long_atonal_hits_001"]
                         :reverb-reverb ["reverb_reverb_001"]
                         :regular-multitones ["regular_multitones_001"]
                         :multiple-rapid-glissando ["multiple_rapid_glissando_001"]
                         :two-tones-microtonal-blob ["two_tones_microtonal_blob_001"]
                         :quiet-tiny-impulses ["quiet_tiny_impulses_001"]
                         :short-decay-block ["short_decay_block_001"]
                         :glitch-electronics ["glitch_electronics_001"]})

(defn create-synth! [sample]
  (println "initializing: " sample)
  (let [synth (-> ;;(new js/Tone.Sampler (clj->js {"C3" sample}) #(println "LOADED: " sample))
               (new js/Tone.Player sample #(println "loaded: " sample))
               to-master)]
    ;; (set! (.-volume synth) -32)
    (.log js/console synth)
    synth))

(defn set-master-defaults! []
  (set! (.-volume js/Tone.Master) -12)) 

(defn play-note! [synth]
  ;;(.triggerAttack synth "C3")
  (.start synth))

(defn release-note! [synth]
  (.triggerRelease synth))

(defn init-synths! []
  (into {}
        (for [tool-type (keys tool-type->samples)]
          [tool-type (create-synth! (str "samples/" (s/replace (name tool-type) "-" "_") "/" (-> (get tool-type->samples tool-type) first) ".wav.mp3"))])))
