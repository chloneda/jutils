package com.chloneda.jutils.commons;

import java.util.Arrays;

/**
 * Created by chloneda@gmail.com
 * Description: 提供先决条件判断、对象有效性校验的工具类
 */
public class CheckUtils {

    public static boolean equals(Object a, Object b) {
        return (a == b) || (a != null && a.equals(b));
    }

    public static boolean deepEquals(Object a, Object b) {
        if (a == b) {
            return true;
        } else if (a == null || b == null) {
            return false;
        } else {
            return Arrays.deepEquals((Object[]) a, (Object[]) b);
        }
    }

    public static boolean isNull(Object obj) {
        return obj == null;
    }

    public static boolean notNull(Object obj) {
        return obj != null;
    }

    public static void isTrue(boolean expression) {
        if (!expression) {
            throw new IllegalArgumentException("The expression is false !");
        }
    }

    public static void isTrue(boolean expression, String message, Object... values) {
        if (!expression) {
            throw new IllegalArgumentException(String.format(message, values));
        }
    }

    public static int checkIndex(int index, int size) {
        return checkIndex(index, size, "index");
    }

    public static int checkIndex(int index, int size, String message) {
        if (index >= 0 && index < size) {
            return index;
        } else {
            throw new IndexOutOfBoundsException(badIndex(index, size, message));
        }
    }

    private static String badIndex(int index, int size, String message) {
        if (index < 0) {
            return String.format("%s (%s) must not be negative", message, index);
        } else if (size < 0) {
            throw new IllegalArgumentException("negative size: " + size);
        } else {
            return String.format("%s (%s) must not be greater than size (%s)", message, index, size);
        }
    }

    public static <T> T requireNotNull(T obj) {
        if (obj == null) {
            throw new NullPointerException();
        }
        return obj;
    }

    public static <T> T requireNotNull(T obj, String message) {
        if (obj == null) {
            throw new NullPointerException(message);
        }
        return obj;
    }

}
