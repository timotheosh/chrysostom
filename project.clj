(defproject chrysostom "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [clj-yaml "0.4.0"]
                 [ring/ring-jetty-adapter "1.6.3"]
                 [ring/ring-defaults "0.3.1"]
                 [bidi "2.1.3"]
                 [liberator "0.15.1"]
                 [enlive "1.1.6"]
                 [stasis "2.3.0"]
                 [hiccup "1.0.5"]
                 [clj-org "0.0.2"]
                 [me.raynes/cegdown "0.1.1"]]
  :main ^:skip-aot chrysostom.core
  :target-path "target/%s"
  :ring {:handler chrysostom.handler/app}
  :profiles {
             :uberjar {:aot :all}
             :dev {:plugins [[lein-ring "0.8.10"]]}})
