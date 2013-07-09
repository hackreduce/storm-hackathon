package org.meetup.storm.hackathon.bolt;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import org.mortbay.xml.XmlParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GnipEdcRequestBolt extends BaseBasicBolt {
  private static Logger LOG = LoggerFactory.getLogger(GnipEdcRequestBolt.class);
  private String gnipConnectionUrl;
  private String gnipConnectionLogin;
  private String gnipConnectionPassword;
  private String refreshUrl;

  public GnipEdcRequestBolt(String gnipConnectionUrl, String gnipConnectionLogin, String gnipConnectionPassword) {
    this.gnipConnectionUrl = gnipConnectionUrl;
    this.gnipConnectionLogin = gnipConnectionLogin;
    this.gnipConnectionPassword = gnipConnectionPassword;
  }

  @Override
  public void execute(Tuple tuple, BasicOutputCollector basicOutputCollector) {
    try {
      for (XmlParser.Node event : requestGnipData()) {
        basicOutputCollector.emit("gnip_events", Arrays.asList((Object) event));
      }
    } catch (GnipRequestException e) {
      LOG.error("GNIP Request Failed", e);
    }
  }

  @Override
  public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
    outputFieldsDeclarer.declareStream("gnip_events", new Fields("gnip_event"));
  }

  private List<XmlParser.Node> requestGnipData() throws GnipRequestException {
    URLConnection uc;
    URL connectionUrl;
    try {
      connectionUrl = refreshUrl == null ? new URL(gnipConnectionUrl) : new URL(refreshUrl);
    } catch (MalformedURLException e) {
      throw new GnipRequestException("Failed parsing connection URL", e);
    }
    try {
      uc = connectionUrl.openConnection();
    } catch (IOException e) {
      throw new GnipRequestException("Failed opening connection", e);
    }
    String authorizationString = "Basic " + Base64.encode((gnipConnectionLogin + ":" + gnipConnectionPassword).getBytes());
    uc.setRequestProperty("Authorization", authorizationString);
    InputStream in = null;
    try {
      try {
        in = uc.getInputStream();
      } catch (IOException e) {
        throw new GnipRequestException("Failed opening input stream", e);
      }
      XmlParser p = new XmlParser();
      XmlParser.Node doc;
      try {
        doc = p.parse(in);
      } catch (IOException e) {
        throw new GnipRequestException("Failed reading input stream", e);
      } catch (SAXException e) {
        throw new GnipRequestException("Failed parsing input stream", e);
      }
      refreshUrl = doc.getAttribute("refreshURL");
      List<XmlParser.Node> result = new ArrayList<XmlParser.Node>();
      for (int i = 0; i < doc.size(); i++) {
        Object nodeObj = doc.get(i);
        if (nodeObj instanceof XmlParser.Node) {
          XmlParser.Node node = (XmlParser.Node) nodeObj;
          result.add(node);
        }
      }
      return result;
    } finally {
      if (in != null) {
        try {
          in.close();
        } catch (IOException e) {
        }
      }
    }
  }
}
