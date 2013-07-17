---
layout: page
title: "Storm Hackathon Getting Started"
date: 2013-07-09 10:03
comments: true
sharing: true
footer: true
---

# Getting Started

This will help you get started with Storm. At the end of this exercise, you should be able to submit topologies to the Storm cluster.

## Prerequisites

1.   A sane shell ([Cygwin](http://www.cygwin.com/) may or may not work)
1.   [git](http://git-scm.com/downloads)
1.   A [JDK](http://www.oracle.com/technetwork/java/javasebusiness/downloads/java-archive-downloads-javase6-419409.html#jdk-6u45-oth-JPR)
1.   [Maven](http://maven.apache.org/download.cgi)
1.   A fork of the [storm-hackathon repo](https://github.com/hackreduce/storm-hackathon)
1.   A team name (this will be used to partition your topologies and data from other teams)

## Setting up your Environment 

Here, we'll clone your forked repository, build the example topologies and submit one to the Storm cluster.

    git clone https://github.com/<your github.com account>/storm-hackathon
    cd storm-hackathon
    mvn package
    ./tools/submit hackreduce.storm.Example <my-team-name>

If all of these commands were successful, you should now see your topology running in the [Storm UI](http://cluster-7-master.sl.hackreduce.net:8080). It should appear as <my-team-name>-example.

You may now kill this example topology:

    ./tools/kill <my-team-name>-example

## Hack!

Congradulations, you are now ready to create your own topologies and submit them in the same way: simply change the name of the main class when submitting.

Other example topologies are available in your forked repository. You can start from one of those or from scratch.

# Resources

*   [storm-project.net](http://storm-project.net/)

## Learning Storm 

*   [storm tutorial](https://github.com/nathanmarz/storm/wiki/Tutorial)
*   [trident tutorial](https://github.com/nathanmarz/storm/wiki/Trident-tutorial)

### Spouts, Bolts, etc.

*    [storm-kestrel](https://github.com/rapportive-oss/storm-amqp-spout): Adapter to use [Kestrel](https://github.com/robey/kestrel/) as a spout
*    [storm-pubsub](https://github.com/sorenmacbeth/storm-pubsub): A spout that subscribes to a Redis pubsub stream
*    [storm-kafka](https://github.com/nathanmarz/storm-contrib/tree/master/storm-kafka): [Kafka](http://kafka.apache.org/) spout
*    [storm-contrib](https://github.com/nathanmarz/storm-contrib/): lots of spouts and bolts for various endpoints (e.g. Mongo, cassandra, etc.)
*    [trident-memcached](https://github.com/nathanmarz/trident-memcached): Memcached Trident State
*    [Storm-R](https://github.com/quintona/storm-r): provides a trident function that enables integration with R functions
*    [trident-ml](https://github.com/pmerienne/trident-ml): machine learning on Storm

### Polyglot Storm

*   [clojure](https://github.com/nathanmarz/storm/wiki/Clojure-DSL)
*   [scala](https://github.com/velvia/ScalaStorm)
*   [tormenta](https://github.com/twitter/tormenta) - library for type safe kestrel/kafka spouts from twitter
*   [ruby](https://github.com/colinsurprenant/redstorm)
*   [python](https://github.com/AirSage/Petrel)
*   [perl](https://github.com/gphat/io-storm)
*   [php](https://github.com/lazyshot/storm-php)

## Presentations

{% vimeo 40972420 %}

*   [Storm Introduction](http://www.infoq.com/presentations/Storm-Introduction) - Nathan Marz 2013 Storm presentation
*   [Storm: Distributed and Fault-tolerant Real-time Computation](http://www.infoq.com/presentations/Storm) - Nathan Marz talking about storm in 2011
