package com.chloneda.jutils.beans;

import org.apache.commons.beanutils.BeanUtils;

/**
 * Created by chloneda
 * Description:
 */
public abstract class JBeanUtils {

    public static <T> T copyObject(Class<T> cls, Object src) {
        if (src == null) {
            return null;
        }
        try {
            Object dst = cls.newInstance();
            BeanUtils.copyProperties(dst, src);
            return (T) dst;
        } catch (Exception var) {
            var.printStackTrace();
        }
        return null;
    }

}
