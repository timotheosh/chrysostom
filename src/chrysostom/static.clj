(ns chrysostom.static
  (:require [optimus.link :as link]
            [clojure.java.io :as io]
            [stasis.core :as stasis]
            [hiccup.core :as hiccup]
            [clj-org.org :refer [parse-org]]
            [chrysostom.highlight :refer [highlight-code-blocks]]
            [chrysostom.config :as config]
            [me.raynes.cegdown :as md]
            [chrysostom.templates :as tmpl]))

(defn- get-file-path
  "doctype will be one of
     :public
     :markdown
     :org-files
     :partials"
  [doctype]
  (let [conf (:documents (config/read-config))]
    (or (get conf doctype)
        (cond (= doctype :markdown) "resources/md"
              (= doctype :org-files) "resources/org-files"
              (= doctype :partials) "resources/partials"
              (= doctype :public) "resources/public"
              :else nil))))

(defn layout-page
  [request page]
  (clojure.string/join (tmpl/main-template
                        {:stylesheet (link/file-path request "/styles/main.css")
                         :text page})))

(defn layout-org-file
  [request file]
  (clojure.string/join
   (tmpl/main-template
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
   (stasis/slurp-directory (get-file-path :partials) #".*\.html$")))

(defn get-org-files []
  (org-mode-pages
   (stasis/slurp-directory (get-file-path :org-files) #".*\.org$")))

(defn get-markdown-files []
  (markdown-pages
   (stasis/slurp-directory (get-file-path :markdown) #"\.md$")))

;; For handling code highlighting
(defn get-raw-pages []
  (stasis/merge-page-sources
   {:public (stasis/slurp-directory (get-file-path :public) #".*\.(html|css|js)$")
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
