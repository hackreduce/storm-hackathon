package org.hackreduce.storm.example.wikipedia;

import backtype.storm.Config;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.spout.SchemeAsMultiScheme;
import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;
import com.google.common.base.Joiner;
import org.hackreduce.storm.HackReduceStormSubmitter;
import org.hackreduce.storm.example.common.Common;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import storm.kafka.KafkaSpout;
import storm.kafka.SpoutConfig;
import storm.kafka.StringScheme;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static org.hackreduce.storm.HackReduceStormSubmitter.teamPrefix;

public class LinkLogger {

    public static class LinkExtractorBolt extends BaseRichBolt {

        private static Logger LOG = LoggerFactory.getLogger(LinkExtractorBolt.class);

        private static final int MAX_LINKS = 10;
        private static final Pattern TITLE_REGEX = Pattern.compile("<title>(.+?)</title>");
        private static final Pattern LINK_REGEX = Pattern.compile("\\[\\[(.+?)\\]\\]");

        private OutputCollector collector;

        @Override
        public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
            this.collector = outputCollector;
        }

        @Override
        public void execute(Tuple tuple) {
            String content = tuple.getString(0);

            // Get title

            Matcher titleMatcher = TITLE_REGEX.matcher(content);
            String title = (titleMatcher.find()) ? titleMatcher.group(1) : "<unknown>";

            // Get links

            Matcher matcher = LINK_REGEX.matcher(content);
            List<String> links = new ArrayList<String>();

            while (matcher.find() && links.size() < MAX_LINKS) {
                try {
                    String linkTarget = matcher.group(1).split("\\|")[0];
                    links.add(linkTarget);
                } catch (ArrayIndexOutOfBoundsException aioobe) {
                    LOG.warn("Couldn't parse link: " + matcher.group(1));
                }
            }

            LOG.info("Found {} links on page '{}': ({})",
                new Object[]{ links.size(), title, Joiner.on(", ").join(links) }
            );

            collector.ack(tuple);
        }

        @Override
        public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
            // This bolt provides no output
        }
    }

    public static void main(String[] args) throws AlreadyAliveException, InvalidTopologyException {

        TopologyBuilder builder = new TopologyBuilder();

        // Configuration

        Config config = new Config();

        SpoutConfig spoutConfig = new SpoutConfig(
            Common.getKafkaHosts(),
            "wikipedia_articles",
            "/kafkastorm",
            teamPrefix("wikipedia-state")
        );
        spoutConfig.scheme = new SchemeAsMultiScheme(new StringScheme());

        // This tells the spout to start at the very beginning of the data stream
        // If you just want to resume where you left off, remove this line
        spoutConfig.forceStartOffsetTime(-2);


        builder.setSpout("articles", new KafkaSpout(spoutConfig));

        builder.setBolt("link-logger", new LinkExtractorBolt())
               .shuffleGrouping("articles");

        // Launch

        HackReduceStormSubmitter.submitTopology("wikipedia-logger", config, builder.createTopology());
    }
}
