package org.hackreduce.storm.gnip.topology;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.generated.StormTopology;
import backtype.storm.topology.TopologyBuilder;
import org.hackreduce.storm.HackReduceStormSubmitter;
import org.hackreduce.storm.KafkaPersistBolt;
import org.hackreduce.storm.gnip.bolt.GnipEdcRequestBolt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class GnipTwitterSpritzerFeedToKafka {
  private static final String TOPOLOGY_NAME = "GNIP_Twitter_Spritzer_Topology";
  private static Logger LOG = LoggerFactory.getLogger(GnipTwitterSpritzerFeedToKafka.class);

  public static void submitTopology(LocalCluster cluster, String topoName) throws InterruptedException, AlreadyAliveException, InvalidTopologyException, IOException {
    Properties prop = new Properties();
    InputStream in = GnipDeliciousBookmarksFeedToKafka.class.getResourceAsStream("gnip.properties");
    prop.load(in);
    in.close();

    GnipEdcRequestBolt gnipRequestBolt = new GnipEdcRequestBolt(
        "https://bostonstorm.gnip.com/data_collectors/9/activities.xml",
        prop.getProperty("gnip_login"),
        prop.getProperty("gnip_password"),
        "gnip_events",
        "line"
    );

    Config tickConfig = new Config();
    tickConfig.put(Config.TOPOLOGY_TICK_TUPLE_FREQ_SECS, 1);

    TopologyBuilder builder = new TopologyBuilder();
    builder.setBolt("gnip_in_tw", gnipRequestBolt, 1).addConfigurations(tickConfig);
    builder.setBolt("gnip_transform", new KafkaPersistBolt(), 1)
        .localOrShuffleGrouping("gnip_in_tw", "gnip_events");

    Config conf = new Config();
    conf.put("persist.topic", "twitter_spritzer");

    StormTopology topology = builder.createTopology();
    if (cluster != null) {
      submitLocalTopology(cluster, topoName, conf, topology);
    } else {
      HackReduceStormSubmitter.submitTopology(topoName, conf, topology);
    }
  }

  private static void submitLocalTopology(LocalCluster cluster, String topoName, Config conf, StormTopology topology) throws InterruptedException {
    cluster.submitTopology(topoName, conf, topology);
    try {
      Thread.sleep(30 * 60 * 1000); //30 min
    } finally {
      cluster.killTopology(topoName);
      cluster.shutdown();
    }
  }

  public static void main(String[] args) {
    try {
      LOG.info("Starting {}", TOPOLOGY_NAME);
      //if there is any command-line parameter - will run local cluster
      GnipTwitterSpritzerFeedToKafka.submitTopology(((args != null && args.length > 0)) ? new LocalCluster() : null, TOPOLOGY_NAME);
    } catch (InterruptedException e) {
      LOG.error("Failed", e);
    } catch (AlreadyAliveException e) {
      LOG.error("Failed", e);
    } catch (InvalidTopologyException e) {
      LOG.error("Failed", e);
    } catch (IOException e) {
      LOG.error("Failed", e);
    }
  }
}
