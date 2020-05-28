package com.chloneda.jutils.elasticsearch;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.metadata.MetaData;
import org.junit.Before;
import org.junit.Test;

import java.net.UnknownHostException;
import java.util.Map;
import java.util.Set;

/**
 * @Created by chloneda
 * @Description:
 */
public class ElasticsearchTransportClientTest {
    
    private TransportClient client;

    @Before
    public void init() throws UnknownHostException {
        EsConfig esConfig = new EsConfig();
        esConfig.setHost("172.21.8.28");
        esConfig.setPort(9300);
        esConfig.setSniff(false);
        esConfig.setClusterName("elastic");
        client = ElasticsearchTransportClient.initClient(esConfig);
    }

    @Test
    public void testGetAllIndexes() {
        Set set = ElasticsearchTransportClient.getAllIndexes();
        System.out.println("All index:{} " + set);
    }

    @Test
    public void testIsExitIndex() {
        boolean exit = ElasticsearchTransportClient.isExitIndex("chl_test");
        System.out.println("The index is exit:{} " + exit);
    }

    @Test
    public void testIsExistsIndexType() {
        boolean exit = ElasticsearchTransportClient.isExistsIndexType("chl_test", "person");
        System.out.println("The index type is exit:{} " + exit);
    }

    @Test
    public void testGetMetaData() {
        MetaData metaData = ElasticsearchTransportClient.getMetaData();
        System.out.println("The ES metaData is :{} " + metaData.clusterUUID());
    }

    @Test
    public void testCreateIndex() {
        boolean isCreated = ElasticsearchTransportClient.createIndex("http_log");
        System.out.println("The ES index is created:{} " + isCreated);
    }

    @Test
    public void testCreateType() {
        boolean isCreateType = ElasticsearchTransportClient.createType("http_log", "log");
        System.out.println("The ES index type is created:{} " + isCreateType);
    }

    @Test
    public void testGetIndexAllTypes() {
        Set typeSet = ElasticsearchTransportClient.getIndexAllTypes("http_log");
        System.out.println("The ES index all type is :{} " + typeSet);
    }

    @Test
    public void testDeleteIndex() {
        boolean isDeleted = ElasticsearchTransportClient.deleteIndex("http_log");
        System.out.println("The ES index is deleted:{} " + isDeleted);
    }

    @Test
    public void testDeleteIndexWithType() {//?
        boolean isDeleted = ElasticsearchTransportClient.deleteIndexWithType("http_log", "log");
        System.out.println("The ES index with type is deleted:{} " + isDeleted);
    }

    @Test
    public void testGetFieldsIndexTypes() {
        Map map = ElasticsearchTransportClient.getFieldsIndexTypes("172.21.8.28", 9200, "chl_test", "person");
        System.out.println("The ES index fields is :{} " + map);
    }

    @Test
    public void testIsConnected() {
        boolean isConnected = ElasticsearchTransportClient.isConnected();
        System.out.println("The ES is connected:{} " + isConnected);
    }

}
