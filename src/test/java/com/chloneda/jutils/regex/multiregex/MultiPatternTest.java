package com.chloneda.jutils.regex.multiregex;

import com.chloneda.jutils.regex.multiregex.MultiPattern;
import com.chloneda.jutils.regex.multiregex.MultiPatternMatcher;
import com.chloneda.jutils.regex.multiregex.MultiPatternSearcher;
import org.junit.Assert;
import org.junit.Test;

/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2013 Paul Masurel
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
public class MultiPatternTest {
    public static MultiPattern multiPattern = MultiPattern.of(
            "ab+",     // 0
            "abc+",    // 1
            "ab?c",    // 2
            "v",       // 3
            "v.*",     // 4
            "(def)+"   // 5
    );

    public static final MultiPatternSearcher multiPatternSearcher = multiPattern.searcher();

    public static final MultiPatternMatcher multiPatternMatcher = multiPattern.matcher();

    @Test
    public void testSearcher() {
        MultiPatternSearcher.Cursor cursor = multiPatternSearcher.search("ab abc vvv");
        Assert.assertTrue(cursor.next());
        Assert.assertEquals(cursor.match(), 0);
        Assert.assertEquals(cursor.start(), 0);
        Assert.assertEquals(cursor.end(), 2);

        Assert.assertTrue(cursor.next());
        Assert.assertTrue(cursor.found());
        Assert.assertEquals(cursor.match(), 0);
        Assert.assertEquals(cursor.start(), 3);
        Assert.assertEquals(cursor.end(), 5);

        Assert.assertTrue(cursor.next());
        Assert.assertEquals(cursor.match(), 3);
        Assert.assertTrue(cursor.next());
        Assert.assertEquals(cursor.match(), 3);
        Assert.assertTrue(cursor.next());
        Assert.assertEquals(cursor.match(), 3);
        Assert.assertFalse(cursor.next());
        Assert.assertEquals(cursor.match(), -1);
        Assert.assertFalse(cursor.found());
    }

    public static void helper(MultiPatternMatcher matcher, String str, int... vals) {
        Assert.assertArrayEquals(vals, matcher.match(str));
    }

    @Test
    public void testString() {
        helper(multiPatternMatcher, "ab", 0);
        helper(multiPatternMatcher, "abc", 1, 2);
        helper(multiPatternMatcher, "ac", 2);
        helper(multiPatternMatcher, "");
        helper(multiPatternMatcher, "v", 3, 4);
        helper(multiPatternMatcher, "defdef", 5);
        helper(multiPatternMatcher, "defde");
        helper(multiPatternMatcher, "abbbbb", 0);
    }
}
