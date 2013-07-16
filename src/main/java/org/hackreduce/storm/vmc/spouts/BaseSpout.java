package org.hackreduce.storm.vmc.spouts;

import java.util.Map;

import org.hackreduce.storm.vmc.common.BaseComponent;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichSpout;

public abstract class BaseSpout extends BaseComponent implements IRichSpout
{
  
  protected SpoutOutputCollector collector;
  
  @Override
  public void open(Map conf, TopologyContext context, SpoutOutputCollector collector)
  {
    this.conf = conf;
    this.context = context;
    this.collector = collector;
  }

  @Override public void close() {}
  
  @Override public void activate() {}
  @Override public void deactivate() {}

  @Override public void ack(Object msgId) {}
  @Override public void fail(Object msgId) {}

}
