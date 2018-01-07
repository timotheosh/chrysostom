(ns chrysostom.handler
  (:require [bidi.ring :refer [make-handler]]
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
  {:status 200
   :headers {"Content-type" "text/html"}
   :body (tmpl/main-template {:sidebar "Greetings visitor!"})})

(defn gen-route
  "Returns a rout that bidi can use."
  [route]
  [(let [path (first route)]
     (if [(= (subs path 0 1) "/")]
       (clojure.string/replace-first path "/" "")
       path))
   (let [page (clojure.string/join (second route))]
     (send-page page))])

(def app-routes
  [[""  index-handler]])

(defn generate-routes
  []
   (let [rts (static/get-partials)]
     ["/" (into app-routes (map #(gen-route %) rts))]))

(def app
  (wrap-defaults
   (make-handler (generate-routes))
   site-defaults))