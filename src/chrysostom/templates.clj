(ns chrysostom.templates
  (:require [net.cgrand.enlive-html :as html]
            [hiccup.core :as hiccup]
            [optimus.assets :as assets]
            [chrysostom.config :as config])
  (:gen-class))

(def template "templates/walls")

;; Build top navbar with top pages

(defn get-links
  "Retrieves list of files from a docroot"
  [type]
  (let [path (config/get-file-path type)]
    (map
     #(clojure.string/replace % path "")
     (mapv str (filter #(.isFile %) (file-seq (clojure.java.io/file path)))))))

(defn remove-suffix
  [text]
  (clojure.string/replace text #"\.\S*$" ""))

(defn link-text
  "Strips any leading slash and captializes first letter."
  [link]
  (remove-suffix
   (clojure.string/capitalize
    (if (.startsWith link "/")
      (subs link 1)
      link))))

(defn get-navbar
  [depth]
  (hiccup/html
   [:ul
    (clojure.string/join
     (map
      #(hiccup/html [:li [:a {:href (remove-suffix %)} (link-text %)]])
      (filter #(= (count (clojure.string/split % #"/")) (inc depth))
              (get-links :pages))))]))

(defn get-assets []
  (concat
   (assets/load-assets "public" [#".*"])
   (assets/load-assets template [#".*"])))

(html/deftemplate main-template (str template "/template.html")
  [ctxt]
  [:div#menu] (html/html-content (get-navbar 1))
  [:div#sidebar] (html/html-content (:sidebar ctxt))
  [:div#text] (html/html-content (:text ctxt)))
