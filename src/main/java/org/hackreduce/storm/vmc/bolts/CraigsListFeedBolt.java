package org.hackreduce.storm.vmc.bolts;

import java.net.URL;
import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.fetcher.FeedFetcher;
import com.sun.syndication.fetcher.impl.HttpURLFeedFetcher;
import org.hackreduce.storm.vmc.common.BaseComponent;

/**
 * 
 * Based on http://www.datasalt.com/2012/01/real-time-feed-processing-with-storm/ 
 * with a few differences
 * 1) Outputs city and the title of the craigs list ad/entry.
 * 2) Updated to use 0.8.2 (this project uses 0.6) 
 *
 */
public class CraigsListFeedBolt extends BaseComponent implements IRichBolt
{
  
  private OutputCollector collector;

  @Override
  public void prepare(Map conf, TopologyContext context, OutputCollector collector)
  {
    this.collector = collector;
    this.conf = conf;
  }

  @Override
  public void execute(Tuple input)
  {
    FeedFetcher feedFetcher = new HttpURLFeedFetcher();
    String feedUrl = input.getStringByField("feed");
    try
    {
      SyndFeed feed = feedFetcher.retrieveFeed(new URL(feedUrl));
      for (Object entry : feed.getEntries())
      {
        SyndEntry se = (SyndEntry)entry;
        String city = input.getString(0);
        //System.out.println("City: [" + city + "] with entry [" + se.getTitle() + "].");
        this.collector.emit(new Values(city, se.getTitle(), se.getLink(), city));
      }
      this.collector.ack(input);
    } 
    catch (Throwable t)
    {
      System.err.println("Problem loading [" + feedUrl + "].");
      t.printStackTrace();
      this.collector.fail(input);
    }
  }

  @Override
  public void cleanup() {}

  @Override
  public void declareOutputFields(OutputFieldsDeclarer declarer)
  {declarer.declare(new Fields("city", "title", "link", "groupBy"));}

}
