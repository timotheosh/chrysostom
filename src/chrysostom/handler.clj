(ns chrysostom.handler
  (:require [bidi.ring :refer [make-handler]]
            [chrysostom.templates :as tmpl]
            [chrysostom.web :as web]
            [liberator.core :refer [defresource resource]]
            [liberator.representation :refer [ring-response]]
            [optimus.optimizations :as optimizations]
            [optimus.prime :as optimus]
            [optimus.strategies :refer [serve-live-assets]]
            [ring.middleware.defaults :refer [site-defaults wrap-defaults]]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.util.response :as res]))

(defn not-found-handler
  [request]
  (web/layout-page request "<h1 style=\"color: red;\">Not Found</h1>"))

"Liberator library makes things easier when we just need to send the
content of arbitrary files. We just need it to send us a handler.
TODO: Write my own macro in liberator's place. Unless, of course,
there are other things I can use from the lib."
(defresource send-page [page]
  :allowed-methods [:get]
  :available-media-types ["text/html"]
  :handle-ok page
  :handle-not-found not-found-handler)

(defn index-handler
  [request]
  (web/layout-page request "Welcome to Chrysostom!"))

(def app-routes
  [[""  (send-page index-handler)]])

(defn- gen-route
  [route]
  "Returns a route that bidi can use."
  [(let [path (first route)]
     (if [(.startsWith path "/")]
       (subs path 1)
       path))
   (let [page (second route)]
     (send-page page))])

(defn generate-routes
  []
  ["/" (conj
        (into app-routes (map #(gen-route %) (web/get-static-pages)))
        [true (send-page not-found-handler)])])

(def app
  (wrap-defaults
   (optimus/wrap
    (make-handler (generate-routes))
    tmpl/get-assets
    optimizations/all
    serve-live-assets)
   (assoc site-defaults :static false)))
