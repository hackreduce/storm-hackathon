package org.meetup.storm.hackathon.bolt;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import org.mortbay.xml.XmlParser;

import java.util.Arrays;

public class GnipEventTransformBolt extends BaseBasicBolt {
  @Override
  public void execute(Tuple tuple, BasicOutputCollector basicOutputCollector) {
    XmlParser.Node node = (XmlParser.Node) tuple.getValueByField("gnip_event");
    String title = node.getString("title", false, false);
    String href = node.get("link").getAttribute("href");
    basicOutputCollector.emit("msgs", Arrays.asList((Object) (title + " ---> " + href)));
  }

  @Override
  public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
    outputFieldsDeclarer.declareStream("msgs", new Fields("msg"));
  }
}
