package org.hackreduce.storm.vmc.bolts;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import org.hackreduce.storm.vmc.common.BaseComponent;
import org.hackreduce.storm.vmc.common.Utils;
import org.hackreduce.storm.vmc.common.Whatever;

public class HelloCountBolt extends BaseComponent implements IRichBolt
{
  
  private Map<String, Integer> counts;
  private Map<String, String> lookup;
  
  private OutputCollector collector;
  
  public HelloCountBolt(Map<String, String> lookup)
  {this.lookup = lookup;}

  @Override
  public void prepare(Map conf, TopologyContext context, OutputCollector collector)
  {
    this.collector = collector;
    this.conf = conf;
    counts = new ConcurrentHashMap<String, Integer>();
    Set<String> keys = lookup.keySet();
    for (String key: keys) this.counts.put(key, 0);
  }

  @Override
  public void execute(Tuple input)
  {
    String lang = input.getValueByField(Whatever.LANGUAGEFIELD).toString();
    String hello = input.getValueByField(Whatever.HELLOFIELD).toString();
    this.counts.put(lang, counts.get(lang) + 1);
    this.collector.emit(new Values(lang, hello, this.counts.get(lang)));
  }

  @Override
  public void cleanup()
  {System.out.println(Utils.displayHelloCounts(this.lookup, this.counts));}

  @Override
  public void declareOutputFields(OutputFieldsDeclarer declarer)
  {declarer.declare(new Fields(Whatever.LANGUAGEFIELD, Whatever.HELLOFIELD, "count"));}

}
