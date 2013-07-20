---
layout: page
title: "gnip"
date: 2013-07-18 10:01
comments: true
sharing: true
footer: true
---

# GNIP Enterprise Data Collector (EDC).

GNIP provides access to enriched data from sources including Twitter, Facebook, Youtube, etc. There are several ways to access the data. The example we have set up is Youtube uploads using EDC. It is a stream of events describing uploads, matching some criteria. The data is retrieved by periodically (every 1 sec) connecting to a specified endpoint, receiving the response and processing it. The response contains a “refreshUrl” parameter, which should be used as the target endpoint for the next request. The topology uses tick events to trigger EDC requests:

```java
tickConfig.put(Config.TOPOLOGY_TICK_TUPLE_FREQ_SECS, 1);
```

The request is performed by GnipEdcRequestBolt, response is parsed and each event is emitted to “gnip_events” stream. There is two more bolts in the topology. GnipEventTransformBolt parses the event, extracts two fields (“title”, “link”) from it and emits a concatenated value as “msgs” stream. GnipEventPersistBolt simply prints the values.
Quick Start Guide.

Modify ```./src/main/java/org/hackreduce/storm/gnip/topology/GnipStreamTopology.java``` providing valid credentials to connect to GNIP EDC.

## Build the project:
mvn clean package

## Run it:
``` bash
tools/submit org.hackreduce.storm.gnip.topology.GnipStreamTopology 1
```

The command line parameter ```1``` will force the topology to run in local cluster. If no parameter is specified, the topology will be submitted to the cluster using StormSubmitter.submitTopology() method to localhost as Niumbus.
