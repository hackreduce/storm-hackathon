---
layout: page
title: "Hopper Storm Hackathon"
date: 2013-07-19 10:03
comments: true
sharing: true
footer: true
---

# Getting Started with Finagle

Hopper is offerring 3 thrift-based Finagle RPC services:

* Geocoder: geocode an address, or reverse geocode a coordinate using Google's geocoder
* SpiderBunny: fetch the contents of a URL honoring ```robots.txt```
* HCache: a simple cache used by both the Geocoder and SpiderBunny, but directly usable a topology as well.

## Get the thrift file

Each service has its own ```.thrift``` file that contains its interface definition.

* [Geocoder](/finagle/geocoder.thrift)
* [SpideBunny](/finagle/spiderbunny.thrift)
* [HCache](/finagle/hcache.thrift)

## Compile it

Use the thrift compiler of your choice to get the bindings for the language you are using (Java, Scala, Ruby, etc.)

* [Thrift](http://thrift.apache.org/) the original thrift compiler. Supports all common languages.
* [Scrooge](https://github.com/twitter/scrooge): compiles to scala-idiomatic classes

## Use it

Service endpoints are as follows:

* Geocoder: ```cluster-7-slave-06.sl.hackreduce.net:5874```
* SpiderBunny: ```cluster-7-slave-07.sl.hackreduce.net:5656```
* HCache: ```cluster-7-slave-03.sl.hackreduce.net:9098```

```scala
    import com.twitter.finagle._
    import com.hopper.spiderbunny._

    val sb = Thrift.newIface[SpiderBunny.FutureIface]("cluster-7-slave-07.sl.hackreduce.net:5656```

    sb.fetch("http://www.hackreduce.org")
      .map { response =>
        println("Content type is: " + response.headers("Content-Type"))
      }
```
