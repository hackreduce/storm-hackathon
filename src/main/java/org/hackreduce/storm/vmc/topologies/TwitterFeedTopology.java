package org.hackreduce.storm.vmc.topologies;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;
import org.hackreduce.storm.vmc.bolts.TwitterContentFilter;
import org.hackreduce.storm.vmc.spouts.TwitterStreamSpout;


/**
 * Created with IntelliJ IDEA.
 * User: Richard Gu
 * Date: 7/19/13
 * Time: 2:04 PM
 */

public class TwitterFeedTopology {

    public static void main(String[] args) throws InterruptedException {

        TopologyBuilder tb = new TopologyBuilder();
        tb.setSpout("twitterSpout", new TwitterStreamSpout(), 2).setNumTasks(3);
        //route all input to one bolt so they can be counted
        tb.setBolt("twitterFilterBolt", new TwitterContentFilter(), 1).globalGrouping("twitterSpout");

        Config cfg = new Config();
        cfg.setDebug(false);

        LocalCluster lc = new LocalCluster();
        lc.submitTopology("Twitter Topology", cfg, tb.createTopology());
        Thread.sleep(60000);
        lc.shutdown();

    }


}
