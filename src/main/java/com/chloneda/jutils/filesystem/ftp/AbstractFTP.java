package com.chloneda.jutils.filesystem.ftp;

import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @Created by chloneda
 * @Description:FTP抽象工具类
 */
public interface AbstractFTP {

    /**
     * 判断远程文件是否存在
     *
     * @param fileName
     * @return
     */
    public boolean isExists(String fileName);

    /**
     * 下载远程文件
     *
     * @param fileName
     * @return
     */
    public boolean downloadFile(String fileName);

    /**
     * 下载远程目录
     *
     * @param directory
     * @return
     */
    public boolean downloadDir(String directory);

    /**
     * 删除远程文件
     *
     * @param fileName
     * @return
     */
    public boolean deleteFile(String fileName);

    /**
     * 删除远程目录
     *
     * @param directory
     * @return
     */
    public boolean deleteDir(String directory);

    /**
     * 上传本地文件到远程目录
     *
     * @param fileName
     * @param remoteFileName
     * @param isDelete
     * @return
     */
    public boolean putFile(String fileName, String remoteFileName, boolean isDelete);

    /**
     * 上传本地文件到远程目录
     *
     * @param file
     * @param remoteFileName
     * @param isDelete
     * @return
     */
    public boolean putFile(File file, String remoteFileName, boolean isDelete);

    /**
     * 上传本地目录到远程
     *
     * @param fileName
     * @param remoteDir
     * @return
     */
    public boolean putDir(String fileName, String remoteDir);

    /**
     * 上传本地目录到远程
     *
     * @param file
     * @param remoteDir
     * @return
     */
    public boolean putDir(File file, String remoteDir);

    /**
     * 创建文件夹
     *
     * @param destory
     * @return
     */
    public boolean mkDir(String destory);

    /**
     * 获取远程文件列表
     *
     * @param directory
     * @return
     */
    public List<String> listFile(String directory);

    /**
     * 获取远程文件夹的目录结构
     *
     * @param direcotyr
     * @return
     */
    public LinkedList<String> listDir(String direcotyr);

    /**
     * 获取远程文件属性以Map形式返回
     *
     * @param directory
     * @return
     */
    public Map<String, FTPFileAttr> listFileAttr(String directory);

    /**
     * 改变FTP连接的工作目录
     *
     * @param directory
     * @return
     */
    public boolean changeWorkDir(String directory);

    /**
     * 获取当前连接的工作目录
     *
     * @return
     */
    public String getWorkDir();

    /**
     * 重命名文件
     *
     * @param oldName
     * @param newName
     * @return
     */
    public boolean changName(String oldName, String newName);

    /**
     * 返回FTPCliend对象(已经打开连接)
     *
     * @return
     */
    public FTPClient client();

    /**
     * 释放资源
     */
    public void destory();

}