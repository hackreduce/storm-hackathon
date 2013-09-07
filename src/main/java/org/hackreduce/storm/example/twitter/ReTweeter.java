package org.hackreduce.storm.example.twitter;

import static org.hackreduce.storm.HackReduceStormSubmitter.teamPrefix;

import java.util.Map;

import org.hackreduce.storm.HackReduceStormSubmitter;
import org.hackreduce.storm.example.common.Common;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import storm.kafka.KafkaSpout;
import storm.kafka.SpoutConfig;
import twitter4j.Status;
import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.spout.SchemeAsMultiScheme;
import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;

public class ReTweeter {

    public static class ReTweetLogger extends BaseRichBolt {

        private static Logger LOG = LoggerFactory.getLogger(ReTweetLogger.class);

        private OutputCollector collector;

        @Override
        public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
            this.collector = outputCollector;
        }

        @Override
        public void execute(Tuple tuple) {
            Status status = (Status) tuple.getValue(0);
            if(status.isRetweet()) {
              LOG.info("User {} retweeted {}", status.getUser().getScreenName(), status.getRetweetedStatus().getUser().getScreenName());
            }
            collector.ack(tuple);
        }

        @Override
        public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
            // This bolt provides no output
        }
    }
    
    public static void main(String[] args) throws AlreadyAliveException, InvalidTopologyException, InterruptedException {

        TopologyBuilder builder = new TopologyBuilder();

        // Configuration

        Config config = new Config();

        SpoutConfig spoutConfig = new SpoutConfig(
            Common.getKafkaHosts(),
            "twitter-sample", // The Kafka topic name
            "/kafkastorm", // Where to store state in ZK (don't change this)
            teamPrefix("retweet-logger") // Unique id of this spout. This needs to be unique across ALL topologies.
        );
        spoutConfig.scheme = new SchemeAsMultiScheme(new TweetScheme()); // You can parse the tweets yourself if you prefer

        // This tells the spout to start at the very beginning of the data stream
        // If you just want to resume where you left off, remove this line
        spoutConfig.forceStartOffsetTime(-2);

        builder.setSpout("tweets", new KafkaSpout(spoutConfig));

        builder.setBolt("retweet-logger", new ReTweetLogger())
               .shuffleGrouping("tweets");

        // Launch

        if(args.length > 0 && args[0].equalsIgnoreCase("local")) {
            LocalCluster lc = new LocalCluster();
            lc.submitTopology("retweet-logger", config, builder.createTopology());
            Thread.sleep(120000); // 2 minutes
            lc.shutdown();
        } else {
            HackReduceStormSubmitter.submitTopology("retweet-logger", config, builder.createTopology());
        }
        
    }
}
