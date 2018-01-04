(ns chrysostom.handler
  (:require [bidi.ring :refer (make-handler)]
            [ring.util.response :as res]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.middleware.resource :refer [wrap-resource]]
            [net.cgrand.enlive-html :as html]
            [stasis.core :as stasis]
            [chrysostom.static.web :as chrysweb]))

  (def template "templates/walls")

(html/deftemplate main-template (str template "/template.html")
  [])

(defn index-handler
  [request]
  {:status 200
   :headers {"Content-type" "text/html"}
   :body (main-template)})

(def app-routes
  ["/" [["" index-handler]
        ["index.html" index-handler]]])

(def app
  (->
   (make-handler app-routes)
   (wrap-defaults
    site-defaults)
   (wrap-resource
    (chrysweb/get-pages))))
