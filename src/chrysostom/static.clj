(ns chrysostom.static
  (:require
   [clojure.java.io :as io]
   [stasis.core :as stasis]
   [chrysostom.templates :as tmpl]))

(defn layout-page
  [page]
  (tmpl/main-template {:text page}))

(defn partial-pages
  [pages]
  (zipmap (keys pages)
          (map layout-page (vals pages))))

(defn about-page [request]
  {:status 200
   :headers {"Content-type" "text/html"}
   :body (tmpl/main-template {:text (slurp (io/resource "partials/about.html"))})})

(defn get-partials []
  (partial-pages
   (stasis/slurp-directory "resources/partials" #".*\.html$")))

(defn get-pages []
  (stasis/merge-page-sources
   {:public (stasis/slurp-directory "resources/public" #".*\.(html|css|js)$")
    :partials (partial-pages
               (stasis/slurp-directory "resources/partials" #".*\.html$"))}))
