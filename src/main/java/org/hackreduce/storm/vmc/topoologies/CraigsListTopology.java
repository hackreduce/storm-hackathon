package org.hackreduce.storm.vmc.topoologies;

import java.net.URL;
import java.util.Map;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;

import org.hackreduce.storm.vmc.bolts.CraigsListFeedBolt;
import org.hackreduce.storm.vmc.bolts.CraigsListTopNBolt;
import org.hackreduce.storm.vmc.common.Utils;
import org.hackreduce.storm.vmc.spouts.CraigsListSpout;

public class CraigsListTopology
{

  private final static String CLFILE = "craigslistFeeds.txt";
  
  private static Map<String, String> loadTabbedFeeds() throws Exception
  {
    URL url = CraigsListTopology.class.getResource(CLFILE);
    return Utils.loadFileFromUrl(url);
  }
  
  public static void main(String[] args) throws InterruptedException 
  {
    Map<String, String> lookup = null;
    try
    {lookup = loadTabbedFeeds();}
    catch(Exception e)
    {
      System.err.println("Could not load [" + CLFILE + "]");
      e.printStackTrace();
    }
    
    TopologyBuilder tb = new TopologyBuilder();
    tb.setSpout("CraigsListSpout", new CraigsListSpout(lookup), 1);
    tb.setBolt("CraigsListFeedBolt", new CraigsListFeedBolt(), 2).shuffleGrouping("CraigsListSpout");
    
    //top 10 per city in sqft
    tb.setBolt("CraigsListTopNSqFtBolt", new CraigsListTopNBolt(10, "([\\d]+)[\\s]*sqft", "Top10SqFtByCity"), 1).fieldsGrouping("CraigsListFeedBolt", new Fields("city"));
    //top 5 per city in price
    tb.setBolt("CraigsListTopNPriceBolt", new CraigsListTopNBolt(5, "\\$[\\s]*([\\d]+)", "Top5PriceByCity"), 1).fieldsGrouping("CraigsListFeedBolt", new Fields("city"));
    
    //Note that top 5 overall is a subset of the union of the top 5 per city.  This should save processing as only tuples from the top 5 by city are even emitted to this bolt.
    //top 5 overall in price
    tb.setBolt("CraigsListTopNBolt", new CraigsListTopNBolt(5, "\\$[\\s]*([\\d]+)", "Top5PriceOverall"), 1).globalGrouping("CraigsListTopNPriceBolt");
    
    Config cfg = new Config();
    cfg.setDebug(false);
    //Every 15 seconds we want to printout a Top N report.
    cfg.put(Config.TOPOLOGY_TICK_TUPLE_FREQ_SECS, 30);
    
    LocalCluster lc = new LocalCluster();
    lc.submitTopology("Craig's List Topology", cfg, tb.createTopology());
    Thread.sleep(120000);
    lc.shutdown();
  }
}
