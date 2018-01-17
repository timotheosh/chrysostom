(ns chrysostom.static
  (:require
   [clojure.java.io :as io]
   [stasis.core :as stasis]
   [hiccup.core :as hiccup]
   [clj-org.org :refer [parse-org]]
   [chrysostom.highlight :refer [highlight-code-blocks]]
   [me.raynes.cegdown :as md]
   [chrysostom.templates :as tmpl]))

(defn layout-page
  [page]
  (clojure.string/join (tmpl/main-template {:text page})))

(defn layout-org-file
  [file]
  (clojure.string/join
   (tmpl/main-template
    {:text
     (hiccup/html (:content (parse-org file)))})))

(defn partial-pages
  [pages]
  (zipmap (keys pages)
          (map layout-page (vals pages))))

(defn org-mode-pages
  [pages]
  (zipmap (map #(clojure.string/replace % #"\.org$" ".html") (keys pages))
          (map layout-org-file (vals pages))))

;; Functions for handling markdown
(def pegdown-options ;; https://github.com/sirthias/pegdown
  [:autolinks :fenced-code-blocks :strikethrough])

(defn render-markdown-page [page]
  (layout-page (md/to-html page pegdown-options)))

(defn markdown-pages [pages]
  (zipmap (map #(clojure.string/replace % #"\.md$" "") (keys pages))
          (map render-markdown-page (vals pages))))
;; end: Markdown functions

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

;; For handling code highlighting
(defn get-raw-pages []
  (stasis/merge-page-sources
   {:public (stasis/slurp-directory "resources/public" #".*\.(html|css|js)$")
    :partials (get-partials)
    :orgfiles (get-org-files)
    :markdown (get-markdown-files)}))

(defn prepare-pages [pages]
  (zipmap (keys pages)
          (map #(highlight-code-blocks %) (vals pages))))

(defn get-static-pages []
  (prepare-pages (get-raw-pages)))
;; end: code highlighting
