(ns scultura.synth
  (:require [tonejs]))

(defn to-master [n]
  (.toMaster n))

(def tool-type->samples {:short-hit ["short_hit"]
                         :block ["block"]
                         :dissonant-block ["dissonant_block"]
                         :strong-long-atonal-hits ["strong_long_atonal_hits"]
                         :reverb-reverb ["reverb_reverb"]
                         :regular-multitones ["regular_multitones"]
                         :multiple-rapid-glissando ["multiple_rapid_glissando"]
                         :two-tones-microtonal-blob ["two_tones_microtonal_blob"]
                         :quiet-tiny-impulses ["quiet_tiny_impulses"]
                         :short-decay-block ["short_decay_block"]
                         :glitch-electronics ["glitch_electronics"]})

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
          [tool-type (create-synth! (str "samples/" (-> (get tool-type->samples tool-type) first) ".WAV"))])))
