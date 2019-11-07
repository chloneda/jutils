package com.chloneda.jutils.algorithm;

import org.junit.Test;

/**
 * @author chloneda
 * @description:
 */
public class EncryptUtilsTest {

    @Test
    public void testEncryptBASE64() {
        String encStr = EncryptUtils.encryptBASE64("admin");
        System.out.println("BASE64加密后字符串{}: " + encStr);
    }

    @Test
    public void testDecryptBASE64() {
        String encStr = EncryptUtils.encryptBASE64("admin");
        System.out.println("BASE64加密后字符串{}: " + encStr);
        String decStr = EncryptUtils.decryptBASE64(encStr);
        System.out.println("BASE64解密后字符串{}: " + decStr);

    }

    /**
     * {@link org.apache.commons.codec.binary.Base64}
     *
     * 判断字符串是否Base64字符串的一种方法:
     * 将目标字符串 解密后再将解密字符串加密回去 与原来的值做比较 如果相同就是base64
     */
    @Test
    public void testIsBase64(){
        String encStr = EncryptUtils.encryptBASE64("admin");
        System.out.println("BASE64加密后字符串{}: " + encStr);
        boolean isBase64= EncryptUtils.isBase64(encStr);
        System.out.println(isBase64);
    }

}
