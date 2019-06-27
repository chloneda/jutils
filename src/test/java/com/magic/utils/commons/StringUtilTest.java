package com.magic.utils.commons;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by chloneda
 * Description:
 */
public class StringUtilTest {

    @Test
    public void testReplaceEach(){
        String jdbcUrl ="jdbc:mysql://%h:%p/testdb";
        String[] searchList = new String[]{"%h", "%p"};
        String[] replaceList = new String[]{"localhost", "3306"};
        String str=StringUtil.replaceEach(jdbcUrl,searchList,replaceList);
        System.out.println(str);
    }

    @Test
    public void testReplace(){
        String text="China,%s %s!";
        String[] replaceList=new String[]{"hello","world"};
        Assert.assertNotNull(text);

        String str=StringUtil.replace(text,replaceList);
        System.out.println(str);
    }


}
