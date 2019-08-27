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
public class ElasticSearchUtilsTest {
    private ElasticSearchUtils elasticSearchUtils;
    private TransportClient client;

    @Before
    public void init() throws UnknownHostException {
        EsConfig esConfig = new EsConfig();
        esConfig.setHost("172.21.8.28");
        esConfig.setPort(9300);
        esConfig.setSniff(false);
        esConfig.setClusterName("elastic");
        elasticSearchUtils = new ElasticSearchUtils(esConfig);
        client = elasticSearchUtils.getClient();
    }

    @Test
    public void testGetAllIndices() {
        Set set=elasticSearchUtils.getAllIndices();
        System.out.println("All index:{} "+set);
    }

    @Test
    public void testIsExitIndice(){
        boolean exit=elasticSearchUtils.isExitIndice("chl_test");
        System.out.println("The index is exit:{} "+ exit);
    }


}
