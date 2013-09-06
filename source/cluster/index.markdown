---
layout: page
title: "cluster"
date: 2013-07-17 09:14
comments: true
sharing: true
footer: true
---

To deploy and demo your topology with production data, you can use the Hopper SoftLayer storm cluster. 

# Cluster info

## Storm

* http://cluster-7-master.sl.hackreduce.net:5698/ - Storm UI
* ```cluster-7-master.sl.hackreduce.net:8745``` - Nimbus thrift port

## Riak Nodes

[Riak](http://basho.com/riak) is available on the following nodes on port:

* ```cluster-7-slave-(04,05,06,07).sl.hackreduce.net:8098```

## Redis

[Redis](http://redis.io/) is available on the following node:

* ```cluster-7-slave-20.sl.hackreduce.net```

## ElasticSearch

[ElasticSearch](http://elasticsearch.org) is available on the following nodes:

* ```cluster-7-slave-11.sl.hackreduce.net```

## Kafka

Use the [Storm Kafka Spout](https://github.com/nathanmarz/storm-contrib/tree/master/storm-kafka)

Zookeeper connect String to use kafka:

```cluster-7-slave-01.sl.hackreduce.net:2181,cluster-7-slave-02.sl.hackreduce.net:2181,cluster-7-slave-03.sl.hackreduce.net:2181```

## Hopper Finagle Services

Finagle services are described [here](/finagle/)

## Anything else

Is not available, but ask the Hopper guys and maybe it'll happen.
