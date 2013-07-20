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
* cluster-7-master.sl.hackreduce.net:8745 - Nimbus thrift port

## Riak Nodes

Riak is available on the following nodes on port:

* cluster-7-slave-(00,02,03,06).sl.hackreduce.net:8098

## MySQL

MySQL is available on cluster-7-slave-08.sl.hackreduce.net:3306

username: hackreduce

password: codebigorgohome

Please prefix your database name with your team's name.

## Kafka

use the [Storm Kafka Spout](https://github.com/nathanmarz/storm-contrib/tree/master/storm-kafka)

Zookeeper connect String to use kafka:

```cluster-7-slave-04.sl.hackreduce.net:2181,cluster-7-slave-02.sl.hackreduce.net:2181,cluster-7-slave-01.sl.hackreduce.net:2181```

## Hopper Finagle Services

Finagle services are described [here](/finagle/)

## SSH

You can SSH into any node using the following credentials: [hackreduce-0720.pem](hackreduce-0720.pem)

And then:

    chmod 600 hackreduce-0720.pem
    ssh -i hackreduce-0720.pem hackreduce@<hostname>.sl.hackreduce.net

## Anything else

Is not available, but ask the Hopper guys and maybe it'll happen.
