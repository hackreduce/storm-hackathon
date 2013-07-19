package org.hackreduce.storm.vmc.spouts;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;


import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * Created with IntelliJ IDEA.
 * User: Richard Gu
 * Date: 7/19/13
 * Time: 2:05 PM
 */


public class TwitterStreamSpout extends BaseSpout {

    private static final String consumerKey = "bW7WjFbtGBmTri2NmzQ";
    private static final String consumerSecret = "N1bz4Sncho5X8Fipa85GBFveKecrxRINQh3iFqWaEg";

    private static final String accessToken = "15270429-EyvSABg3Pv3ooNZORAQmafZuMo0myLMTM6viWoSVj";
    private static final String accessSecret = "V9DJBuU3S1S04Ne9sJPFEKzQRwKWXGuuc5OAk3RQZk";

    private static final  String feedUrl = "https://stream.twitter.com/1.1/statuses/sample.json";


    private OAuthConsumer consumer;

    @Override
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        super.open(conf, context, collector);
        this.setup();
    }

    @Override
    public void close() {
        super.close();
    }

    private void setup() {
        consumer = new CommonsHttpOAuthConsumer(consumerKey, consumerSecret);
        consumer.setTokenWithSecret(accessToken, accessSecret);
    }


    @Override
    public void nextTuple() {

        try {

            HttpGet request = new HttpGet(feedUrl);
            consumer.sign(request);

            DefaultHttpClient client = new DefaultHttpClient();
            HttpResponse response = client.execute(request);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent()));

            while (true) {
                String line = reader.readLine();
                if (line == null)
                    break;
                else {
                    this.collector.emit(new Values(line), line);
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        System.out.println("Sleeping before reconnect...");

        try {
            Thread.sleep(15000);
        } catch (Exception e) {
        }


    }

    @Override
    public void ack(Object obj) {
        super.ack(obj);
    }

    @Override
    public void fail(Object obj) {
        super.fail(obj);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("line"));
    }


}
