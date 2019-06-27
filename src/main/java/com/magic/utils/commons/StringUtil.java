package com.magic.utils.commons;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by chloneda
 * Description:
 */
public class StringUtil {

    public static String replaceEach(String text, String[] searchList, String[] replaceList) {
        return StringUtils.replaceEach(text, searchList, replaceList);
    }

    public static String replace(String text,String[] replaceList){
        return String.format(text,replaceList);
    }

    public static void test(){
        StringUtils.abbreviate("abcdefg", 6);// ---"abc..."

        //字符串结尾的后缀是否与你要结尾的后缀匹配，若不匹配则添加后缀
        StringUtils.appendIfMissing("abc","xyz");//---"abcxyz"
        StringUtils.appendIfMissingIgnoreCase("abcXYZ","xyz");//---"abcXYZ"

        //首字母大小写转换
        StringUtils.capitalize("cat");//---"Cat"
        StringUtils.uncapitalize("Cat");//---"cat"

        //字符串扩充至指定大小且居中（若扩充大小少于原字符大小则返回原字符，若扩充大小为 负数则为0计算 ）
        StringUtils.center("abcd", 2);//--- "abcd"
        StringUtils.center("ab", -1);//--- "ab"
        StringUtils.center("ab", 4);//---" ab "
        StringUtils.center("a", 4, "yz");//---"yayz"
        StringUtils.center("abc", 7, "");//---"  abc  "

        //去除字符串中的"\n", "\r", or "\r\n"
        StringUtils.chomp("abc\r\n");//---"abc"

        //判断一字符串是否包含另一字符串
        StringUtils.contains("abc", "z");//---false
        StringUtils.containsIgnoreCase("abc", "A");//---true

        //统计一字符串在另一字符串中出现次数
        StringUtils.countMatches("abba", "a");//---2

        //删除字符串中的梭有空格
        StringUtils.deleteWhitespace("   ab  c  ");//---"abc"

        //比较两字符串，返回不同之处。确切的说是返回第二个参数中与第一个参数所不同的字符串
        StringUtils.difference("abcde", "abxyz");//---"xyz"

        //检查字符串结尾后缀是否匹配
        StringUtils.endsWith("abcdef", "def");//---true
        StringUtils.endsWithIgnoreCase("ABCDEF", "def");//---true
        StringUtils.endsWithAny("abcxyz", new String[] {null, "xyz", "abc"});//---true

        //检查起始字符串是否匹配
        StringUtils.startsWith("abcdef", "abc");//---true
        StringUtils.startsWithIgnoreCase("ABCDEF", "abc");//---true
        StringUtils.startsWithAny("abcxyz", new String[] {null, "xyz", "abc"});//---true

        //判断两字符串是否相同
        StringUtils.equals("abc", "abc");//---true
        StringUtils.equalsIgnoreCase("abc", "ABC");//---true

        //比较字符串数组内的所有元素的字符序列，起始一致则返回一致的字符串，若无则返回""
        StringUtils.getCommonPrefix(new String[] {"abcde", "abxyz"});//---"ab"

        //正向查找字符在字符串中第一次出现的位置
        StringUtils.indexOf("aabaabaa", "b");//---2
        StringUtils.indexOf("aabaabaa", "b", 3);//---5(从角标3后查找)
        StringUtils.ordinalIndexOf("aabaabaa", "a", 3);//---1(查找第n次出现的位置)

        //反向查找字符串第一次出现的位置
        StringUtils.lastIndexOf("aabaabaa", 'b');//---5
        StringUtils.lastIndexOf("aabaabaa", 'b', 4);//---2
        StringUtils.lastOrdinalIndexOf("aabaabaa", "ab", 2);//---1

        //判断字符串大写、小写
        StringUtils.isAllUpperCase("ABC");//---true
        StringUtils.isAllLowerCase("abC");//---false

        //判断是否为空(注：isBlank与isEmpty 区别)
        StringUtils.isBlank(null);StringUtils.isBlank("");StringUtils.isBlank(" ");//---true
        StringUtils.isNoneBlank(" ", "bar");//---false

        StringUtils.isEmpty(null);StringUtils.isEmpty("");//---true
        StringUtils.isEmpty(" ");//---false
        StringUtils.isNoneEmpty(" ", "bar");//---true

        //判断字符串数字
        StringUtils.isNumeric("123");//---false
        StringUtils.isNumeric("12 3");//---false (不识别运算符号、小数点、空格……)
        StringUtils.isNumericSpace("12 3");//---true

        //数组中加入分隔符号
        //StringUtils.join([1, 2, 3], ';');//---"1;2;3"

        //大小写转换
        StringUtils.upperCase("aBc");//---"ABC"
        StringUtils.lowerCase("aBc");//---"abc"
        StringUtils.swapCase("The dog has a BONE");//---"tHE DOG HAS A bone"

        //替换字符串内容……（replacePattern、replceOnce）
        StringUtils.replace("aba", "a", "z");//---"zbz"
        StringUtils.overlay("abcdef", "zz", 2, 4);//---"abzzef"(指定区域)
        StringUtils.replaceEach("abcde", new String[]{"ab", "d"},
                new String[]{"w", "t"});//---"wcte"(多组指定替换ab->w，d->t)

        //重复字符
        StringUtils.repeat('e', 3);//---"eee"

        //反转字符串
        StringUtils.reverse("bat");//---"tab"

        //删除某字符
        StringUtils.remove("queued", 'u');//---"qeed"

        //分割字符串
        StringUtils.split("a..b.c", '.');//---["a", "b", "c"]
        StringUtils.split("ab:cd:ef", ":", 2);//---["ab", "cd:ef"]
        StringUtils.splitByWholeSeparator("ab-!-cd-!-ef", "-!-", 2);//---["ab", "cd-!-ef"]
        StringUtils.splitByWholeSeparatorPreserveAllTokens("ab::cd:ef", ":");//-["ab"," ","cd","ef"]

        //去除首尾空格，类似trim……（stripStart、stripEnd、stripAll、stripAccents）
        StringUtils.strip(" ab c ");//---"ab c"
        StringUtils.stripToNull(null);//---null
        StringUtils.stripToEmpty(null);//---""

        //截取字符串
        StringUtils.substring("abcd", 2);//---"cd"
        StringUtils.substring("abcdef", 2, 4);//---"cd"

        //left、right从左(右)开始截取n位字符
        StringUtils.left("abc", 2);//---"ab"
        StringUtils.right("abc", 2);//---"bc"
        //从第n位开始截取m位字符       n  m
        StringUtils.mid("abcdefg", 2, 4);//---"cdef"

        StringUtils.substringBefore("abcba", "b");//---"a"
        StringUtils.substringBeforeLast("abcba", "b");//---"abc"
        StringUtils.substringAfter("abcba", "b");//---"cba"
        StringUtils.substringAfterLast("abcba", "b");//---"a"

        StringUtils.substringBetween("tagabctag", "tag");//---"abc"
        StringUtils.substringBetween("yabczyabcz", "y", "z");//---"abc"
    }
}
