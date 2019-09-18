(ns chrysostom.highlight
  (:require [clojure.java.io :as io]
            [clygments.core :as pygments]
            [net.cgrand.enlive-html :as enlive]))

(defn- extract-code
  [highlighted]
  (-> highlighted
      java.io.StringReader.
      enlive/html-resource
      (enlive/select [:pre])
      first
      :content))

(defn- highlight [node]
  (let [code (->> (first (enlive/select node [:pre :code])) :content (apply str))
        lang (->> (first (enlive/select node [:pre])) :attrs :class keyword)]
    (cond
      (= lang :example) (assoc node :content code)
      (= lang :commonlisp) (assoc node :content (-> code
                                                    (pygments/highlight :lisp :html)
                                                    extract-code))
      :else (assoc node :content (-> code
                                     (pygments/highlight lang :html)
                                     extract-code)))))

(defn get-class
  [node]
  (:attr (:class node)))

(defn highlight-code-blocks [page]
  (enlive/sniptest (clojure.string/join page)
                   [:pre] highlight
                   [:pre] #(assoc-in % [:attrs :class] "highlight")))
