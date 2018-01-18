(ns chrysostom.static
  (:require [optimus.link :as link]
            [clojure.java.io :as io]
            [stasis.core :as stasis]
            [hiccup.core :as hiccup]
            [clj-org.org :refer [parse-org]]
            [chrysostom.highlight :refer [highlight-code-blocks]]
            [me.raynes.cegdown :as md]
            [chrysostom.templates :as tmpl]))

(defn layout-page
  [request page]
  (clojure.string/join (tmpl/main-template
                        {:stylesheet (link/file-path request "/styles/main.css")
                         :text page})))

(defn layout-org-file
  [request file]
  (clojure.string/join
   (tmpl/main-template
    request
    {:text
     (hiccup/html (:content (parse-org file)))})))

(defn partial-pages
  [pages]
  (zipmap (keys pages)
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
  (zipmap (map #(clojure.string/replace % #"\.md$" "") (keys pages))
          (map #(fn [req] (layout-page req (md/to-html % pegdown-options)))
               (vals pages))))
;; end: Markdown functions

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

(defn prepare-page [page req]
  (-> (if (string? page) page (page req))
      highlight-code-blocks))

(defn prepare-pages [pages]
  (zipmap (keys pages)
          (map #(partial prepare-page %) (vals pages))))

(defn get-static-pages []
  (prepare-pages (get-raw-pages)))
;; end: code highlighting
