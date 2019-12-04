package com.chloneda.jutils.commons;

import org.junit.Test;

/**
 * @author chloneda
 * @description:
 */
public class StringFormatterTest {

    @Test
    public void testFormat() {
        String str = StringFormatter.format("Hello {}!", "world");
        System.out.println(str);
    }

}
