package org.meetup.storm.hackathon.bolt;


import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GnipEventPersistBolt extends BaseBasicBolt {
  private static final Logger LOG = LoggerFactory.getLogger(GnipEventPersistBolt.class);

  @Override
  public void execute(Tuple tuple, BasicOutputCollector basicOutputCollector) {
    LOG.info(tuple.getStringByField("msg"));
  }

  @Override
  public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
  }
}
