(ns chrysostom.config
  (:require [clj-yaml.core :as yaml]))

(defn read-config
  "Loads a config file in yaml format and returns a key-word vector."
  ([] (read-config "doc/chrysostom.yml"))
  ([conf]
   (yaml/parse-string
    (slurp conf))))
