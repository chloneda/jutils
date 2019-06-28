package com.magic.utils.commons;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by chloneda
 * Description:
 */
public class StringFormater {
    static final char DELIM_START = '{';
    static final char DELIM_STOP = '}';
    static final String DELIM_STR = "{}";
    private static final char ESCAPE_CHAR = '\\';

    public StringFormater() {
    }

    public static final String format(String messagePattern, Object... argArray) {
        if (messagePattern == null) {
            return null;
        } else if (argArray == null) {
            return messagePattern;
        } else {
            int i = 0;
            StringBuffer sbuf = new StringBuffer(messagePattern.length() + 50);

            for (int L = 0; L < argArray.length; ++L) {
                int j = messagePattern.indexOf("{}", i);
                if (j == -1) {
                    if (i == 0) {
                        return messagePattern;
                    }

                    sbuf.append(messagePattern.substring(i, messagePattern.length()));
                    return sbuf.toString();
                }

                if (isEscapedDelimeter(messagePattern, j)) {
                    if (!isDoubleEscaped(messagePattern, j)) {
                        --L;
                        sbuf.append(messagePattern.substring(i, j - 1));
                        sbuf.append('{');
                        i = j + 1;
                    } else {
                        sbuf.append(messagePattern.substring(i, j - 1));
                        deeplyAppendParameter(sbuf, argArray[L], new HashMap());
                        i = j + 2;
                    }
                } else {
                    sbuf.append(messagePattern.substring(i, j));
                    deeplyAppendParameter(sbuf, argArray[L], new HashMap());
                    i = j + 2;
                }
            }

            sbuf.append(messagePattern.substring(i, messagePattern.length()));
            return sbuf.toString();
        }
    }

    static final boolean isEscapedDelimeter(String messagePattern, int delimeterStartIndex) {
        if (delimeterStartIndex == 0) {
            return false;
        } else {
            char potentialEscape = messagePattern.charAt(delimeterStartIndex - 1);
            return potentialEscape == '\\';
        }
    }

    static final boolean isDoubleEscaped(String messagePattern, int delimeterStartIndex) {
        return delimeterStartIndex >= 2 && messagePattern.charAt(delimeterStartIndex - 2) == '\\';
    }

    private static void deeplyAppendParameter(StringBuffer sbuf, Object o, Map<Object, Object> seenMap) {
        if (o == null) {
            sbuf.append("null");
        } else {
            if (!o.getClass().isArray()) {
                sbuf.append(o);
            } else if (o instanceof boolean[]) {
                booleanArrayAppend(sbuf, (boolean[]) ((boolean[]) o));
            } else if (o instanceof byte[]) {
                byteArrayAppend(sbuf, (byte[]) ((byte[]) o));
            } else if (o instanceof char[]) {
                charArrayAppend(sbuf, (char[]) ((char[]) o));
            } else if (o instanceof short[]) {
                shortArrayAppend(sbuf, (short[]) ((short[]) o));
            } else if (o instanceof int[]) {
                intArrayAppend(sbuf, (int[]) ((int[]) o));
            } else if (o instanceof long[]) {
                longArrayAppend(sbuf, (long[]) ((long[]) o));
            } else if (o instanceof float[]) {
                floatArrayAppend(sbuf, (float[]) ((float[]) o));
            } else if (o instanceof double[]) {
                doubleArrayAppend(sbuf, (double[]) ((double[]) o));
            } else {
                objectArrayAppend(sbuf, (Object[]) ((Object[]) o), seenMap);
            }

        }
    }

    private static void objectArrayAppend(StringBuffer sbuf, Object[] a, Map<Object, Object> seenMap) {
        sbuf.append('[');
        if (!seenMap.containsKey(a)) {
            seenMap.put(a, (Object) null);
            int len = a.length;

            for (int i = 0; i < len; ++i) {
                deeplyAppendParameter(sbuf, a[i], seenMap);
                if (i != len - 1) {
                    sbuf.append(", ");
                }
            }

            seenMap.remove(a);
        } else {
            sbuf.append("...");
        }

        sbuf.append(']');
    }

    private static void booleanArrayAppend(StringBuffer sbuf, boolean[] a) {
        sbuf.append('[');
        int len = a.length;

        for (int i = 0; i < len; ++i) {
            sbuf.append(a[i]);
            if (i != len - 1) {
                sbuf.append(", ");
            }
        }

        sbuf.append(']');
    }

    private static void byteArrayAppend(StringBuffer sbuf, byte[] a) {
        sbuf.append('[');
        int len = a.length;

        for (int i = 0; i < len; ++i) {
            sbuf.append(a[i]);
            if (i != len - 1) {
                sbuf.append(", ");
            }
        }

        sbuf.append(']');
    }

    private static void charArrayAppend(StringBuffer sbuf, char[] a) {
        sbuf.append('[');
        int len = a.length;

        for (int i = 0; i < len; ++i) {
            sbuf.append(a[i]);
            if (i != len - 1) {
                sbuf.append(", ");
            }
        }

        sbuf.append(']');
    }

    private static void shortArrayAppend(StringBuffer sbuf, short[] a) {
        sbuf.append('[');
        int len = a.length;

        for (int i = 0; i < len; ++i) {
            sbuf.append(a[i]);
            if (i != len - 1) {
                sbuf.append(", ");
            }
        }

        sbuf.append(']');
    }

    private static void intArrayAppend(StringBuffer sbuf, int[] a) {
        sbuf.append('[');
        int len = a.length;

        for (int i = 0; i < len; ++i) {
            sbuf.append(a[i]);
            if (i != len - 1) {
                sbuf.append(", ");
            }
        }

        sbuf.append(']');
    }

    private static void longArrayAppend(StringBuffer sbuf, long[] a) {
        sbuf.append('[');
        int len = a.length;

        for (int i = 0; i < len; ++i) {
            sbuf.append(a[i]);
            if (i != len - 1) {
                sbuf.append(", ");
            }
        }

        sbuf.append(']');
    }

    private static void floatArrayAppend(StringBuffer sbuf, float[] a) {
        sbuf.append('[');
        int len = a.length;

        for (int i = 0; i < len; ++i) {
            sbuf.append(a[i]);
            if (i != len - 1) {
                sbuf.append(", ");
            }
        }

        sbuf.append(']');
    }

    private static void doubleArrayAppend(StringBuffer sbuf, double[] a) {
        sbuf.append('[');
        int len = a.length;

        for (int i = 0; i < len; ++i) {
            sbuf.append(a[i]);
            if (i != len - 1) {
                sbuf.append(", ");
            }
        }

        sbuf.append(']');
    }
}
