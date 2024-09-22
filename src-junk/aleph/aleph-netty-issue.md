- aleph uses netty
- netty brings compiled classfiles in jars.
- ClassNotFoundException 
: io.netty.channel.epoll.Epoll
- fastjar -t -f ~/.m2/repository/io/netty/netty-transport-classes-epoll/4.1.100.Final/netty-transport-classes-epoll-4.1.100.Final.jar >>    netty-transport-classes-epoll.txt
  io/netty/channel/epoll/Epoll.class
- we have the jar, we have the jar with the class
- why does it not get loaded??

Execution error (ClassNotFoundException) at java.net.URLClassLoader/findClass (REPL:-1).
io.netty.channel.epoll.Epoll

show architecture 
:
uname -a

fastjar -t -f ~/.m2/repository/io/netty/netty-transport/4.1.111.Final/netty-transport-4.1.111.Final.jar
only class files.

classpath: 

 src

:/home/florian/.m2/repository/aleph/aleph/0.8.1/aleph-0.8.1.jar 
:/home/florian/.m2/repository/org/clojure/clojure/1.11.3/clojure-1.11.3.jar 
:/home/florian/.m2/repository/io/netty/netty-codec/4.1.111.Final/netty-codec-4.1.111.Final.jar 
:/home/florian/.m2/repository/io/netty/netty-codec-http/4.1.111.Final/netty-codec-http-4.1.111.Final.jar 
:/home/florian/.m2/repository/io/netty/netty-codec-http2/4.1.111.Final/netty-codec-http2-4.1.111.Final.jar 
:/home/florian/.m2/repository/io/netty/netty-handler/4.1.111.Final/netty-handler-4.1.111.Final.jar 
:/home/florian/.m2/repository/io/netty/netty-handler-proxy/4.1.111.Final/netty-handler-proxy-4.1.111.Final.jar 
:/home/florian/.m2/repository/io/netty/netty-resolver/4.1.111.Final/netty-resolver-4.1.111.Final.jar 
:/home/florian/.m2/repository/io/netty/netty-resolver-dns/4.1.111.Final/netty-resolver-dns-4.1.111.Final.jar 
:/home/florian/.m2/repository/io/netty/netty-transport/4.1.111.Final/netty-transport-4.1.111.Final.jar 
:/home/florian/.m2/repository/io/netty/netty-transport-native-epoll/4.1.111.Final/netty-transport-native-epoll-4.1.111.Final-linux-aarch_64.jar 
:/home/florian/.m2/repository/io/netty/netty-transport-native-epoll/4.1.111.Final/netty-transport-native-epoll-4.1.111.Final-linux-x86_64.jar 
:/home/florian/.m2/repository/io/netty/netty-transport-native-kqueue/4.1.111.Final/netty-transport-native-kqueue-4.1.111.Final-osx-x86_64.jar 
:/home/florian/.m2/repository/io/netty/incubator/netty-incubator-transport-native-io_uring/0.0.25.Final/netty-incubator-transport-native-io_uring-0.0.25.Final-linux-aarch_64.jar 
:/home/florian/.m2/repository/io/netty/incubator/netty-incubator-transport-native-io_uring/0.0.25.Final/netty-incubator-transport-native-io_uring-0.0.25.Final-linux-x86_64.jar 
:/home/florian/.m2/repository/manifold/manifold/0.4.3/manifold-0.4.3.jar 
:/home/florian/.m2/repository/metosin/malli/0.16.1/malli-0.16.1.jar 
:/home/florian/.m2/repository/org/clj-commons/byte-streams/0.3.4/byte-streams-0.3.4.jar 
:/home/florian/.m2/repository/org/clj-commons/dirigiste/1.0.4/dirigiste-1.0.4.jar 
:/home/florian/.m2/repository/org/clj-commons/primitive-math/1.0.1/primitive-math-1.0.1.jar 
:/home/florian/.m2/repository/org/clojure/tools.logging/1.3.0/tools.logging-1.3.0.jar 
:/home/florian/.m2/repository/potemkin/potemkin/0.4.7/potemkin-0.4.7.jar 
:/home/florian/.m2/repository/org/clojure/core.specs.alpha/0.2.62/core.specs.alpha-0.2.62.jar 
:/home/florian/.m2/repository/org/clojure/spec.alpha/0.3.218/spec.alpha-0.3.218.jar 
:/home/florian/.m2/repository/io/netty/incubator/netty-incubator-transport-classes-io_uring/0.0.25.Final/netty-incubator-transport-classes-io_uring-0.0.25.Final.jar 
:/home/florian/.m2/repository/riddley/riddley/0.2.0/riddley-0.2.0.jar 
:/home/florian/.m2/repository/borkdude/dynaload/0.3.5/dynaload-0.3.5.jar 
:/home/florian/.m2/repository/borkdude/edamame/1.4.25/edamame-1.4.25.jar 
:/home/florian/.m2/repository/fipp/fipp/0.6.26/fipp-0.6.26.jar 
:/home/florian/.m2/repository/mvxcvi/arrangement/2.1.0/arrangement-2.1.0.jar 
:/home/florian/.m2/repository/org/clojure/test.check/1.1.1/test.check-1.1.1.jar 
:/home/florian/.m2/repository/io/netty/netty-buffer/4.1.107.Final/netty-buffer-4.1.107.Final.jar 
:/home/florian/.m2/repository/io/netty/netty-common/4.1.107.Final/netty-common-4.1.107.Final.jar 
:/home/florian/.m2/repository/io/netty/netty-transport-native-unix-common/4.1.107.Final/netty-transport-native-unix-common-4.1.107.Final.jar 
:/home/florian/.m2/repository/org/clojure/tools.reader/1.3.4/tools.reader-1.3.4.jar 
:/home/florian/.m2/repository/org/clojure/core.rrb-vector/0.1.2/core.rrb-vector-0.1.2.jar 
:/gnu/store/v54h290s09qljzr934n2icyj0j9i2605-clojure-tools-1.11.1.1413/lib/clojure/libexec/exec.jar