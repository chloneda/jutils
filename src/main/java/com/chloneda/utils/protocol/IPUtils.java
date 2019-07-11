package com.chloneda.utils.protocol;

import java.util.StringTokenizer;

/**
 * Created by chloneda
 * Description:
 */
public class IPUtils {

    public static long ipToLong(String ipString){
        long result = 0;
        StringTokenizer token = new StringTokenizer(ipString,".");
        result += Long.parseLong(token.nextToken())<<24;
        result += Long.parseLong(token.nextToken())<<16;
        result += Long.parseLong(token.nextToken())<<8;
        result += Long.parseLong(token.nextToken());
        return result;
    }

    public static String longToIP(long ip) {
        return ((ip >> 24) & 0xFF) + "."
                + ((ip >> 16) & 0xFF) + "."
                + ((ip >> 8) & 0xFF) + "."
                + (ip & 0xFF);
    }

    /**
     * 把long类型的Ip转为一般Ip类型：xx.xx.xx.xx
     * @param ip
     * @return
     */
    public static String getIpFromLong(Long ip) {
        String s1 = String.valueOf((ip & 4278190080L) / 16777216L);
        String s2 = String.valueOf((ip & 16711680L) / 65536L);
        String s3 = String.valueOf((ip & 65280L) / 256L);
        String s4 = String.valueOf(ip & 255L);
        return s1 + "." + s2 + "." + s3 + "." + s4;
    }

    /**
     * 把xx.xx.xx.xx类型的转为long类型的
     * @param ip
     * @return
     */
    public static Long getIpFromString(String ip) {
        Long ipLong = 0L;
        String ipTemp = ip;
        ipLong = ipLong * 256 + Long.parseLong(ipTemp.substring(0, ipTemp.indexOf(".")));
        ipTemp = ipTemp.substring(ipTemp.indexOf(".") + 1, ipTemp.length());
        ipLong = ipLong * 256 + Long.parseLong(ipTemp.substring(0, ipTemp.indexOf(".")));
        ipTemp = ipTemp.substring(ipTemp.indexOf(".") + 1, ipTemp.length());
        ipLong = ipLong * 256 + Long.parseLong(ipTemp.substring(0, ipTemp.indexOf(".")));
        ipTemp = ipTemp.substring(ipTemp.indexOf(".") + 1, ipTemp.length());
        ipLong = ipLong * 256 + Long.parseLong(ipTemp);
        return ipLong;
    }

    public static void main(String[] args) {
        System.out.println(longToIP(167773121L));
        System.out.println(ipToLong("10.0.3.193"));

        System.out.println(getIpFromString("10.0.3.193"));
        System.out.println(getIpFromLong(167773121L));
    }
}
