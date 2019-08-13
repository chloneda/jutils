package com.chloneda.jutils.algorithm;

import org.apache.commons.codec.binary.Base64;
//import java.util.Base64;

import java.nio.charset.Charset;
import java.util.Objects;


/**
 * Created by chloneda
 * Description:
 */
public class Base64Utils {

    public static void main(String[] args) {
        String str="a;fsafkaj";
        if(Base64.isBase64(str)){
            new String(Base64.decodeBase64(str), Charset.forName("UTF-8"));
        }
        //BASE64Encoder;
    }

    public static String isBase64(String str){
        Objects.requireNonNull(str);
        if(Base64.isBase64(str))
            str=new String(Base64.decodeBase64(str), Charset.forName("UTF-8"));
        return str;
    }

//    public static String decode(String str){
//        Base64.decodeBase64(str);
//    }

    public static void encode(String str){
        //Base64.encodeBase64();
    }
}
