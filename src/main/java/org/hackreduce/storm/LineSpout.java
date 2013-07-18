package org.hackreduce.storm;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * This spout reads data from a CSV file. It is only suitable for testing in local mode
 */
public class LineSpout extends BaseRichSpout {
  private final String fileName;
  private SpoutOutputCollector _collector;
  private BufferedReader reader;
  private AtomicLong linesRead;

  public LineSpout(String filename) {
    this.fileName = filename;
    linesRead = new AtomicLong(0);
  }

  @Override
  public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
    _collector = collector;
    try {
      reader = new BufferedReader(new FileReader(fileName));
      // read and ignore the header if one exists
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void nextTuple() {
    try {
      String line = reader.readLine();
      if (line != null) {
        long id = linesRead.incrementAndGet();
        _collector.emit(new Values(line), id);
      } else
        System.out.println("Finished reading file, " + linesRead.get() + " lines read");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void ack(Object id) {
  }

  @Override
  public void fail(Object id) {
    System.err.println("Failed line number " + id);
  }

  @Override
  public void declareOutputFields(OutputFieldsDeclarer declarer) {
    // read csv header to get field info
    declarer.declare(new Fields("line"));
  }

}