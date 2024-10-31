(defproject noob-project "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [com.novemberain/monger "3.5.0"]
                 [ring/ring-core "1.9.5"]
                 [ring/ring-jetty-adapter "1.9.5"]
                 [compojure "1.6.2"]]
  :main ^:skip-aot noob-project.core
  :plugins [[lein-ring "0.12.5"]]
  :ring {:handler noob-project.core/app}
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}
             :dev {:dependencies [[ring/ring-mock "0.4.0"]]}})
