(ns chrysostom.templates
  (:require [net.cgrand.enlive-html :as html]
            [optimus.assets :as assets])
  (:gen-class))

(def template "templates/walls")

(defn get-assets []
  (concat
   (assets/load-assets "public" [#".*"])
   (assets/load-assets template [#".*"])))

(html/deftemplate main-template (str template "/template.html")
  [ctxt]
  [:div#sidebar] (html/html-content (:sidebar ctxt))
  [:div#text] (html/html-content (:text ctxt)))
