(defproject chrysostom "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [clj-yaml "0.4.0"]
                 [ring/ring-jetty-adapter "1.7.1"]
                 [ring/ring-defaults "0.3.2"]
                 [bidi "2.1.6"]
                 [liberator "0.15.3"]
                 [enlive "1.1.6"]
                 [stasis "2.5.0"]
                 [hiccup "1.0.5"]
                 [clj-org "0.0.2"]
                 [me.raynes/cegdown "0.1.1"]
                 [clygments "2.0.0"]
                 [optimus "0.20.2"]]
  :main ^:skip-aot chrysostom.core
  :target-path "target/%s"
  :plugins [[lein-ring "0.12.5"]]
  :ring {:port 8880
         :handler chrysostom.handler/app
         :auto-reload? true}
  :profiles {
             :uberjar {:aot :all}})
