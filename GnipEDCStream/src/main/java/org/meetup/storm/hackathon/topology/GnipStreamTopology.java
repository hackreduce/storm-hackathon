package org.meetup.storm.hackathon.topology;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;
import org.meetup.storm.hackathon.bolt.GnipEdcRequestBolt;
import org.meetup.storm.hackathon.bolt.GnipEventPersistBolt;
import org.meetup.storm.hackathon.bolt.GnipEventTransformBolt;

public class GnipStreamTopology {

  public static void main(String[] args) {

    GnipEdcRequestBolt gnipEdcRequestBolt = null;
    gnipEdcRequestBolt = new GnipEdcRequestBolt(
        "https://bostonstorm.gnip.com/data_collectors/1/activities.xml",
        "aarutyunyants",
        "St0rmH4ck2013"
    );

    Config tickConfig = new Config();
    tickConfig.put(Config.TOPOLOGY_TICK_TUPLE_FREQ_SECS, 1);

    TopologyBuilder builder = new TopologyBuilder();
    builder.setBolt("gnip_in", gnipEdcRequestBolt, 1).addConfigurations(tickConfig);
    builder.setBolt("gnip_transform", new GnipEventTransformBolt(), 2).localOrShuffleGrouping("gnip_in", "gnip_events");
    builder.setBolt("gnip_persist", new GnipEventPersistBolt(), 3).localOrShuffleGrouping("gnip_transform", "msgs");

    Config conf = new Config();

    LocalCluster cluster = new LocalCluster();
    cluster.submitTopology("GNIP_EDC_Topology", conf, builder.createTopology());
    try {
      Thread.sleep(30 * 60 * 1000); //30 min
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    cluster.killTopology("GNIP_EDC_Topology");
    cluster.shutdown();
  }
}
