package com.chloneda.jutils.compress.compression.imp;

import com.chloneda.jutils.compress.compression.Compression;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipCompress implements Compression {

    @Override
    public void uncompression(File warFile, boolean delete, String unzipPath) {
        try {
            //获得输出流
            BufferedInputStream bufferedInputStream = new BufferedInputStream(
                    new FileInputStream(warFile));
            ArchiveInputStream in = new ArchiveStreamFactory()
                    .createArchiveInputStream(ArchiveStreamFactory.ZIP,
                            bufferedInputStream);
            ZipArchiveEntry entry;
            //循环遍历解压
            while ((entry = (ZipArchiveEntry) in.getNextEntry()) != null) {
                if (entry.isDirectory()) {
                    new File(unzipPath, entry.getName()).mkdir();
                } else {
                    OutputStream out = FileUtils.openOutputStream(new File(
                            unzipPath, entry.getName()));
                    IOUtils.copy(in, out);
                    close(out);
                }
            }
            close(in);
            if (delete)
                warFile.delete();
        } catch (FileNotFoundException e) {
            System.err.println("未找到文件");
        } catch (ArchiveException e) {
            System.err.println("不支持的压缩格式");
        } catch (IOException e) {
            System.err.println("文件写入发生错误");
        }
    }

    @Override
    public void compression(File[] srcfile, boolean delete, String zipfile) {
        byte[] buf = new byte[1024];
        try {
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(
                    zipfile));
            for (int i = 0; i < srcfile.length; i++) {
                FileInputStream in = new FileInputStream(srcfile[i]);
                out.putNextEntry(new ZipEntry(srcfile[i].getName()));
                String str = srcfile[i].getName();
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.closeEntry();
                in.close();
            }
            out.close();
            System.out.println("压缩完成.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close(AutoCloseable closeable) {
        if (null != closeable) {
            try {
                closeable.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
