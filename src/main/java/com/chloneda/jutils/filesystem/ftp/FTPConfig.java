package com.chloneda.jutils.filesystem.ftp;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by chloneda
 * Description: 包装FTP的连接基本信息
 */
public class FTPConfig {
    private String hostName;
    private int port;
    private String username;
    private String password;
    private String remoteBaseDir;
    private String localDir;
    private String remoteEncoding;
    private boolean passiveMode;
    //其他配置参数
    private JSONObject conf;

    public FTPConfig(String hostName, int port, String username, String password, String remoteBaseDir, String localDir,
                     String remoteEncoding, boolean passiveMode) {
        this.hostName = hostName;
        this.port = port;
        this.remoteBaseDir = remoteBaseDir;
        this.localDir = localDir;
        this.remoteEncoding = remoteEncoding;
        this.passiveMode = passiveMode;
        this.username = username;
        this.password = password;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRemoteBaseDir() {
        return remoteBaseDir;
    }

    public void setRemoteBaseDir(String remoteBaseDir) {
        this.remoteBaseDir = remoteBaseDir;
    }

    public String getLocalDir() {
        return localDir;
    }

    public void setLocalDir(String localDir) {
        this.localDir = localDir;
    }

    public String getRemoteEncoding() {
        return remoteEncoding;
    }

    public void setRemoteEncoding(String remoteEncoding) {
        this.remoteEncoding = remoteEncoding;
    }

    public boolean isPassiveMode() {
        return passiveMode;
    }

    public void setPassiveMode(boolean passiveMode) {
        this.passiveMode = passiveMode;
    }
}
