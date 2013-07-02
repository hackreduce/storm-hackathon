package com.vmc.topoologies;

import java.net.URL;
import java.util.Map;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;

import com.vmc.bolts.HelloCountBolt;
import com.vmc.common.Utils;
import com.vmc.spouts.HelloSpout;

public class HelloTopology
{
  
  private final static String HELLOFILE = "hello.txt";
  
  private static Map<String, String> loadTabbedHellos() throws Exception
  {
    URL url = HelloTopology.class.getResource(HELLOFILE);
    return Utils.loadFileFromUrl(url);
  }
  
  public static void main(String[] args) throws InterruptedException 
  {
    
    Map<String, String> lookup = null;
    try
    {lookup = loadTabbedHellos();}
    catch(Exception e)
    {
      System.err.println("Could not load [" + HELLOFILE + "]");
      e.printStackTrace();
    }
    
    TopologyBuilder tb = new TopologyBuilder();
    tb.setSpout("helloSpout", new HelloSpout(lookup), 2).setNumTasks(3);
    //route all input to one bolt so they can be counted
    tb.setBolt("helloCountBolt", new HelloCountBolt(lookup), 1).globalGrouping("helloSpout");
    
    Config cfg = new Config();
    cfg.setDebug(false);
    
    LocalCluster lc = new LocalCluster();
    lc.submitTopology("Hello Topology", cfg, tb.createTopology());
    Thread.sleep(60000);
    lc.shutdown();
  }

}
