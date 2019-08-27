package com.chloneda.jutils.elasticsearch;

import com.chloneda.jutils.commons.CheckUtils;
import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.admin.indices.stats.IndicesStatsRequest;
import org.elasticsearch.action.admin.indices.stats.IndicesStatsResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Set;

/**
 * Created by chloneda
 * Description:
 */
public class ElasticSearchUtils {
    private static final Logger LOGGER= LoggerFactory.getLogger(ElasticSearchUtils.class);

    private static TransportClient client;
    private EsConfig esConfig;

    public ElasticSearchUtils(EsConfig esConfig) {
        this.esConfig = esConfig;
        initClient(esConfig);
    }

    public boolean initClient(EsConfig esConfig) {
        boolean flag = false;
        // 连接集群的设置
        Settings settings = Settings.builder()
                .put("cluster.name", esConfig.getClusterName())
                .put("client.transport.sniff", esConfig.isSniff())
                .build();
        try {
            client = new PreBuiltTransportClient(settings)
                    .addTransportAddress(new TransportAddress(InetAddress.getByName(esConfig.getHost()), esConfig.getPort()));
            flag = true;
        } catch (UnknownHostException e) {
            flag = false;
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 获取所有索引
     *
     * @return
     */
    public Set<String> getAllIndices() {
        ActionFuture<IndicesStatsResponse> isr= getClient()
                .admin()
                .indices()
                .stats(new IndicesStatsRequest().all());
        Set<String> indices=isr.actionGet().getIndices().keySet();
        return indices;
    }

    /**
     * 判断指定的索引名是否存在
     *
     * @param indexName 索引名
     * @return
     */
    public boolean isExitIndice(String indexName){
        IndicesExistsResponse response=getClient()
                .admin()
                .indices()
                .exists(new IndicesExistsRequest(new String[]{indexName}))
                .actionGet();
        return response.isExists();
    }


    public TransportClient getClient() {
        CheckUtils.requireNotNull(client, "The client is null !");
        return client;
    }

    public void destory() {
        CheckUtils.requireNotNull(client, "The client is null !");
        client.close();
    }

    public void test(){
        GetResponse gresponse = client.prepareGet("chl_test", "person", "1").get();
        System.out.println(gresponse.getFields());
        SearchResponse response = client.prepareSearch("chl_test")
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(QueryBuilders.termQuery("age", "18"))                 // Query
                .setPostFilter(QueryBuilders.rangeQuery("age").from(12).to(20))     // Filter
                .setFrom(0).setSize(60).setExplain(true)
                .get();
        System.out.println(response.toString());
    }

}
