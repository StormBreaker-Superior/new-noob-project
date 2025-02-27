(defproject noob-project "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [com.novemberain/monger "3.5.0"]
                 [ring "1.9.5"]
                 [ring/ring-json "0.5.1"]
                 [compojure "1.6.2"]
                 [mount "0.1.16"]]
  :main ^:skip-aot noob-project.core
  :ring {:handler noob-project.core/app}
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})
