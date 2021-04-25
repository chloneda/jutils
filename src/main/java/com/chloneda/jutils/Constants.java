package com.chloneda.jutils;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @Created by chloneda
 * @Description: 常量相关工具类，可以继承扩展
 */
public class Constants {

    private Constants() {
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
     * 字符集编码
     */
    public static final class Charsets {

        private Charsets() {
            throw new AssertionError("No Charsets instances for you!");
        }

        public static final Charset US_ASCII = Charset.forName("US-ASCII");

        public static final Charset ISO_8859_1 = Charset.forName("ISO-8859-1");

        public static final Charset UTF_8 = Charset.forName("UTF-8");

        public static final Charset UTF_16BE = Charset.forName("UTF-16BE");

        public static final Charset UTF_16LE = Charset.forName("UTF-16LE");

        public static final Charset UTF_16 = Charset.forName("UTF-16");

        /**
         * 中文超大字符集
         */
        public static final Charset GBK = Charset.forName("GBK");

        /**
         * 最常见的中文字符集
         */
        public static final Charset GB2312 = Charset.forName("GB2312");

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

    public static final class ResourcePath {

        public static final String BASE_PATH = Thread.currentThread().getContextClassLoader().getResource("").getPath();

        /**
         * RESOURCE_PATH和 BASE_PATH 获取路径一样.tomcat重写了类加载器
         * 如: /opt/tomcat/webapps/jutil/WEB-INF/class/
         */
        public static final String RESOURCE_PATH = ResourcePath.class.getClass().getResource("/").getPath();

        /**
         * Tomcat静态资源存放根路径，如：/opt/tomcat/webapps/jutil
         */
        public static final String ROOT_PATH = ResourcePath.class.getClass().getResource("/").getPath() + "../../";

    }

    public enum HttpMethod {

        GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE;

        private static final Map<String, HttpMethod> mappings = new HashMap<>(16);

        static {
            for (HttpMethod httpMethod : values()) {
                mappings.put(httpMethod.name(), httpMethod);
            }
        }

        public static HttpMethod resolve(String method) {
            return (method != null ? mappings.get(method) : null);
        }

        public boolean matches(String method) {
            return (this == resolve(method));
        }

    }


}
