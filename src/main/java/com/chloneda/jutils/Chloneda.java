package com.chloneda.jutils;

import java.nio.charset.Charset;
import java.util.regex.Pattern;

/**
 * @Created by chloneda
 * @Description: 常量相关工具类，可以继承扩展
 */
public class Chloneda {

    private Chloneda() {
        throw new UnsupportedOperationException("You can't instantiate me...");
    }

    /**
     * 存储相关常量
     */
    public static class Memory {

        /**
         * Byte与Byte的倍数
         */
        public static final int BYTE = 1;

        /**
         * KB与Byte的倍数
         */
        public static final int KB = 1024;

        /**
         * MB与Byte的倍数
         */
        public static final int MB = 1024 * 1024;

        /**
         * GB与Byte的倍数
         */
        public static final int GB = 1024 * 1024 * 1024;

        /**
         * TB与Byte的倍数
         */
        public static final long TB = 1024 * 1024 * 1024 * 1024;

        /**
         * PB与Byte的倍数
         */
        public static final long PB = 1024 * 1024 * 1024 * 1024 * 1024;

        public enum MemoryUnit {
            BYTE,
            KB,
            MB,
            GB,
            TB,
            PB
        }
    }

    /**
     * 时间相关常量
     */
    public static class Time {
        /**
         * 毫秒与毫秒的倍数
         */
        public static final int MSEC = 1;

        /**
         * 秒与毫秒的倍数
         */
        public static final int SEC = 1000;

        /**
         * 分与毫秒的倍数
         */
        public static final int MIN = 60000;

        /**
         * 时与毫秒的倍数
         */
        public static final int HOUR = 3600000;
        /**
         * 天与毫秒的倍数
         */
        public static final int DAY = 86400000;

        public enum TimeUnit {
            MSEC,
            SEC,
            MIN,
            HOUR,
            DAY
        }
    }

    /**
     * 标准字符集编码
     */
    public static final class StandardCharsets {

        private StandardCharsets() {
            throw new AssertionError("No StandardCharsets instances for you!");
        }

        /**
         * Seven-bit ASCII, a.k.a. ISO646-US, a.k.a. the Basic Latin block of the
         * Unicode character set
         */
        public static final Charset US_ASCII = Charset.forName("US-ASCII");

        /**
         * ISO Latin Alphabet No. 1, a.k.a. ISO-LATIN-1
         */
        public static final Charset ISO_8859_1 = Charset.forName("ISO-8859-1");

        /**
         * Eight-bit UCS Transformation Format
         */
        public static final Charset UTF_8 = Charset.forName("UTF-8");

        /**
         * Sixteen-bit UCS Transformation Format, big-endian byte order
         */
        public static final Charset UTF_16BE = Charset.forName("UTF-16BE");

        /**
         * Sixteen-bit UCS Transformation Format, little-endian byte order
         */
        public static final Charset UTF_16LE = Charset.forName("UTF-16LE");

        /**
         * Sixteen-bit UCS Transformation Format, byte order identified by an
         * optional byte-order mark
         */
        public static final Charset UTF_16 = Charset.forName("UTF-16");

    }


    /**
     * 字符集编码
     */
    public static class CharSet {
        /**
         * 7位ASCII字符，也叫作ISO646-US、Unicode字符集的基本拉丁块
         */
        public static final String US_ASCII = "US-ASCII";

        /**
         * ISO 拉丁字母表
         */
        public static final String ISO_8859_1 = "ISO-8859-1";

        /**
         * 8 位 UCS 转换格式
         */
        public static final String UTF_8 = "UTF-8";

        /**
         * 16 位 UCS 转换格式，Big Endian（最低地址存放高位字节）字节顺序
         */
        public static final String UTF_16BE = "UTF-16BE";

        /**
         * 16 位 UCS 转换格式，Little-endian（最高地址存放低位字节）字节顺序
         */
        public static final String UTF_16LE = "UTF-16LE";

        /**
         * 16 位 UCS 转换格式，字节顺序由可选的字节顺序标记来标识
         */
        public static final String UTF_16 = "UTF-16";

        /**
         * 中文超大字符集
         */
        public static final String GBK = "GBK";

        /**
         * 最常见的中文字符集
         */
        public static final String GB2312 = "GB2312";
    }

    /**
     * 正则相关常量
     */
    public static class Regex {
        /**
         * 正则：手机号（简单）
         */
        public static final Pattern REGEX_MOBILE_SIMPLE = Pattern.compile("^[1]\\d{10}$");

        /**
         * 正则：手机号（精确）
         * <p>移动：134(0-8)、135、136、137、138、139、147、150、151、152、157、158、159、178、182、183、184、187、188</p>
         * <p>联通：130、131、132、145、155、156、175、176、185、186</p>
         * <p>电信：133、153、173、177、180、181、189</p>
         * <p>全球星：1349</p>
         * <p>虚拟运营商：170</p>
         */
        public static final Pattern REGEX_MOBILE_EXACT = Pattern.compile("^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0,3,5-8])|(18[0-9])|(147))\\d{8}$");

        /**
         * 正则：电话号码
         */
        public static final Pattern REGEX_TEL = Pattern.compile("^0\\d{2,3}[- ]?\\d{7,8}");

        /**
         * 正则：身份证号码15位
         */
        public static final Pattern REGEX_IDCARD15 = Pattern.compile("^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$");

        /**
         * 正则：身份证号码18位
         */
        public static final Pattern REGEX_IDCARD18 = Pattern.compile("^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9Xx])$");

        /**
         * 正则：邮箱
         */
        public static final Pattern REGEX_EMAIL = Pattern.compile("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");

        /**
         * 正则：URL
         */
        public static final Pattern REGEX_URL = Pattern.compile("http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w-./?%&=]*)?");

        /**
         * 正则：汉字
         */
        public static final Pattern REGEX_CHINESE = Pattern.compile("^[\\u4e00-\\u9fa5]+$");

        /**
         * 正则：用户名，取值范围为a-z,A-Z,0-9,"_",汉字，不能以"_"结尾,用户名必须是6-20位
         */
        public static final Pattern REGEX_USERNAME = Pattern.compile("^[\\w\\u4e00-\\u9fa5]{6,20}(?<!_)$");

        /**
         * 正则：yyyy-MM-dd格式的日期校验，已考虑平闰年
         */
        public static final Pattern REGEX_DATE = Pattern.compile("^(?:(?!0000)[0-9]{4}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1[0-9]|2[0-8])|(?:0[13-9]|1[0-2])-(?:29|30)|(?:0[13578]|1[02])-31)|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)-02-29)$");

        /**
         * 正则：IPv4地址
         */
        public static final Pattern REGEX_IPV4 = Pattern.compile("^(([1-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){1}(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){2}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$");

        /**
         * 正则：IPv6地址
         */
        public static final Pattern REGEX_IPV6 = Pattern.compile("^[0-9a-fA-F]{1,4}(:[0-9a-fA-F]{1,4}){7}$");
    }

}
