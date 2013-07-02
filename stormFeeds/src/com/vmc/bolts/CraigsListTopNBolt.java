package com.vmc.bolts;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import com.vmc.common.BaseComponent;
import com.vmc.common.Utils;

public class CraigsListTopNBolt extends BaseComponent implements IRichBolt
{

  private Map<String, List<Values>> topNLookup;
  private OutputCollector collector;
  private int n;
  private Pattern p;
  private String name;
  
  public CraigsListTopNBolt(int n, String regex, String name)
  {
    this.n = n;
    this.p = Pattern.compile(regex);
    this.name = name;
  }
  
  @Override
  public void prepare(Map conf, TopologyContext context, OutputCollector collector)
  {
    this.collector = collector;
    this.conf = conf;
    topNLookup = new ConcurrentHashMap<String, List<Values>>();
  }

  @Override
  public void execute(Tuple input)
  {
    
    if(input.getSourceStreamId().equals("__tick")) 
    {
      this.printout();
      return;
    }
    
    String city = input.getStringByField("city");
    String title = input.getStringByField("title");
    String link = input.getStringByField("link");
    String groupBy = input.getStringByField("groupBy");
    Matcher m = this.p.matcher(title);
    if (!m.find() && m.groupCount() > 0) return;
    String num = m.group(1);
    try
    {
      int x = Integer.parseInt(num);
      Values v = new Values(city, title, link, x);
      if (!this.topNLookup.containsKey(groupBy))
        this.topNLookup.put(groupBy, new LinkedList<Values>());
      List<Values> q = this.topNLookup.get(groupBy);
      //emit only if the Top N list changed
      if (Utils.setTopN(q, v, this.n))
        this.collector.emit(new Values(city, title, link, "_OVERALL_"));
    }
    catch(NumberFormatException nfe)
    {return;}
  }

  @Override
  public void cleanup() {}

  @Override
  public void declareOutputFields(OutputFieldsDeclarer declarer)
  {declarer.declare(new Fields("city", "title", "link", "groupBy"));}
  
  private void printout()
  {System.err.println(Utils.displayTopN(this.topNLookup, this.name));}

}
