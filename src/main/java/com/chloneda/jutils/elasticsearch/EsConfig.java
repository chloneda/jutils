package com.chloneda.jutils.elasticsearch;

import java.io.Serializable;

/**
 * @Created by chloneda
 * @Description:
 */
public class EsConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String DEFAULT_ES_CLUSTER_NAME = "elasticsearch";

    private String clusterName;

    /**
     * 自动嗅探,设置为true,将自动嗅探整个集群,自动加入集群的节点到连接列表中。
     */
    private boolean isSniff = false;

    private String host;

    private int port;

    private int restPort;

    private String indexName;

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public boolean isSniff() {
        return isSniff;
    }

    public void setSniff(boolean sniff) {
        isSniff = sniff;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getRestPort() {
        return restPort;
    }

    public void setRestPort(int restPort) {
        this.restPort = restPort;
    }

    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

}
