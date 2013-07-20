package org.hackreduce.storm.example.stock;

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
import com.google.common.collect.ImmutableSet;
import org.hackreduce.storm.example.common.Common;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import storm.kafka.KafkaSpout;
import storm.kafka.SpoutConfig;
import storm.kafka.StringScheme;

import java.util.Map;
import java.util.Set;

import static org.hackreduce.storm.HackReduceStormSubmitter.teamPrefix;

public class StockLoggerLocal {

  public static void main(String[] args) throws AlreadyAliveException, InvalidTopologyException {

    TopologyBuilder builder = new TopologyBuilder();

    Config config = new Config();


    SpoutConfig spoutConfig = new SpoutConfig(
        Common.getKafkaHosts(),     // The locations of the kafka servers
        "stock_daily_prices",       // The topic (queue) to consume
        "/kafkastorm",              // The root location in zookeeper where progress is stored
        teamPrefix("stock-logger")  // A globally-unique name for this particular spout
    );

    // The data is serialized as a string
    spoutConfig.scheme = new SchemeAsMultiScheme(new StringScheme());

    // This tells the spout to start at the very beginning of the data stream
    // If you just want to resume where you left off, remove this line
    spoutConfig.forceStartOffsetTime(-2);

    // If we're passed a specific list of stocks, pay attention to only those stocks
    ImmutableSet<String> stocksToWatch = ImmutableSet.copyOf(args);

    builder.setSpout("stocks", new KafkaSpout(spoutConfig));

    builder.setBolt("stock-logger", new LoggerBolt(stocksToWatch))
        .shuffleGrouping("stocks");

    new LocalCluster().submitTopology("stock-logger", config, builder.createTopology());
//        HackReduceStormSubmitter.submitTopology("stock-logger", config, builder.createTopology());
  }

  /**
   * Logs some info about the stock if it's one of our 'stocks to watch'.
   * <p/>
   * Here's the csv format:
   * exchange,stock_symbol,date,stock_price_open,stock_price_high,stock_price_low,stock_price_close,stock_volume,stock_price_adj_close
   */
  public static class LoggerBolt extends BaseRichBolt {

    private static Logger LOG = LoggerFactory.getLogger(LoggerBolt.class);
    private OutputCollector collector;
    private Set<String> stocksToWatch;

    public LoggerBolt(Set<String> stocksToWatch) {
      this.stocksToWatch = stocksToWatch;
    }

    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
      this.collector = outputCollector;
    }

    @Override
    public void execute(Tuple tuple) {
      String[] components = tuple.getString(0).split(",");

      try {
        String symbol = components[0] + ":" + components[1]; // eg. NYSE:GCH

        if (stocksToWatch.isEmpty() || stocksToWatch.contains(symbol)) {
          String date = components[2];
          double high = Double.parseDouble(components[4]);
          double low = Double.parseDouble(components[5]);

          LOG.info("Got info for {} on {}: high={} low={}", new Object[]{symbol, date, high, low});
        }
      } catch (IndexOutOfBoundsException ioobe) {
        LOG.warn("Invalid input row", ioobe);
      } catch (NumberFormatException nfe) {
        LOG.warn("Could not parse numeric value", nfe);
      } finally {
        // All of our failures are permanent, so failing the tuple would just make it fail over and over
        collector.ack(tuple);
      }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
      // This bolt provides no output
    }
  }
}
