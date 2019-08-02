package com.chloneda.utils.commons;

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

    public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    public static boolean isNotEmpty(CharSequence cs) {
        return !isEmpty(cs);
    }

}
