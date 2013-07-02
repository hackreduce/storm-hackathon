package com.vmc.spouts;

import java.util.Map;
import java.util.Random;

import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

import com.vmc.common.Utils;
import com.vmc.common.Whatever;

public class HelloSpout extends BaseSpout
{

  private Random r;
  private Values[] tuples;  
  
  public HelloSpout(Map<String, String> lookup)
  {
    r = new Random();
    this.tuples = Utils.mapToTuples(lookup);
  }
  
  @Override
  public void nextTuple()
  {
    int i = this.r.nextInt(this.tuples.length);
    this.collector.emit(this.tuples[i]);
    //System.err.println("Hello in [" + this.tuples[i].get(0) + "] is [" + this.tuples[i].get(1) + "].");
  }
  
  @Override
  public void declareOutputFields(OutputFieldsDeclarer declarer)
  {declarer.declare(new Fields(Whatever.LANGUAGEFIELD, Whatever.HELLOFIELD));}
}
