package com.magic.utils.commons;

import org.junit.Test;

/**
 * Created by chloneda
 * Description:
 */
public class StringFormaterTest {

    @Test
    public void test(){
        String str=StringFormater.format("{}:{}","localhost","8080");
        System.out.println(str);
    }
}
