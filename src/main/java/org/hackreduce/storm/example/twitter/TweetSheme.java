package org.hackreduce.storm.example.twitter;

import java.util.List;

import com.google.common.base.Charsets;

import twitter4j.conf.ConfigurationBuilder;
import twitter4j.internal.json.z_T4JInternalFactory;
import twitter4j.internal.json.z_T4JInternalJSONImplFactory;
import twitter4j.internal.org.json.JSONObject;
import backtype.storm.spout.Scheme;
import backtype.storm.tuple.Fields;
import backtype.storm.utils.Utils;

public final class TweetSheme implements Scheme {

	private static final long serialVersionUID = -4589383226205015147L;

	private final z_T4JInternalFactory factory = new z_T4JInternalJSONImplFactory(new ConfigurationBuilder().build());

	@Override
	public List<Object> deserialize(byte[] ser) {
		try {
			return Utils.tuple(factory.createStatus(new JSONObject(new String(ser, Charsets.UTF_8))));
		} catch (Exception e) {
			// ignore non-parsable tweets
			return null;
		}
	}

	@Override
	public Fields getOutputFields() {
        return new Fields("tweet");
    }
	
}