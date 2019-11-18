package com.chloneda.jutils.compress;

import org.junit.Test;

import java.io.IOException;
import java.util.Objects;

/**
 * @Created by chloneda
 * @Description:
 */
public class ZipUtilTest {

    @Test
    public void testCompress() throws IOException {
        //将目标目录的文件压缩成Zip文件
        String srcDirPath = "D:/opt/share/zipFolder";
        String dstZipFilePath = "D:/opt/share/new.zip";

        ZipUtils.compress(srcDirPath, dstZipFilePath);
    }

    @Test
    public void testDecompress() throws IOException {
        //将Zip文件解压缩到目标目录
        String zipFilePath = "D:/opt/share/new.zip";
        String targetDirPath = "D:/opt/share/";
        Objects.requireNonNull(targetDirPath);
        Objects.requireNonNull(zipFilePath);

        ZipUtils.decompress(zipFilePath, targetDirPath);
    }

}
