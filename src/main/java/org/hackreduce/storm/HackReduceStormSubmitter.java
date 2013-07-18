package org.hackreduce.storm;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.google.common.base.Charsets;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.io.Resources;

import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.generated.StormTopology;

public class HackReduceStormSubmitter {

    public static void submitTopology(String name, Map<?,?> conf, StormTopology topology) throws AlreadyAliveException, InvalidTopologyException {
        StormSubmitter.submitTopology(teamPrefix(name), conf, topology);
    }

    public static String teamPrefix(String name) {
        return String.format("%s-%s", teamName(), name);
    }

    public static String teamName() {
        String envTeamName = System.getenv("TEAM_NAME");
        String propTeamName = System.getProperty("TEAM_NAME", randomName());

        return envTeamName != null ? envTeamName : propTeamName;
    }

    private static Random rnd = new Random();

    private static String randomName() {
    	List<String> n = someNames();
    	return n.get(rnd.nextInt(n.size()));
    }

    private static final List<String> names = Lists.newArrayList();

    private static List<String> someNames() {
    	try {
        	if(names.isEmpty()) {
        		names.addAll(
        		    Lists.transform(
        		        Resources.readLines(Resources.getResource(HackReduceStormSubmitter.class, "names.txt"), Charsets.UTF_8),
        		        new Function<String, String>() {
                            public String apply(String str) {
                                return str.toLowerCase().trim();
                            }
                        }
                    )
                );
        	}
    	} catch(IOException e) {
    		names.add("error-reading-names");
    	}
    	return names;
    }

}
