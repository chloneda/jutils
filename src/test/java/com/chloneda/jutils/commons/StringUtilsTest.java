package com.chloneda.jutils.commons;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by chloneda
 * Description:
 */
public class StringUtilsTest {

    @Test
    public void testReplaceEach(){
        String jdbcUrl ="jdbc:mysql://%h:%p/testdb";
        String[] searchList = new String[]{"%h", "%p"};
        String[] replaceList = new String[]{"localhost", "3306"};
        String str= StringUtils.replaceEach(jdbcUrl,searchList,replaceList);
        System.out.println(str);
    }

    @Test
    public void testReplace(){
        String text="China,%s %s!";
        String[] replaceList=new String[]{"hello","world"};
        Assert.assertNotNull(text);

        String str= StringUtils.replace(text,replaceList);
        System.out.println(str);
    }

    @Test
    public void testFormat(){
        String str= StringUtils.format("{}:{}","localhost","8080");
        System.out.println(str);
    }

    @Test
    public void test(){
        org.apache.commons.lang3.StringUtils.abbreviate("abcdefg", 6);// ---"abc..."

        //字符串结尾的后缀是否与你要结尾的后缀匹配，若不匹配则添加后缀
        org.apache.commons.lang3.StringUtils.appendIfMissing("abc","xyz");//---"abcxyz"
        org.apache.commons.lang3.StringUtils.appendIfMissingIgnoreCase("abcXYZ","xyz");//---"abcXYZ"

        //首字母大小写转换
        org.apache.commons.lang3.StringUtils.capitalize("cat");//---"Cat"
        org.apache.commons.lang3.StringUtils.uncapitalize("Cat");//---"cat"

        //字符串扩充至指定大小且居中（若扩充大小少于原字符大小则返回原字符，若扩充大小为 负数则为0计算 ）
        org.apache.commons.lang3.StringUtils.center("abcd", 2);//--- "abcd"
        org.apache.commons.lang3.StringUtils.center("ab", -1);//--- "ab"
        org.apache.commons.lang3.StringUtils.center("ab", 4);//---" ab "
        org.apache.commons.lang3.StringUtils.center("a", 4, "yz");//---"yayz"
        org.apache.commons.lang3.StringUtils.center("abc", 7, "");//---"  abc  "

        //去除字符串中的"\n", "\r", or "\r\n"
        org.apache.commons.lang3.StringUtils.chomp("abc\r\n");//---"abc"

        //判断一字符串是否包含另一字符串
        org.apache.commons.lang3.StringUtils.contains("abc", "z");//---false
        org.apache.commons.lang3.StringUtils.containsIgnoreCase("abc", "A");//---true

        //统计一字符串在另一字符串中出现次数
        org.apache.commons.lang3.StringUtils.countMatches("abba", "a");//---2

        //删除字符串中的梭有空格
        org.apache.commons.lang3.StringUtils.deleteWhitespace("   ab  c  ");//---"abc"

        //比较两字符串，返回不同之处。确切的说是返回第二个参数中与第一个参数所不同的字符串
        org.apache.commons.lang3.StringUtils.difference("abcde", "abxyz");//---"xyz"

        //检查字符串结尾后缀是否匹配
        org.apache.commons.lang3.StringUtils.endsWith("abcdef", "def");//---true
        org.apache.commons.lang3.StringUtils.endsWithIgnoreCase("ABCDEF", "def");//---true
        org.apache.commons.lang3.StringUtils.endsWithAny("abcxyz", new String[] {null, "xyz", "abc"});//---true

        //检查起始字符串是否匹配
        org.apache.commons.lang3.StringUtils.startsWith("abcdef", "abc");//---true
        org.apache.commons.lang3.StringUtils.startsWithIgnoreCase("ABCDEF", "abc");//---true
        org.apache.commons.lang3.StringUtils.startsWithAny("abcxyz", new String[] {null, "xyz", "abc"});//---true

        //判断两字符串是否相同
        org.apache.commons.lang3.StringUtils.equals("abc", "abc");//---true
        org.apache.commons.lang3.StringUtils.equalsIgnoreCase("abc", "ABC");//---true

        //比较字符串数组内的所有元素的字符序列，起始一致则返回一致的字符串，若无则返回""
        org.apache.commons.lang3.StringUtils.getCommonPrefix(new String[] {"abcde", "abxyz"});//---"ab"

        //正向查找字符在字符串中第一次出现的位置
        org.apache.commons.lang3.StringUtils.indexOf("aabaabaa", "b");//---2
        org.apache.commons.lang3.StringUtils.indexOf("aabaabaa", "b", 3);//---5(从角标3后查找)
        org.apache.commons.lang3.StringUtils.ordinalIndexOf("aabaabaa", "a", 3);//---1(查找第n次出现的位置)

        //反向查找字符串第一次出现的位置
        org.apache.commons.lang3.StringUtils.lastIndexOf("aabaabaa", 'b');//---5
        org.apache.commons.lang3.StringUtils.lastIndexOf("aabaabaa", 'b', 4);//---2
        org.apache.commons.lang3.StringUtils.lastOrdinalIndexOf("aabaabaa", "ab", 2);//---1

        //判断字符串大写、小写
        org.apache.commons.lang3.StringUtils.isAllUpperCase("ABC");//---true
        org.apache.commons.lang3.StringUtils.isAllLowerCase("abC");//---false

        //判断是否为空(注：isBlank与isEmpty 区别)
        org.apache.commons.lang3.StringUtils.isBlank(null);
        org.apache.commons.lang3.StringUtils.isBlank("");
        org.apache.commons.lang3.StringUtils.isBlank(" ");//---true
        org.apache.commons.lang3.StringUtils.isNoneBlank(" ", "bar");//---false

        org.apache.commons.lang3.StringUtils.isEmpty(null);
        org.apache.commons.lang3.StringUtils.isEmpty("");//---true
        org.apache.commons.lang3.StringUtils.isEmpty(" ");//---false
        org.apache.commons.lang3.StringUtils.isNoneEmpty(" ", "bar");//---true

        //判断字符串数字
        org.apache.commons.lang3.StringUtils.isNumeric("123");//---false
        org.apache.commons.lang3.StringUtils.isNumeric("12 3");//---false (不识别运算符号、小数点、空格……)
        org.apache.commons.lang3.StringUtils.isNumericSpace("12 3");//---true

        //数组中加入分隔符号
        //StringUtils.join([1, 2, 3], ';');//---"1;2;3"

        //大小写转换
        org.apache.commons.lang3.StringUtils.upperCase("aBc");//---"ABC"
        org.apache.commons.lang3.StringUtils.lowerCase("aBc");//---"abc"
        org.apache.commons.lang3.StringUtils.swapCase("The dog has a BONE");//---"tHE DOG HAS A bone"

        //替换字符串内容……（replacePattern、replceOnce）
        org.apache.commons.lang3.StringUtils.replace("aba", "a", "z");//---"zbz"
        org.apache.commons.lang3.StringUtils.overlay("abcdef", "zz", 2, 4);//---"abzzef"(指定区域)
        org.apache.commons.lang3.StringUtils.replaceEach("abcde", new String[]{"ab", "d"},
                new String[]{"w", "t"});//---"wcte"(多组指定替换ab->w，d->t)

        //重复字符
        org.apache.commons.lang3.StringUtils.repeat('e', 3);//---"eee"

        //反转字符串
        org.apache.commons.lang3.StringUtils.reverse("bat");//---"tab"

        //删除某字符
        org.apache.commons.lang3.StringUtils.remove("queued", 'u');//---"qeed"

        //分割字符串
        org.apache.commons.lang3.StringUtils.split("a..b.c", '.');//---["a", "b", "c"]
        org.apache.commons.lang3.StringUtils.split("ab:cd:ef", ":", 2);//---["ab", "cd:ef"]
        org.apache.commons.lang3.StringUtils.splitByWholeSeparator("ab-!-cd-!-ef", "-!-", 2);//---["ab", "cd-!-ef"]
        org.apache.commons.lang3.StringUtils.splitByWholeSeparatorPreserveAllTokens("ab::cd:ef", ":");//-["ab"," ","cd","ef"]

        //去除首尾空格，类似trim……（stripStart、stripEnd、stripAll、stripAccents）
        org.apache.commons.lang3.StringUtils.strip(" ab c ");//---"ab c"
        org.apache.commons.lang3.StringUtils.stripToNull(null);//---null
        org.apache.commons.lang3.StringUtils.stripToEmpty(null);//---""

        //截取字符串
        org.apache.commons.lang3.StringUtils.substring("abcd", 2);//---"cd"
        org.apache.commons.lang3.StringUtils.substring("abcdef", 2, 4);//---"cd"

        //left、right从左(右)开始截取n位字符
        org.apache.commons.lang3.StringUtils.left("abc", 2);//---"ab"
        org.apache.commons.lang3.StringUtils.right("abc", 2);//---"bc"
        //从第n位开始截取m位字符       n  m
        org.apache.commons.lang3.StringUtils.mid("abcdefg", 2, 4);//---"cdef"

        org.apache.commons.lang3.StringUtils.substringBefore("abcba", "b");//---"a"
        org.apache.commons.lang3.StringUtils.substringBeforeLast("abcba", "b");//---"abc"
        org.apache.commons.lang3.StringUtils.substringAfter("abcba", "b");//---"cba"
        org.apache.commons.lang3.StringUtils.substringAfterLast("abcba", "b");//---"a"

        org.apache.commons.lang3.StringUtils.substringBetween("tagabctag", "tag");//---"abc"
        org.apache.commons.lang3.StringUtils.substringBetween("yabczyabcz", "y", "z");//---"abc"
    }

}
