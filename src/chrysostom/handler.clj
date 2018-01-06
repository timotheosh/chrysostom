(ns chrysostom.handler
  (:require [bidi.ring :refer (make-handler)]
            [ring.util.response :as res]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.middleware.resource :refer [wrap-resource]]
            [net.cgrand.enlive-html :as html]
            [chrysostom.static.web :as chrysweb]))

(def template "templates/walls")

(html/deftemplate main-template (str template "/template.html")
  [ctxt]
  [:div#sidebar] (html/content (:sidebar ctxt)))

(defn index-handler
  [request]
  {:status 200
   :headers {"Content-type" "text/html"}
   :body (main-template {:sidebar "Greetings visitor!"})})

(def app-routes
  ["/" [["" index-handler]
        ["index.html" index-handler]
        ["about"
         [["" chrysweb/about-page]
          ["/" chrysweb/about-page]
          ["/index.html" chrysweb/about-page]]]]])

(def app
  (->
   (make-handler app-routes)
   (wrap-defaults
    site-defaults)
   (wrap-resource
    (chrysweb/get-pages))))
