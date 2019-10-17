package com.chloneda.jutils.commons;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

/**
 * @Created by chloneda@gmail.com
 * @Description:
 * 提供功能:
 * 1、先决条件判断(not)
 * 2、对象有效性校验(is)
 */
public class CheckUtils {

    private CheckUtils() {
    }

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

    public static boolean isNotNull(Object obj) {
        return obj != null;
    }

    public static <T extends Collection<?>> boolean isEmpty(T col) {
        return col == null || col.isEmpty();
    }

    public static <T extends Collection<?>> boolean isNotEmpty(T col) {
        return !isEmpty(col);
    }

    public static <T extends Collection<?>> boolean isEmpty(T... cols) {
        return cols == null || cols.length == 0;
    }

    public static <T extends Collection<?>> boolean isNotEmpty(T... cols) {
        return !isEmpty(cols);
    }

    public static <T extends Map<?, ?>> boolean isEmpty(T map) {
        return map == null || map.isEmpty();
    }

    public static <T extends Map<?, ?>> boolean isNotEmpty(T map) {
        return !isEmpty(map);
    }

    public static <T extends Map<?, ?>> boolean isEmpty(T... maps) {
        return maps == null || maps.length == 0;
    }

    public static <T extends Map<?, ?>> boolean isNotEmpty(T... maps) {
        return !isEmpty(maps);
    }

    public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    public static boolean isNotEmpty(CharSequence cs) {
        return !isEmpty(cs);
    }

    public static boolean isEmpty(Object... objs) {
        return objs == null || objs.length == 0;
    }

    public static boolean isNotEmpty(Object... objs) {
        return !isEmpty(objs);
    }

    public static boolean isAnyEmpty(Object... objs) {
        for (Object obj : objs) {
            if (isNull(obj)) {
                return true;
            }
        }
        return false;
    }

    public static <T extends CharSequence> boolean isBlank(T cs) {
        int strLen;
        if (cs != null && (strLen = cs.length()) != 0) {
            for (int i = 0; i < strLen; ++i) {
                if (!Character.isWhitespace(cs.charAt(i))) {
                    return false;
                }
            }
            return true;
        } else {
            return true;
        }
    }

    public static boolean isNotBlank(CharSequence cs) {
        return !isBlank(cs);
    }

    public static <T extends CharSequence> T notBlank(T chars, String message, Object... values) {
        if (chars == null) {
            throw new NullPointerException(String.format(message, values));
        } else if (StringUtils.isBlank(chars)) {
            throw new IllegalArgumentException(String.format(message, values));
        } else {
            return chars;
        }
    }

    public static <T extends CharSequence> T notBlank(T chars) {
        return notBlank(chars, "The character sequence is blank !");
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

    public static String notEmpty(String string, String message) {
        if (string == null || string.length() == 0) {
            throw new IllegalArgumentException(message);
        }
        return string;
    }

    public static String notEmpty(String string) {
        return notEmpty(string, "The string is empty !");
    }

    public static <T> T[] notEmpty(T[] array, String message, Object... values) {
        if (array == null) {
            throw new NullPointerException(String.format(message, values));
        } else if (array.length == 0) {
            throw new IllegalArgumentException(String.format(message, values));
        } else {
            return array;
        }
    }

    public static <T> T[] notEmpty(T[] array) {
        return notEmpty(array, "The array is empty !");
    }

    public static <T extends Collection<?>> T notEmpty(T collection, String message, Object... values) {
        if (collection == null) {
            throw new NullPointerException(String.format(message, values));
        } else if (collection.isEmpty()) {
            throw new IllegalArgumentException(String.format(message, values));
        } else {
            return collection;
        }
    }

    public static <T extends Collection<?>> T notEmpty(T collection) {
        return notEmpty(collection, "The collection is empty !");
    }

    public static <T extends Map<?, ?>> T notEmpty(T map, String message, Object... values) {
        if (map == null) {
            throw new NullPointerException(String.format(message, values));
        } else if (map.isEmpty()) {
            throw new IllegalArgumentException(String.format(message, values));
        } else {
            return map;
        }
    }

    public static <T extends Map<?, ?>> T notEmpty(T map) {
        return notEmpty(map, "The map is empty !");
    }

    public static <T extends CharSequence> T notEmpty(T chars, String message, Object... values) {
        if (chars == null) {
            throw new NullPointerException(String.format(message, values));
        } else if (chars.length() == 0) {
            throw new IllegalArgumentException(String.format(message, values));
        } else {
            return chars;
        }
    }

    public static <T extends CharSequence> T notEmpty(T chars) {
        return notEmpty(chars, "The character sequence is empty !");
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

}
