package com.chloneda.jutils.beans;

import org.apache.commons.beanutils.BeanUtils;

import java.util.*;

/**
 * @Created by chloneda
 * @Description: Bean之间属性复制，抽象工具类，不可实例化
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

    public static <T> T copyObject(T dst, Object src) {
        if (src == null) {
            return null;
        }
        try {
            BeanUtils.copyProperties(dst, src);
            return (T) dst;
        } catch (Exception var) {
            var.printStackTrace();
        }
        return null;
    }

    public static <E, T> T copyObject(Class<T> cls, E src, BeanCopyCallBack<E, T> copyCallBack) {
        if (src == null) {
            return null;
        }
        try {
            T dst = cls.newInstance();
            BeanUtils.copyProperties(dst, src);
            copyCallBack.addProperties(src, dst);
            return dst;
        } catch (Exception var) {
            var.printStackTrace();
        }
        return null;
    }

    public static <T, E> List<T> copyArray(Class<T> cls, Collection<E> srcArray) {
        List<T> desList = new ArrayList<>();
        copyCollection(cls, srcArray, desList);
        return desList;
    }

    public static <T, E> List<T> copyArray(Class<T> cls, Collection<E> srcArray, BeanCopyCallBack<E, T> copyCallBack) {
        if (srcArray == null) {
            return new ArrayList<>();
        }
        List<T> desList = new ArrayList<>(srcArray.size());
        copyCollection(cls, srcArray, desList, copyCallBack);
        return desList;
    }

    public static <T, E> Set<T> copySet(Class<T> cls, Collection<E> srcArray) {
        Set<T> desSet = new HashSet<>();
        copyCollection(cls, srcArray, desSet);
        return desSet;
    }

    public static <T, E> Set<T> copySet(Class<T> cls, Collection<E> srcArray, BeanCopyCallBack copyCallBack) {
        Set<T> desSet = new HashSet<>();
        copyCollection(cls, srcArray, desSet, copyCallBack);
        return desSet;
    }

    public static <T, E> void copyCollection(Class<T> cls, Collection<E> srcArray, Collection<T> desCollection) {
        try {
            if (srcArray == null) {
                return;
            }
            for (E src : srcArray) {
                Object dst = cls.newInstance();
                BeanUtils.copyProperties(dst, src);
                desCollection.add((T) dst);
            }
        } catch (Exception var) {
            var.printStackTrace();
        }
    }

    public static <T, E> void copyCollection(Class<T> cls, Collection<E> srcArray, Collection<T> desList, BeanCopyCallBack<E, T> copyCallBack) {
        try {
            if (srcArray == null) {
                return;
            }
            for (E src : srcArray) {
                T dst = cls.newInstance();
                BeanUtils.copyProperties(dst, src);
                copyCallBack.addProperties(src, dst);
                desList.add(dst);
            }
        } catch (Exception var) {
            var.printStackTrace();
        }
    }

    public interface BeanCopyCallBack<E, T> {
        void addProperties(E src, T des);
    }

    public interface BeanFilter<T, E> {
        boolean isFiltered(T dst, E src);
    }

}
