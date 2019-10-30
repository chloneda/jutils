package com.chloneda.jutils.elasticsearch;

import org.elasticsearch.client.transport.TransportClient;
import org.junit.Before;
import org.junit.Test;

import java.net.UnknownHostException;
import java.util.Set;

/**
 * Created by chloneda
 * Description:
 */
public class ElasticsearchUtilsTest {
    private TransportClient client;

    @Before
    public void init() throws UnknownHostException {
        EsConfig esConfig = new EsConfig();
        esConfig.setHost("172.21.8.28");
        esConfig.setPort(9300);
        esConfig.setSniff(false);
        esConfig.setClusterName("elastic");
        client = ElasticsearchUtils.initClient(esConfig);
    }

    @Test
    public void testGetAllIndices() {
        Set set = ElasticsearchUtils.getAllIndices();
        System.out.println("All index:{} " + set);
    }

    @Test
    public void testIsExitIndice() {
        boolean exit = ElasticsearchUtils.isExitIndice("chl_test");
        System.out.println("The index is exit:{} " + exit);
    }

    @Test
    public void testIsExistsIndiceType() {
        boolean exit = ElasticsearchUtils.isExistsIndiceType("chl_test", "person");
        System.out.println("The index type is exit:{} " + exit);
    }


}
