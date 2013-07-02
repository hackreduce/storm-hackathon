(defproject stormFeeds "0.0.1-SNAPSHOT"
  :source-path "src/clj"
  :java-source-path "src/jvm"
  :javac-options {:debug "true" :fork "true"}
  :resources-path [
    "lib/asm-4.0.jar"
    "lib/bsh-2.0b4.jar"
    "lib/carbonite-1.5.0.jar"
    "lib/clj-time-0.4.1.jar"
    "lib/clojure-1.4.0.jar"
    "lib/clout-1.0.1.jar"
    "lib/commons-codec-1.4.jar"
    "lib/commons-collections-3.2.1.jar"
    "lib/commons-exec-1.1.jar"
    "lib/commons-fileupload-1.2.1.jar"
    "lib/commons-httpclient-3.0.1.jar"
    "lib/commons-io-1.4.jar"
    "lib/commons-lang-2.5.jar"
    "lib/commons-logging-1.0.4.jar"
    "lib/commons-logging-1.1.1.jar"
    "lib/commons-logging-api-1.0.4.jar"
    "lib/compojure-1.1.3.jar"
    "lib/core.incubator-0.1.0.jar"
    "lib/curator-client-1.0.1.jar"
    "lib/curator-framework-1.0.1.jar"
    "lib/disruptor-2.10.1.jar"
    "lib/fest-assert-core-2.0M8.jar"
    "lib/fest-util-1.2.3.jar"
    "lib/guava-13.0.1.jar"
    "lib/hiccup-0.3.6.jar"
    "lib/httpclient-4.1.1.jar"
    "lib/httpcore-4.1.jar"
    "lib/jcommander-1.27.jar"
    "lib/jdom-1.0.jar"
    "lib/jetty-6.1.26.jar"
    "lib/jetty-util-6.1.26.jar"
    "lib/jgrapht-0.8.3.jar"
    "lib/jline-0.9.94.jar"
    "lib/joda-time-2.0.jar"
    "lib/json-simple-1.1.jar"
    "lib/junit-3.8.1.jar"
    "lib/jzmq-2.1.0.jar"
    "lib/kryo-2.17.jar"
    "lib/"libthrift7-0.7.0.jar"
    "lib/log4j-1.2.16.jar"
    "lib/math.numeric-tower-0.0.1.jar"
    "lib/minlog-1.2.jar"
    "lib/mockito-all-1.9.0.jar"
    "lib/objenesis-1.2.jar"
    "lib/reflectasm-1.07-shaded.jar"
    "lib/ring-core-1.1.5.jar"
    "lib/ring-jetty-adapter-0.3.11.jar"
    "lib/ring-servlet-0.3.11.jar"
    "lib/rome-1.0.0.jar"
    "lib/rome-fetcher-1.0.0.jar"
    "lib/servlet-api-2.5-20081211.jar"
    "lib/servlet-api-2.5.jar"
    "lib/slf4j-api-1.5.8.jar"
    "lib/slf4j-log4j12-1.5.8.jar"
    "lib/snakeyaml-1.6.jar"
    "lib/storm-0.8.2.jar"
    "lib/testng-6.8.jar"
    "lib/tools.cli-0.2.2.jar"
    "lib/tools.logging-0.2.3.jar"
    "lib/tools.macro-0.1.0.jar"
    "lib/twitter4j-core-2.2.6-SNAPSHOT.jar"
    "lib/twitter4j-stream-2.2.6-SNAPSHOT.jar"
    "lib/xercesImpl-2.4.0.jar"
    "lib/zookeeper-3.3.3.jar"  
  ]
  :aot :all
  :repositories {
;;                 "twitter4j" "http://twitter4j.org/maven2"
                 }

  :dependencies [
;;                 [org.twitter4j/twitter4j-core "2.2.6-SNAPSHOT"]
;;                 [org.twitter4j/twitter4j-stream "2.2.6-SNAPSHOT"]
                   [commons-collections/commons-collections "3.2.1"]
                 ]

  :dev-dependencies [[storm "0.8.2"]
                     [org.clojure/clojure "1.4.0"]
                     ])

