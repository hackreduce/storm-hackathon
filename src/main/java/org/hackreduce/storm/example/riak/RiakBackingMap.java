package org.hackreduce.storm.example.riak;

import backtype.storm.task.IMetricsContext;
import com.basho.riak.client.IRiakClient;
import com.basho.riak.client.RiakFactory;
import com.basho.riak.client.bucket.Bucket;
import com.basho.riak.client.cap.DefaultRetrier;
import com.basho.riak.client.query.MultiFetchFuture;
import com.basho.riak.client.raw.pbc.PBClientConfig;
import com.basho.riak.client.raw.pbc.PBClusterConfig;
import com.google.common.base.Joiner;
import storm.trident.state.State;
import storm.trident.state.map.IBackingMap;
import storm.trident.state.map.NonTransactionalMap;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class RiakBackingMap<T> implements IBackingMap<T> {

    private static final char KEY_JOIN_CHAR = ':';

    private final Class<T> clazz;
    private final Bucket bucket;

    public RiakBackingMap(String bucketName, List<String> hosts, int port, Class<T> clazz) {

        this.clazz = clazz;

        try {
            PBClusterConfig clusterConfig = new PBClusterConfig(5);

            PBClientConfig clientConfig = new PBClientConfig.Builder()
                    .withPort(port)
                    .build();

            clusterConfig.addHosts(clientConfig, hosts.toArray(new String[hosts.size()]));

            IRiakClient riakClient = RiakFactory.newClient(clusterConfig);

            bucket = riakClient.createBucket(bucketName)
                    .withRetrier(DefaultRetrier.attempts(3))
                    .execute();

        } catch (Exception e) {
            throw new RuntimeException("Exception while talking to Riak!", e);
        }
    }

    @Override
    public List<T> multiGet(List<List<Object>> lists) {
        try {
            ArrayList<String> keys = new ArrayList<String>();
            for (List<Object> keyList : lists) {
                String key = Joiner.on(KEY_JOIN_CHAR).join(keyList);
                keys.add(key);
            }

            List<MultiFetchFuture<T>> futureResults = bucket.multiFetch(keys, clazz)
                    .withRetrier(DefaultRetrier.attempts(3))
                    .execute();

            ArrayList<T> results = new ArrayList<T>();
            for (MultiFetchFuture<T> result : futureResults) {
                results.add(result.get());
            }

            return results;
        } catch (Exception e) {
            throw new RuntimeException("Exception while talking to Riak!", e);
        }
    }

    @Override
    public void multiPut(List<List<Object>> lists, List<T> ts) {

        Iterator<List<Object>> keyIterator = lists.iterator();
        Iterator<T> valueIterator = ts.iterator();

        try {

            while (keyIterator.hasNext() && valueIterator.hasNext()) {
                String key = Joiner.on(KEY_JOIN_CHAR).join(keyIterator.next());
                T value = valueIterator.next();

                bucket.store(key, value)
                        .withRetrier(DefaultRetrier.attempts(3))
                        .execute();
            }
        } catch (Exception e) {
            throw new RuntimeException("Exception while talking to Riak!", e);
        }
    }

    /**
     * Creates a nontransactional state using Riak as the backing store.
     *
     * This is nontransactional for two reasons: it makes it friendlier to users of the HTTP client
     */
    public static class Factory<T> implements storm.trident.state.StateFactory {

        private final String bucket;
        private List<String> hosts;
        private int port;
        private Class<T> clazz;

        public Factory(String bucket, List<String> hosts, int port, Class<T> clazz) {
            this.bucket = bucket;
            this.hosts = hosts;
            this.port = port;
            this.clazz = clazz;
        }

        @Override public State makeState(Map map, IMetricsContext iMetricsContext, int i, int i2) {
            return NonTransactionalMap.build(new RiakBackingMap<T>(bucket, hosts, port, clazz));
        }
    }
}
