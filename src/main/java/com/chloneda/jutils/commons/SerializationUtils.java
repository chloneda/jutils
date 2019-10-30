package com.chloneda.jutils.commons;

import java.io.*;

/**
 * @Created by chloneda
 * @Description: {@link AutoCloseable}
 * 对象序列化与反序列化工具类
 */
public abstract class SerializationUtils {

    private SerializationUtils() {
    }

    public static byte[] serialize(Object object) {
        if (object == null) {
            return null;
        }
        ByteArrayOutputStream baos = null;
        ObjectOutputStream oos =null;
        try {
            baos = new ByteArrayOutputStream(1024);
            oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            oos.flush();
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }finally {
            close(baos);
            close(oos);
        }
    }

    public static Object deserialize(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        ByteArrayInputStream bis = null;
        ObjectInputStream ois = null;
        try {
            bis = new ByteArrayInputStream(bytes);
            ois = new ObjectInputStream(bis);
            return ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        } finally {
            close(bis);
            close(ois);
        }
    }

    public static void close(AutoCloseable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
