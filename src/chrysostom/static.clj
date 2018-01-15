(ns chrysostom.static
  (:require
   [clojure.java.io :as io]
   [stasis.core :as stasis]
   [hiccup.core :as hiccup]
   [clj-org.org :refer [parse-org]]
   [me.raynes.cegdown :as md]
   [chrysostom.templates :as tmpl]))

(defn layout-page
  [page]
  (tmpl/main-template {:text page}))

(defn layout-org-file
  [file]
  (tmpl/main-template
   {:text
    (hiccup/html (:content (parse-org file)))}))

(defn partial-pages
  [pages]
  (zipmap (keys pages)
          (map layout-page (vals pages))))

(defn org-mode-pages
  [pages]
  (zipmap (map #(clojure.string/replace % #"\.org$" ".html") (keys pages))
          (map layout-org-file (vals pages))))

(defn markdown-pages [pages]
  (zipmap (map #(clojure.string/replace % #"\.md$" "") (keys pages))
          (map #(layout-page (md/to-html %)) (vals pages))))

(defn about-page [request]
  {:status 200
   :headers {"Content-type" "text/html"}
   :body (tmpl/main-template {:text (slurp (io/resource "partials/about.html"))})})

(defn get-partials []
  (partial-pages
   (stasis/slurp-directory "resources/partials" #".*\.html$")))

(defn get-org-files []
  (org-mode-pages
   (stasis/slurp-directory "resources/org-files" #".*\.org$")))

(defn get-markdown-files []
  (markdown-pages
   (stasis/slurp-directory "resources/md" #"\.md$")))

(defn get-static-pages []
  (stasis/merge-page-sources
   {:partials (get-partials)
    :orgfiles (get-org-files)
    :markdown (get-markdown-files)}))
