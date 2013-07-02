package com.vmc.spouts;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

import com.vmc.common.Utils;

/**
 * 
 * Based on http://www.datasalt.com/2012/01/real-time-feed-processing-with-storm/ 
 * with a few differences
 * 1) Modified for simplicity and to demo some of the subtleties of Storm (such as acking and failing for tuples).
 * 2) Updated to use 0.8.2 (this project uses 0.6) 
 *
 */
public class CraigsListSpout extends BaseSpout
{
  
  private Map<String, String> lookup;
  
  private Values[] tuples;
  private Queue<Values> feedQueue = new LinkedList<Values>();
  
  public CraigsListSpout(Map<String, String> lookup)
  {
    this.lookup = lookup;
    this.tuples = Utils.mapToTuples(lookup);
  }
  
  @Override
  public void open(Map conf, TopologyContext context, SpoutOutputCollector collector)
  {
    super.open(conf, context, collector);
    this.setup();
  }
  
  @Override
  public void close()
  {
    super.close();
    this.teardown();
  }
  
  private void setup()
  {for (Values tuple: this.tuples) this.feedQueue.add(tuple);}
  
  private void teardown()
  {this.feedQueue.clear();}

  @Override
  public void nextTuple()
  {
    Values feed = feedQueue.poll();
    if (feed != null)
    {
      this.collector.emit(feed, feed.get(0));
      //System.err.println("Feed for [" + feed.get(0) + "] is [" + feed.get(1) + "].");
    }
  }
  
  @Override
  public void ack(Object city)
  {
    super.ack(city);
    String url = this.lookup.get(city);
    this.tryagain((String)city);
  }
  
  @Override
  public void fail(Object city)
  {
    super.fail(city);
    this.tryagain((String)city);
  }
  
  private void tryagain(String city)
  {
    String url = this.lookup.get(city);
    Values v = new Values(city, url);
    this.feedQueue.add(v);
  }

  @Override
  public void declareOutputFields(OutputFieldsDeclarer declarer)
  {declarer.declare(new Fields("city", "feed"));}

}
