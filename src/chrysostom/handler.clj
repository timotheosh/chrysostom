(ns chrysostom.handler
  (:require [optimus.assets :as assets]
            [optimus.optimizations :as optimizations]
            [optimus.prime :as optimus]
            [optimus.strategies :refer [serve-live-assets]]
            [bidi.ring :refer [make-handler]]
            [chrysostom.static :as static]
            [chrysostom.templates :as tmpl]
            [liberator.core :refer [defresource resource]]
            [liberator.representation :refer [ring-response]]
            [ring.middleware.defaults :refer [site-defaults wrap-defaults]]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.util.response :as res]))

"Liberator library makes things easier when we just need to send the
content of arbitrary files. We just need it to send us a handler.
TODO: Write my own macro in liberator's place. Unless, of course,
there are other things I can use from the lib."
(defresource send-page [page]
  :allowed-methods [:get]
  :available-media-types ["text/html"]
  :handle-ok page)

(defn index-handler
  [request]
  (static/layout-page request "Welcome to Chrysostom!"))

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
  ["/" (into app-routes (map #(gen-route %) (static/get-static-pages)))])

(defn get-assets []
  (assets/load-assets "public" [#".*"]))

(def app
  (wrap-defaults
   (optimus/wrap
    (make-handler (generate-routes))
    get-assets
    optimizations/all
    serve-live-assets)
   (assoc site-defaults :static false)))
