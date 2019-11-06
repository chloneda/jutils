package com.chloneda.jutils.random;

import org.apache.commons.codec.binary.Base64;

import java.util.UUID;

/**
 * @Created by chloneda
 * @Description: UUID (Universally Unique Identifier),通用唯一识别码
 */
public class UUIDUtils {

    public UUIDUtils() {
    }

    public static String createUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    public static String compressedUUID() {
        UUID uuid = UUID.randomUUID();
        return compressedUUID(uuid);
    }

    public static String compactUUID() {
        UUID uuid = UUID.randomUUID();
        String result = uuid.toString();
        return result.replaceAll("-", "");
    }

    protected static String compressedUUID(UUID uuid) {
        byte[] byUUID = new byte[16];
        long least = uuid.getLeastSignificantBits();
        long most = uuid.getMostSignificantBits();
        long2bytes(most, byUUID, 0);
        long2bytes(least, byUUID, 8);
        String compressUUID = Base64.encodeBase64URLSafeString(byUUID);
        return compressUUID;
    }

    protected static void long2bytes(long value, byte[] bytes, int offset) {
        for(int i = 7; i > -1; --i) {
            bytes[offset++] = (byte)((int)(value >> 8 * i & 255L));
        }

    }

    public static String compress(String uuidString) {
        UUID uuid = UUID.fromString(uuidString);
        return compressedUUID(uuid);
    }

    public static String uncompress(String compressedUUID) {
        if (compressedUUID.length() != 22) {
            throw new IllegalArgumentException("Invalid uuid!");
        } else {
            byte[] byUUID = Base64.decodeBase64(compressedUUID + "==");
            long most = bytes2long(byUUID, 0);
            long least = bytes2long(byUUID, 8);
            UUID uuid = new UUID(most, least);
            return uuid.toString();
        }
    }

    protected static long bytes2long(byte[] bytes, int offset) {
        long value = 0L;

        for(int i = 7; i > -1; --i) {
            value |= ((long)bytes[offset++] & 255L) << 8 * i;
        }

        return value;
    }

}
