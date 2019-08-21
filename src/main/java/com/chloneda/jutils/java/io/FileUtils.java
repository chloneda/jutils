package com.chloneda.jutils.java.io;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by chloneda
 * Description:
 */
public class FileUtils {

    /**
     * Buffer的大小
     */
    private static Integer BUFFER_SIZE = 1024 * 1024 * 10;

    public static MessageDigest MD5 = null;

    static {
        try {
            MD5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建多级目录中的文件
     *
     * @param filePath 需要创建的文件
     * @return 是否成功
     */
    public final static boolean createFiles(String filePath) {
        File file = new File(filePath);
        if(file.isDirectory()){
            return !file.exists()&&file.mkdirs();
        }else{
            File dir = file.getParentFile();
            if (!dir.exists()) {
                if (dir.mkdirs()) {
                    try {
                        return file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return false;
    }

    /**
     * 创建多级目录
     *
     * @param filePath 需要创建的目录
     * @return 是否创建成功
     */
    public static boolean mkdir(String filePath){
        File file=new File(filePath);
        return !file.exists()&&file.mkdir();
    }

    public static boolean deleteAllFiles(File f) {
        if (f.exists()) {
            File[] files = f.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteAllFiles(file);
                        file.delete(); //删除目录下的所有文件后，该目录变成了空目录，可直接删除
                    } else if (file.isFile()) {
                        file.delete();
                    }
                }
            }
            return f.delete(); //删除最外层的目录
        }
        return false;
    }

    public static boolean deleteFile(String sPath) {
        File file = new File(sPath);
        if (file.isFile() && file.exists()) {
            return file.delete();
        }
        return false;
    }


}
