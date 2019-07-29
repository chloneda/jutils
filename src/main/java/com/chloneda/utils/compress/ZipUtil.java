package com.chloneda.utils.compress;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.*;

/**
 * Created by chloneda
 * Description:
 */
public class ZipUtil {
    private static final int BUFFER = 8192;
    private static final String ZIP_FILE_EXTENSION = ".zip";

    /**
     * 压缩文件
     * @param srcPath 压缩文件或目录
     * @param dstPath 压缩目标路径
     */
    public static void compress(String srcPath , String dstPath){
        File srcFile = new File(srcPath);
        File dstFile = new File(dstPath);
        if (!dstFile.exists()) {
            dstFile.mkdirs();
        }

        FileOutputStream out = null;
        ZipOutputStream zipOut = null;
        try {
            out = new FileOutputStream(dstFile);
            CheckedOutputStream cos = new CheckedOutputStream(out,new CRC32());
            zipOut = new ZipOutputStream(cos);
            String baseDir = "";
            compress(srcFile, zipOut, baseDir);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if(null != zipOut){
                try {
                    zipOut.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(null != out){
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void compress(File file, ZipOutputStream zipOut, String baseDir){
        if (file.isDirectory())
            compressDir(file, zipOut, baseDir);
        else
            compressFile(file, zipOut, baseDir);
    }

    /**
     * 压缩目录
     * @param dir
     * @param zipOut
     * @param baseDir
     */
    private static void compressDir(File dir, ZipOutputStream zipOut, String baseDir){
        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++) {
            compress(files[i], zipOut, baseDir + dir.getName() + "/");
        }
    }

    /**
     * 压缩文件
     * @param file
     * @param zipOut
     * @param baseDir
     */
    private static void compressFile(File file, ZipOutputStream zipOut, String baseDir){
        if (!file.exists()){
            return;
        }

        BufferedInputStream bis = null;
        try {
            bis = new BufferedInputStream(new FileInputStream(file));
            ZipEntry entry = new ZipEntry(baseDir + file.getName());
            zipOut.putNextEntry(entry);
            int count;
            byte data[] = new byte[BUFFER];
            while ((count = bis.read(data, 0, BUFFER)) != -1) {
                zipOut.write(data, 0, count);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(null != bis){
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 解压文件
     * @param zipFile zip文件
     * @param dstPath 解压路径
     */
    public static void decompress(String zipFile , String dstPath){
        File pathFile = new File(dstPath);
        if(!pathFile.exists()){
            pathFile.mkdirs();
        }

        InputStream in = null;
        OutputStream out = null;
        try{
            ZipFile zip = new ZipFile(zipFile);
            for(Enumeration entries = zip.entries(); entries.hasMoreElements();){
                ZipEntry entry = (ZipEntry)entries.nextElement();
                String zipEntryName = entry.getName();

                in =  zip.getInputStream(entry);
                String outPath = (dstPath+"/"+zipEntryName).replaceAll("\\*", "/");;
                //判断路径是否存在,不存在则创建文件路径
                File file = new File(outPath.substring(0, outPath.lastIndexOf('/')));
                if(!file.exists()){
                    file.mkdirs();
                }
                //判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
                if(new File(outPath).isDirectory()){
                    continue;
                }
                out = new FileOutputStream(outPath);
                byte[] buf1 = new byte[1024];
                int len;
                while((len=in.read(buf1))>0) {
                    out.write(buf1, 0, len);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(null != in){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if(null != out){
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
