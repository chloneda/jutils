package com.chloneda.jutils.kafka;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.ZkConnection;
import org.I0Itec.zkclient.exception.ZkMarshallingError;
import org.I0Itec.zkclient.exception.ZkTimeoutException;
import org.I0Itec.zkclient.serialize.ZkSerializer;

import java.io.UnsupportedEncodingException;

/**
 * @Created by chloneda
 * @Description:
 */
public class KafkaUtils {

    private static final int DEFAULT_SESSION_TIMEOUT = 30000;
    private ZkClient zkClient;
    private ZkConnection zkConnection;

    public KafkaUtils(String zkServers) {
        this(zkServers, DEFAULT_SESSION_TIMEOUT);
    }

    public KafkaUtils(String zkServers, int sessionTimeOut) {
        this.zkConnection = new ZkConnection(zkServers, sessionTimeOut);
        try {
            this.zkClient = new ZkClient(zkConnection, DEFAULT_SESSION_TIMEOUT, new ZkSerializer() {
                @Override
                public byte[] serialize(Object data) throws ZkMarshallingError {
                    try {
                        return String.valueOf(data).getBytes("utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    return new byte[0];
                }

                @Override
                public Object deserialize(byte[] bytes) throws ZkMarshallingError {
                    if (null == bytes) {
                        return null;
                    } else {
                        try {
                            return new String(bytes, "utf-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                    return null;
                }
            });
        } catch (ZkTimeoutException var) {
            zkConnection = null;
            var.printStackTrace();
        }
    }

    public KafkaUtils(ZkConnection connection) {
        this.zkConnection = connection;
        try {
            this.zkClient = new ZkClient(zkConnection, DEFAULT_SESSION_TIMEOUT, new ZkSerializer() {
                @Override
                public byte[] serialize(Object data) throws ZkMarshallingError {
                    try {
                        return String.valueOf(data).getBytes("utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    return new byte[0];
                }

                @Override
                public Object deserialize(byte[] bytes) throws ZkMarshallingError {
                    if (null == bytes) {
                        return null;
                    } else {
                        try {
                            return new String(bytes, "utf-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                    return null;
                }
            });
        } catch (ZkTimeoutException var) {
            zkConnection = null;
            var.printStackTrace();
        }
    }

    public boolean isConnected() {
        try {
            return zkConnection != null;
        } finally {
            if (zkConnection != null) {
                close();
            }
        }
    }

    public void close() {
        try {
            if (null != zkConnection) {
                zkConnection.close();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public ZkClient getZkClient() {
        return zkClient;
    }

    public void setZkClient(ZkClient zkClient) {
        this.zkClient = zkClient;
    }

    public ZkConnection getZkConnection() {
        return zkConnection;
    }

    public void setZkConnection(ZkConnection zkConnection) {
        this.zkConnection = zkConnection;
    }
}
