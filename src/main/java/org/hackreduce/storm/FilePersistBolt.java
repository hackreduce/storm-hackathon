package org.hackreduce.storm;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

/**
 * Date: 7/19/13
 * Time: 8:57 PM
 *
 * @author ikaplun
 */
public class FilePersistBolt extends BaseRichBolt {
  private static final Logger LOG = LoggerFactory.getLogger(FilePersistBolt.class);
  private BufferedWriter writer;

  @Override
  public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
    String filepath = (String) map.get("persist.file");
    String absoluteFileName = filepath+"."+topologyContext.getThisTaskIndex();
    try {
      writer = new BufferedWriter(new FileWriter(absoluteFileName));
    } catch (IOException e) {
      // this will propagate the error to storm
      throw new RuntimeException("Problem opening file " +absoluteFileName,e);
    }
  }

  @Override
  public void execute(Tuple tuple) {
    String line = tuple.getStringByField("line");
    LOG.info(line);
    try {
      writer.write(line);
      writer.flush();
    } catch (IOException e) {
      throw new RuntimeException("Problem writing to file",e);
    }
  }

  @Override
  public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
    //nothing here since we are not emitting anything
  }
}
