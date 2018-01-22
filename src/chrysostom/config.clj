(ns chrysostom.config
  (:require [clj-yaml.core :as yaml]))

(defn read-config
  "Loads a config file in yaml format and returns a key-word vector."
  ([] (read-config "doc/chrysostom.yml"))
  ([conf]
   (yaml/parse-string
    (slurp conf))))

(defn get-file-path
  "doctype will be one of
          :public
          or one of the defined types"
  [doctype]
  (let [conf (:documents (read-config))]
    (or (get conf doctype)
        (if (= doctype :public)
          "resources/public"
          (str "resources/" doctype)))))
