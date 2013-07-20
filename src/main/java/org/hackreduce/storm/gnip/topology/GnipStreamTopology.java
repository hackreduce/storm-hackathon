package org.hackreduce.storm.gnip.topology;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.generated.StormTopology;
import backtype.storm.topology.TopologyBuilder;

import org.hackreduce.storm.HackReduceStormSubmitter;
import org.hackreduce.storm.gnip.bolt.GnipEdcRequestBolt;
import org.hackreduce.storm.gnip.bolt.GnipEventPersistBolt;
import org.hackreduce.storm.gnip.bolt.GnipEventTransformBolt;

public class GnipStreamTopology {

  public static void submitTopology(LocalCluster cluster) throws InterruptedException, AlreadyAliveException, InvalidTopologyException {
    GnipEdcRequestBolt gnipEdcYoutubeRequestBolt = null;
    gnipEdcYoutubeRequestBolt = new GnipEdcRequestBolt(
        "https://bostonstorm.gnip.com/data_collectors/1/activities.xml",
        "xxx",
        "xxx"
    );

    GnipEdcRequestBolt gnipEdcTwitterRequestBolt = null;
    gnipEdcTwitterRequestBolt = new GnipEdcRequestBolt(
        "https://bostonstorm.gnip.com/data_collectors/4/activities.xml",
        "xxx",
        "xxx"
    );

    Config tickConfig = new Config();
    tickConfig.put(Config.TOPOLOGY_TICK_TUPLE_FREQ_SECS, 1);

    TopologyBuilder builder = new TopologyBuilder();
    builder.setBolt("gnip_in_yt", gnipEdcYoutubeRequestBolt, 1).addConfigurations(tickConfig);
    builder.setBolt("gnip_in_tw", gnipEdcTwitterRequestBolt, 1).addConfigurations(tickConfig);
    builder.setBolt("gnip_transform", new GnipEventTransformBolt(), 2)
        .localOrShuffleGrouping("gnip_in_yt", "gnip_events")
        .localOrShuffleGrouping("gnip_in_tw", "gnip_events");
    builder.setBolt("gnip_persist", new GnipEventPersistBolt(), 3).localOrShuffleGrouping("gnip_transform", "msgs");

    Config conf = new Config();
    StormTopology topology = builder.createTopology();
    if (cluster != null) {
      submitLocalTopology(cluster, "GNIP_EDC_Topology", conf, topology);
    } else {
      HackReduceStormSubmitter.submitTopology("GNIP_EDC_Topology", conf, topology);
    }
  }

  private static void submitLocalTopology(LocalCluster cluster, String topoName, Config conf, StormTopology topology) throws InterruptedException {
    cluster.submitTopology("GNIP_EDC_Topology", conf, topology);
    try {
      Thread.sleep(30 * 60 * 1000); //30 min
    } finally {
      cluster.killTopology("GNIP_EDC_Topology");
      cluster.shutdown();
    }
  }

  public static void main(String[] args) {
    try {
      System.out.println("Starting");
      //if there is any command-line parameter - will run local cluster
      GnipStreamTopology.submitTopology(((args != null && args.length > 0)) ? new LocalCluster() : null);
    } catch (InterruptedException e) {
      e.printStackTrace();
    } catch (AlreadyAliveException e) {
      e.printStackTrace();
    } catch (InvalidTopologyException e) {
      e.printStackTrace();
    }
  }

}
