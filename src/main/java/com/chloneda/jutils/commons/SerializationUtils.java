package com.chloneda.jutils.commons;

import java.io.*;
import java.util.Objects;

/**
 * @Created by chloneda
 * @Description: {@link AutoCloseable}
 * 对象序列化与反序列化工具类
 */
public abstract class SerializationUtils {

    private SerializationUtils() {
    }

    public static byte[] serialize(Serializable obj) {
        Objects.requireNonNull(obj, "The Serializable Object must not be null!");

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(obj);
            oos.flush();
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T deserialize(byte[] bytes) {
        Objects.requireNonNull(bytes, "The byte[] must not be null!");
        return deserialize(new ByteArrayInputStream(bytes));
    }

    public static <T> T deserialize(InputStream inputStream) {
        Objects.requireNonNull(inputStream, "The InputStream must not be null!");

        try (ObjectInputStream ois = new ObjectInputStream(inputStream)) {
            return (T) ois.readObject();
        } catch (IOException | ClassNotFoundException var) {
            var.printStackTrace();
            return null;
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
