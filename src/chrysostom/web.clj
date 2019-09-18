(ns chrysostom.web
  (:require [chrysostom.highlight :refer [highlight-code-blocks]]
            [chrysostom.config :as config]
            [chrysostom.templates :as tmpl]
            [optimus.link :as link]
            [me.raynes.conch :refer [let-programs]]
            [clojure.java.io :as io]
            [stasis.core :as stasis]
            [hiccup.core :as hiccup]
            [optimus.assets :as assets]))

(def types [:pages :blog])

(defn- pandoc-html
  [from data]
  (let-programs [pandoc "/usr/bin/pandoc"]
    (str
     "<div id=\"content\">"
     (pandoc "-f" from
             "-t" "html"
             "--no-highlight"
             "-" {:in data})
     "</div>")))

(defn layout-page
  [request page]
  (clojure.string/join (tmpl/main-template
                        {:stylesheet (link/file-path request "/styles/main.css")
                         :text page})))

(defn my-parse-org
  [data]
  (pandoc-html "org" data))

(defn layout-org-file
  [request file]
  (clojure.string/join
   (tmpl/main-template
    {:text
     (my-parse-org file)})))

(defn partial-pages
  [pages]
  (zipmap (map #(clojure.string/replace % #"\.html$" ".html") (keys pages))
          (map #(fn [req] (layout-page req %)) (vals pages))))

(defn org-mode-pages
  [pages]
  (zipmap (map #(clojure.string/replace % #"\.org$" ".html") (keys pages))
          (map #(fn [req] (layout-org-file req %)) (vals pages))))

;; Functions for handling markdown
(def pegdown-options ;; https://github.com/sirthias/pegdown
  [:autolinks :fenced-code-blocks :strikethrough])

;;(defn render-markdown-page [page]
;;(layout-page (md/to-html page pegdown-options)))

(defn markdown-pages [pages]
  (zipmap (map #(clojure.string/replace % #"\.md$" ".html") (keys pages))
          (map #(fn [req] (layout-page req (pandoc-html "markdown" %)))
               (vals pages))))
;; end: Markdown functions

(defn get-partials [type]
  (partial-pages
   (stasis/slurp-directory (config/get-file-path type) #".*\.html$")))

(defn get-org-files [type]
  (org-mode-pages
   (stasis/slurp-directory (config/get-file-path type) #".*\.org$")))

(defn get-markdown-files [type]
  (markdown-pages
   (stasis/slurp-directory (config/get-file-path type) #"\.md$")))

(defn get-raw-types
  [type]
  (stasis/merge-page-sources
   {(keyword (str "partial-" (name type))) (get-partials type)
    (keyword (str "orgfiles-" (name type))) (get-org-files type)
    (keyword (str "markdown-" (name type))) (get-markdown-files type)}))

;; For handling code highlighting
(defn get-raw-pages []
  (stasis/merge-page-sources
   {:public (stasis/slurp-directory (config/get-file-path :public) #".*\.(html|css|js)$")
    :other (into {} (for [type types] (get-raw-types type)))}))

(defn prepare-page [page req]
  (highlight-code-blocks
   (if (string? page) page (page req))))

(defn prepare-pages [pages]
  (zipmap (keys pages)
          (map #(partial prepare-page %) (vals pages))))

(defn get-static-pages []
  (prepare-pages (get-raw-pages)))
;; end: code highlighting
