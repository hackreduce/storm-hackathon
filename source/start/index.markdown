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

    export TEAM_NAME=<my-team-name>
    git clone https://github.com/<your github.com account>/storm-hackathon
    cd storm-hackathon
    mvn package
    ./tools/submit org.hackreduce.storm.gnip.topology.GnipStreamTopology 

If all of these commands were successful, you should now see your topology running in the [Storm UI](http://cluster-7-master.sl.hackreduce.net:8080). It should appear as ```<my-team-name>-GNIP_EDC_Topology```.

You may now kill this example topology:

    ./tools/kill <my-team-name>-GNIP_EDC_Topology

Note that if you don't specify a team name (through the ```TEAM_NAME``` environment variable), the helper code will pick one at random for you. Set the ```TEAM_NAME``` environment variable to get a constant prefix.

## Hack!

Congradulations, you are now ready to create your own topologies and submit them in the same way: simply change the name of the main class when submitting.

Other example topologies are available in your forked repository. You can start from one of those or from scratch.

In order to submit a topology prefixed with your team's name, use the ```org.hackreduce.storm.HackReduceStormSubmitter``` utility class or simply make sure you name your topologies accordingly.

# Storm VM

If you would like to run a mini Storm cluster in a [Vagrant](http://www.vagrantup.com/) vm, follow these instructions:

1.   [Install VirtualBox](https://www.virtualbox.org/wiki/Downloads)
2.   [Install Vagrant](http://docs.vagrantup.com/v2/installation/)
3.   Initialize Vagrant project (this will create a _Vagrantfile_)
    {% highlight sh %} 
    vagrant init storm https://dl.dropboxusercontent.com/u/2759041/Storm/stormvm.box
    {% endhighlight %}
4.   Add the following line to your Vagrantfile
    {% highlight ruby %} 
      config.vm.network :hostonly, ip: "192.168.101.11"
    {% endhighlight %}
5.   Start VM
    {% highlight sh %} 
    vagrant up
    {% endhighlight %}
6.   open storm ui http://192.168.101.11:8082
7.   configure storm submitter to point to the local cluster
    {% highlight sh %}
    mkdir -p ~/.storm
    echo 'nimbus.host: "192.168.101.11"' > ~/.storm/storm.yaml
    {% endhighlight %}
    *    to point back to the softlayer cluster, change ```nimbus.host``` to ```cluster-7-master.sl.hackreduce.net``` 


The vagrant box was built with the following software versions:

*   Vagrant 1.1.0
*   VirtualBox 4.1.23

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
