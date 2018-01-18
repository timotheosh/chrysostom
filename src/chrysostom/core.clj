(ns chrysostom.core
  (:require
   [ring.adapter.jetty :as jetty]
   [chrysostom.config :as config]
   [chrysostom.handler :as handler])
  (:gen-class))

(defn -main
  "Starts the web server."
  [& args]
  (let [conf (:main (config/read-config))]
    (defonce server (jetty/run-jetty
                     #'handler/app
                     {:port 8880
                      :send-server-version? false
                      :join? false}))))
