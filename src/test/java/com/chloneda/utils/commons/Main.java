package com.chloneda.utils.commons;

import com.chloneda.utils.regex.multiregex.MultiPattern;
import com.chloneda.utils.regex.multiregex.MultiPatternMatcher;
import com.chloneda.utils.regex.multiregex.MultiPatternSearcher;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by chloneda on 2019-06-27
 * Description:
 * <p>
 * JUnit4常用的几个annotation：
 *
 * @Before：初始化方法 对于每一个测试方法都要执行一次（注意与BeforeClass区别，后者是对于所有方法执行一次）
 * @After：释放资源 对于每一个测试方法都要执行一次（注意与AfterClass区别，后者是对于所有方法执行一次）
 * @Test：测试方法，在这里可以测试期望异常和超时时间
 * @Test(expected=ArithmeticException.class)检查被测方法是否抛出ArithmeticException异常
 * @Ignore：忽略的测试方法
 * @BeforeClass：针对所有测试，只执行一次，且必须为static void
 * @AfterClass：针对所有测试，只执行一次，且必须为static void
 * 一个JUnit4的单元测试用例执行顺序为：
 * @BeforeClass -> @Before -> @Test -> @After -> @AfterClass;
 * 每一个测试方法的调用顺序为：
 * @Before -> @Test -> @After;
 */
public class Main {

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
    public void testSearch() {
        MultiPatternSearcher.Cursor cursor = multiPatternSearcher.search("ab abc vvv");//"ab abc vvv"
        //cursor.next();
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
