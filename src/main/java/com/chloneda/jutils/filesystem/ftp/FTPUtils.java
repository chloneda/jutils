package com.chloneda.jutils.filesystem.ftp;

import com.chloneda.jutils.commons.AssertUtils;
import com.chloneda.jutils.java.io.FileUtils;
import com.chloneda.jutils.json.JacksonUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by chloneda
 * Description:FTP工具类
 */
public class FTPUtils implements AbstractFTP {

    private Logger logger = LoggerFactory.getLogger(FTPUtils.class);
    private FTPClient client;
    private FTPVo vo;
    private boolean login = false;


    public FTPUtils(FTPVo vo) throws IOException {
        this.vo = vo;
        login = initFTPClien(vo);
    }

    private synchronized boolean initFTPClien(FTPVo vo) {
        client = new FTPClient();
        boolean flag = true;
        int reply = -1;
        try {
            client.connect(vo.getHostName(), vo.getPort());
            client.login(vo.getUsername(), vo.getPassword());
            reply = client.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                client.disconnect();
                flag = false;
                return flag;
            }
            if (vo.isPassiveMode()) {
                client.enterLocalPassiveMode();
            } else {
                client.enterRemotePassiveMode();
            }
            client.setControlEncoding(vo.getRemoteEncoding());
            client.setFileType(FTPClient.BINARY_FILE_TYPE);
            client.cwd(vo.getRemoteBaseDir());
            return flag;
        } catch (IOException e) {
            flag = false;
            e.printStackTrace();
        }
        return flag;
    }

    //通过FTP响应码判断是否操作成功
    public boolean reply(String operation) {
        int replyCode = client.getReplyCode();
        FTPLogger log = new FTPLogger();
        log.setHost(vo.getHostName());
        log.setOperation(operation);
        log.setLocalFile("");
        log.setRemoteFile("");
        log.setReplyCode(replyCode);
        log.setReplyCodeDesc(FTPConstant.REPLY_CODE.get(replyCode));
        logger.info(JacksonUtils.toJson(log));
        return FTPReply.isPositiveCompletion(replyCode);
    }

    public boolean reply(String operation, String localFile, String remoteFile) {
        int replyCode = client.getReplyCode();
        FTPLogger log = new FTPLogger();
        log.setHost(vo.getHostName());
        log.setOperation(operation);
        log.setLocalFile(localFile);
        log.setRemoteFile(remoteFile);
        log.setReplyCode(replyCode);
        log.setReplyCodeDesc(FTPConstant.REPLY_CODE.get(replyCode));
        logger.info(JacksonUtils.toJson(log));
        return FTPReply.isPositiveCompletion(replyCode);
    }

    @Override
    public boolean isExists(String fileName) {
        List<String> list = listFile(vo.getRemoteBaseDir());
        if (list != null && list.contains(fileName)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean downloadFile(String fileName) {
        String localfileName = vo.getLocalDir() + File.separator + fileName;
        FileUtils.createFiles(localfileName);
        OutputStream out = null;
        try {
            out = new FileOutputStream(localfileName, true);
            client.retrieveFile(new String(fileName.getBytes(vo.getRemoteEncoding()), "ISO-8859-1"), out);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return reply("DOWNLOAD", localfileName, fileName);
    }

    @Override
    public boolean downloadDir(String directory) {
        List<String> files = listFile(directory);
        for (String fileName : files) {
            downloadFile(fileName);
        }
        return true;
    }

    @Override
    public boolean deleteFile(String fileName) {
        return false;
    }

    @Override
    public boolean deleteDir(String directory) {
        return false;
    }

    @Override
    public boolean putFile(String fileName, String remoteFileName, boolean isDelete) {
        return false;
    }

    @Override
    public boolean putFile(File file, String remoteFileName, boolean isDelete) {
        return false;
    }

    @Override
    public boolean putDir(String fileName, String remoteDir) {
        return false;
    }

    @Override
    public boolean putDir(File file, String remoteDir) {
        return false;
    }

    @Override
    public boolean mkDir(String destory) {
        return false;
    }

    @Override
    public List<String> listFile(String directory) {
        List<String> list = new ArrayList<String>();
        try {
            FTPFile[] files = client.listFiles(directory);
            for (int i = 0; i < files.length; i++) {
                String t = (directory + "/" + files[i].getName()).replaceAll("//", "/");
                if (files[i].isFile()) {
                    list.add(t);
                } else if (files[i].isDirectory()) {
                    list.addAll(listFile((t + "/").replaceAll("//", "/")));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public LinkedList<String> listDir(String direcotyr) {
        return null;
    }

    @Override
    public Map<String, FTPFileAttr> listFileAttr(String directory) {
        return null;
    }

    @Override
    public boolean changeWorkDir(String directory) {
        try {
            client.cwd(directory);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public String getWorkDir() {
        try {
            return client.printWorkingDirectory();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public boolean changName(String oldName, String newName) {
        return false;
    }

    @Override
    public FTPClient client() {
        return client;
    }

    @Override
    public void destory() {
        if (AssertUtils.notNull(client)) {
            try {
                client.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
