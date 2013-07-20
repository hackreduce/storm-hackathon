package org.hackreduce.storm;


import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;
import kafka.javaapi.producer.Producer;
import kafka.producer.ProducerConfig;
import kafka.javaapi.producer.ProducerData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Properties;

public class KafkaPersistBolt extends BaseRichBolt {
  private static final Logger LOG = LoggerFactory.getLogger(KafkaPersistBolt.class);
  private Producer<String,String> producer;
  private String topicName;
  private OutputCollector outputCollector;


  @Override
  public void prepare(Map conf, TopologyContext topologyContext, OutputCollector outputCollector) {
    Properties props = new Properties();
    props.put("zk.connect", "cluster-7-slave-04.sl.hackreduce.net:2181,cluster-7-slave-02.sl.hackreduce.net:2181,cluster-7-slave-01.sl.hackreduce.net:2181");
//    props.put("zk.connect", "127.0.0.1:2181");
    props.put("serializer.class", "kafka.serializer.StringEncoder");
    ProducerConfig config = new ProducerConfig(props);
    producer = new Producer<String, String>(config);
    topicName = (String) conf.get("persist.topic");
    this.outputCollector=outputCollector;
  }

  @Override
  public void execute(Tuple tuple) {
    LOG.info(tuple.getStringByField("line"));
    ProducerData<String, String> data = new ProducerData<String, String>(topicName, tuple.getStringByField("line"));
    producer.send(data);
    outputCollector.ack(tuple);

  }

  @Override
  public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
  }
}
