package org.hackreduce.storm.example.common;

import backtype.storm.Config;
import backtype.storm.utils.Utils;
import com.basho.riak.client.IRiakClient;
import com.basho.riak.client.RiakException;
import com.basho.riak.client.RiakFactory;
import com.basho.riak.client.raw.RiakClientFactory;
import com.basho.riak.client.raw.http.HTTPClientConfig;
import com.basho.riak.client.raw.http.HTTPClusterConfig;
import com.basho.riak.client.raw.http.HTTPRiakClientFactory;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import storm.kafka.HostPort;
import storm.kafka.KafkaConfig;

import java.util.List;
import java.util.Map;

public class Common {

    private static final Logger LOG = LoggerFactory.getLogger(Common.class);

    public static KafkaConfig.BrokerHosts getKafkaHosts() {

        Map config = Utils.readStormConfig();

        List servers = (List) config.get(Config.STORM_ZOOKEEPER_SERVERS);
        Integer port = (Integer) config.get(Config.STORM_ZOOKEEPER_PORT);

        StringBuilder buff = new StringBuilder();
        for (Object serverName : servers) {
            buff.append(serverName);
            buff.append(':');
            buff.append(port);
            buff.append(',');
        }
//        String zkString = buff.substring(0, buff.length() - 1);
        String zkString="cluster-7-slave-04.sl.hackreduce.net:2181,cluster-7-slave-02.sl.hackreduce.net:2181,cluster-7-slave-01.sl.hackreduce.net:2181";

        LOG.info("Pulled connection string from storm config: " + zkString);

        return new KafkaConfig.ZkHosts(zkString, "/brokers");
    }

    /**
     * TODO: Load from config...
     */
    public static List<String> getRiakHosts() {
        return ImmutableList.of(
            "cluster-7-slave-00.sl.hackreduce.net",
            "cluster-7-slave-02.sl.hackreduce.net",
            "cluster-7-slave-03.sl.hackreduce.net",
            "cluster-7-slave-06.sl.hackreduce.net"
        );
    }

    public static int getRiakPort() { return 8087; }
}
