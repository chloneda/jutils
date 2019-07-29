package com.magic.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by chloneda
 * Description:
 */
public class Main {

    private static Set getEmailList(String str) {
        //邮箱域名后缀表
        String dn = "com|cn|org|com.cn|org.cn|net|net.com|gov.cn";
        Pattern p = Pattern.compile("[\\w[.-]]+@[\\w[.-]]+\\.("+dn+")"); //邮箱验证
        Matcher m = p.matcher(str);
        Set<String> emailList = new HashSet<String>();
        while (m.find()) {
            //去除包涵连续两个点的邮箱
            if(!m.group().contains("..")) {
                emailList.add(m.group());
            }
        }
        return emailList;
    }

    public static void main(String[] args) {
    }
}
