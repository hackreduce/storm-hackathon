package org.hackreduce.storm;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.generated.StormTopology;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.utils.Utils;

import java.util.Map;

/**
 * Date: 7/19/13
 * Time: 9:03 PM
 *
 * @author ikaplun
 */
public class KafkaCopyTopology {

  public static void submitTopology(LocalCluster cluster, String sourceFileName, String destinationTopic) throws InterruptedException, AlreadyAliveException, InvalidTopologyException {

    TopologyBuilder builder = new TopologyBuilder();
    builder.setSpout("file_in", new LineSpout());
    builder.setBolt("file_out", new KafkaPersistBolt(), 1).localOrShuffleGrouping("file_in");

    Config conf = new Config();
    conf.put("linespout.file", sourceFileName);
    conf.put("persist.topic", destinationTopic);
    conf.setDebug(true);
    StormTopology topology = builder.createTopology();
    if (cluster != null) {
      submitLocalTopology(cluster, "KafkaCopyTopology", conf, topology);
    } else {
      Map stormConf = Utils.readStormConfig();
      String nimbusHost = (String) stormConf.get(Config.NIMBUS_HOST);
      int nimbusPort = Utils.getInt(stormConf.get(Config.NIMBUS_THRIFT_PORT));
      System.out.println("Using nimbus host:" + nimbusHost + ":" + nimbusPort);
      StormSubmitter.submitTopology("KafkaCopyTopology", conf, topology);
//      HackReduceStormSubmitter.submitTopology("CopyTopology", conf, topology);
    }
  }

  public static void main(String[] args) {
    try {
      //if there is a 3rd command line parameter, run remote cluster
      if (args != null && args.length > 2) {
        KafkaCopyTopology.submitTopology(null, args[0], args[1]);
      } else {
        KafkaCopyTopology.submitTopology(new LocalCluster(), args[0], args[1]);
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
    } catch (AlreadyAliveException e) {
      e.printStackTrace();
    } catch (InvalidTopologyException e) {
      e.printStackTrace();
    }
  }

  private static void submitLocalTopology(LocalCluster cluster, String topoName, Config conf, StormTopology topology) throws InterruptedException {
    cluster.submitTopology(topoName, conf, topology);
    try {
      Thread.sleep(30 * 1000); //30 min
    } finally {
      cluster.killTopology("KafkaCopyTopology");
      cluster.shutdown();
    }
  }
}
