package org.hackreduce.storm;

import au.com.bytecode.opencsv.CSVReader;
import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * This spout reads data from a CSV file. It is only suitable for testing in local mode
 */
public class CsvSpout extends BaseRichSpout {
  private final String fileName;
  private final char separator;
  private boolean includesHeaderRow;
  private SpoutOutputCollector _collector;
  private CSVReader reader;
  private AtomicLong linesRead;

  public CsvSpout(String filename, char separator, boolean includesHeaderRow) {
    this.fileName = filename;
    this.separator = separator;
    this.includesHeaderRow = includesHeaderRow;
    linesRead=new AtomicLong(0);
  }

  @Override
  public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
    _collector = collector;
    try {
      reader = new CSVReader(new FileReader(fileName), separator);
      // read and ignore the header if one exists
      if (includesHeaderRow) reader.readNext();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void nextTuple() {
    try {
      String[] line = reader.readNext();
      if (line != null) {
        long id=linesRead.incrementAndGet();
        _collector.emit(new Values(line),id);
      }
      else
        System.out.println("Finished reading file, "+linesRead.get()+" lines read");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void ack(Object id) {
  }

  @Override
  public void fail(Object id) {
    System.err.println("Failed tuple with id "+id);
  }

  @Override
  public void declareOutputFields(OutputFieldsDeclarer declarer) {
    try {
      CSVReader reader = new CSVReader(new FileReader(fileName), separator);
      // read csv header to get field info
      String[] fields = reader.readNext();
      if (includesHeaderRow) {
        System.out.println("DECLARING OUTPUT FIELDS");
        for (String a : fields)
          System.out.println(a);

        declarer.declare(new Fields(Arrays.asList(fields)));
      } else {
        // if there are no headers, just use field_index naming convention
        ArrayList<String> f= new ArrayList<String>(fields.length);
        for (int i = 0; i < fields.length; i++) {
          f.add("field_"+i);
        }
        declarer.declare(new Fields(f));
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

}