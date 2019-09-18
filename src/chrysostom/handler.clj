(ns chrysostom.handler
  (:require [chrysostom.templates :as tmpl]
            [chrysostom.web :as web]
            [optimus.optimizations :as optimizations]
            [optimus.prime :as optimus]
            [optimus.strategies :refer [serve-live-assets]]
            [ring.middleware.defaults :refer [site-defaults wrap-defaults]]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.util.response :as res]
            [stasis.core :as stasis]))

(def app
  (wrap-defaults
   (optimus/wrap
    (stasis/serve-pages (web/get-static-pages))
    tmpl/get-assets
    optimizations/all
    serve-live-assets)
   (assoc site-defaults :static false)))
