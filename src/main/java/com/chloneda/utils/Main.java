//package com.magic.utils;
//
//import org.apache.commons.lang3.StringUtils;
//
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
///**
// * Created by chloneda
// * Description:
// */
//public class Main {
//
//    private static Set getEmailList(String str) {
//        //在此维护域名后缀表
//        String dn = "com|cn|org|com.cn|org.cn|net|net.com|gov.cn";
//        Pattern p = Pattern.compile("[\\w[.-]]+@[\\w[.-]]+\\.("+dn+")"); //邮箱验证
//        //Pattern p=Pattern.compile("[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?");
//        Matcher m = p.matcher(str);
//        Set<String> emailList = new HashSet<String>();
//        while (m.find()) {
//            //update  2016-1-21 10:03:45
//            //去除包涵连续两个点的邮箱
//            if(!m.group().contains("..")) {
//                emailList.add(m.group());
//            }
//        }
//        return emailList;
//    }
//
//    public static void main(String[] args) {
//
////        String mailStr="\"zhao_chen@topsec.com.cn\" <zhao_chen@topsec.com.cn>,shiminhui <shiminhui@han-networks.com>,li_xuesheng@topsec.com.cn,<qian_peng@topsec.com.cn>";
////
////        /*String str = "tencent.sf.gg@10000@qq.comghghghghg\n" +
////                "你好leveychen，lev；levey-chen@myema你aaa@this.gov.cn好il.gmail.com悲" +
////                "剧help@admin.sf.gg了" +
////                "啊m@levey.com.cnmnihao的都是</br>和<sf@sf.gggame@google.com>";*/
////        System.out.println(getEmailList(mailStr));
//
//
//    }
//}
