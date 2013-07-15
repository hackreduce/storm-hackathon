---
layout: page
title: "Storm Hackathon Getting Started"
date: 2013-07-09 10:03
comments: true
sharing: true
footer: true
---

# Getting Started

## Prerequisites

1.   [Setting up a Development Environment](https://github.com/nathanmarz/storm/wiki/Setting-up-development-environment)
2.   [storm-starter](https://github.com/nathanmarz/storm-starter): Storm starter project, great for getting going

# Resources

*   [storm-project.net](http://storm-project.net/)

## Learning Storm 

*   [storm tutorial](https://github.com/nathanmarz/storm/wiki/Tutorial)
*   [trident tutorial](https://github.com/nathanmarz/storm/wiki/Trident-tutorial)


## Tools and Code

### Deployment, etc.
*    [storm-deploy](https://github.com/nathanmarz/storm-deploy): easy set up of EC2 clusters

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