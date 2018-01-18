(ns chrysostom.templates
  (:require [net.cgrand.enlive-html :as html])
  (:gen-class))

(def template "templates/walls")

(html/deftemplate main-template (str template "/template.html")
  [ctxt]
  [:div#sidebar] (html/html-content (:sidebar ctxt))
  [:div#text] (html/html-content (:text ctxt)))
