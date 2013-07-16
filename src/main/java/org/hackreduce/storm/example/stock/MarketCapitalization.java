package org.hackreduce.storm.example.stock;

import backtype.storm.Config;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.spout.SchemeAsMultiScheme;
import backtype.storm.tuple.Fields;
import com.google.common.collect.ImmutableList;
import org.hackreduce.storm.example.common.Common;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import storm.kafka.StringScheme;
import storm.kafka.trident.TransactionalTridentKafkaSpout;
import storm.kafka.trident.TridentKafkaConfig;
import storm.trident.TridentTopology;
import storm.trident.operation.BaseFunction;
import storm.trident.operation.CombinerAggregator;
import storm.trident.operation.TridentCollector;
import storm.trident.testing.MemoryMapState;
import storm.trident.tuple.TridentTuple;

public class MarketCapitalization {

    /**
     * Extracts the market cap and stock symbol from a formatted string.
     *
     * Here's the csv format:
     * exchange,stock_symbol,date,stock_price_open,stock_price_high,stock_price_low,stock_price_close,stock_volume,stock_price_adj_close
     */
    public static class ExtractStockData extends BaseFunction {

        private static final Logger LOG = LoggerFactory.getLogger(ExtractStockData.class);

        @Override
        public void execute(TridentTuple tuple, TridentCollector collector) {

            String[] components = tuple.getString(0).split(",");

            try {
                String symbol = components[1];
                double volume = Double.parseDouble(components[6]);
                double price = Double.parseDouble(components[7]);

                collector.emit(
                    ImmutableList.<Object>of(symbol, volume * price)
                );
            } catch (IndexOutOfBoundsException ioobe) {
                LOG.warn("Invalid input row", ioobe);
            } catch (NumberFormatException nfe) {
                LOG.warn("Could not parse numeric value", nfe);
            }
        }
    }

    /**
     * Aggregates a series of doubles by finding their maximum.
     *
     * Used to find the maximum historical market cap for each stock.
     */
    public static class MaxValue implements CombinerAggregator<Double> {

        @Override
        public Double init(TridentTuple tuple) {
            return tuple.getDouble(0);
        }

        @Override
        public Double combine(Double val1, Double val2) {
            return Math.max(val1, val2);
        }

        @Override
        public Double zero() {
            return Double.NEGATIVE_INFINITY;
        }
    }

    /**
     * Log each new value for the market cap.
     */
    public static class LogInput extends BaseFunction {

        private static final Logger LOG = LoggerFactory.getLogger(LogInput.class);

        @Override
        public void execute(TridentTuple objects, TridentCollector tridentCollector) {
            String symbol = objects.getString(0);
            Double maxMarketCap = objects.getDouble(1);
            LOG.info("Largest seen market cap for {} is {}", symbol, maxMarketCap);
        }
    }

    public static void main(String[] args) throws AlreadyAliveException, InvalidTopologyException {

        String teamName = args[0];

        Config config = new Config();

        TridentKafkaConfig spoutConfig = new TridentKafkaConfig(
            Common.getKafkaHosts(),
            "nyse"
        );

        spoutConfig.scheme = new SchemeAsMultiScheme(new StringScheme());

        TridentTopology builder = new TridentTopology();

        builder
            .newStream(teamName + "-lines", new TransactionalTridentKafkaSpout(spoutConfig))
            .each(new Fields("str"), new ExtractStockData(), new Fields("symbol", "market_cap"))
            .groupBy(new Fields("symbol"))
            .persistentAggregate(
                new MemoryMapState.Factory(),
                new Fields("market_cap"),
                new MaxValue(),
                new Fields("max_market_cap")
            )
            .newValuesStream()
            .each(new Fields("symbol", "max_market_cap"), new LogInput(), new Fields("never_emits"));

        StormSubmitter.submitTopology(
            teamName + "-market-cap",
            config,
            builder.build()
        );
    }
}
