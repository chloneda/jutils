package com.chloneda.jutils.elasticsearch;

import com.chloneda.jutils.commons.CheckUtils;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by chloneda
 * Description:
 */
public class ElasticSearchUtils {

    private static TransportClient client;

    public ElasticSearchUtils(EsConfig esConfig) throws UnknownHostException {
        // 连接集群的设置
        Settings settings = Settings.builder()
                .put("cluster.name", esConfig.getClusterName())
                .put("client.transport.sniff", esConfig.isSniff())
                .build();
        client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new TransportAddress(InetAddress.getByName(esConfig.getHost()), esConfig.getPort()));
    }

    public static TransportClient getClient(){
        return CheckUtils.requireNotNull(client,"The client is null !");
    }

}
