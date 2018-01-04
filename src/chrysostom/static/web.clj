(ns chrysostom.static.web
  (:require [stasis.core :as stasis]))

(defn get-pages
  "Load static directory"
  []
  (stasis/slurp-directory "resources/public" #".*\.(html|css|js)$"))
