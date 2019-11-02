package com.chloneda.jutils.commons;

import org.junit.Test;

/**
 * @Created by chloneda
 * @Description:
 */
public class CheckUtilsTest {

    @Test
    public void testIsTrue(){
        CheckUtils.isTrue(false);
    }

    @Test
    public void testIsTrues(){
        CheckUtils.isTrue(false,"error","test");
    }



}
