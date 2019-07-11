package com.chloneda.utils.compress;

import org.junit.Test;

import java.io.IOException;
import java.util.Objects;

/**
 * Created by chloneda
 * Description:
 */
public class ZipUtilTest {

    @Test
    public void testCompress() throws IOException {
        //将目标目录的文件压缩成Zip文件
        String targetDirPath = "/opt/tmpdir/zipFolder";
        String newZipFilePath = "/opt/tmpdir/new.zip";

        ZipUtil.compress(targetDirPath , newZipFilePath);
    }

    @Test
    public void testDecompress() throws IOException {
        //将Zip文件解压缩到目标目录
        String targetDirPath = "/opt/tmpdir/zipFolder";
        String newZipFilePath = "/opt/tmpdir/new.zip";
        Objects.nonNull(targetDirPath);
        Objects.requireNonNull(newZipFilePath);

        ZipUtil.decompress(newZipFilePath , targetDirPath);
    }

}
