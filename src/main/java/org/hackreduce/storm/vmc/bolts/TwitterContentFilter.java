package org.hackreduce.storm.vmc.bolts;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import org.hackreduce.storm.vmc.common.BaseComponent;


import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Richard Gu
 * Date: 7/19/13
 * Time: 2:26 PM
 */
public class TwitterContentFilter extends BaseComponent implements IRichBolt {

    private OutputCollector collector;

    private TwitterSentimentAnalyzer classifier;

    @Override
    public void prepare(Map conf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
        this.conf = conf;
        this.classifier = new TwitterSentimentAnalyzer();
    }

    @Override
    public void execute(Tuple input) {

        String line = input.getValueByField("line").toString();
        boolean happy = classifier.classify(line);
        System.out.println(happy + " *** " + line);

    }

    @Override
    public void cleanup() {

    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("line", "content"));
    }


}
