package com.chloneda.jutils.filesystem.ftp;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by chloneda
 * Description:
 */
public class FTPUtilsTest {
    private FTPUtils ftpUtils;

    @Before
    public void init() throws IOException {
        FTPConfig ftpConfig =new FTPConfig(
                "192.167.2.120",
                21,
                "vsftpd",
                "vsftpd",
                "/",
                "/opt",
                "GBK",
                true);
        ftpUtils=new FTPUtils(ftpConfig);
    }

    @Test
    public void isExists(){
        boolean isExists= ftpUtils.isExists("/Ftp.jpg");
        Assert.assertEquals(true,isExists);
    }
}
