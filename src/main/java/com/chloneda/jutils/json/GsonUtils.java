package com.chloneda.jutils.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;

import java.lang.reflect.Type;

/**
 * Created by chloneda
 * Description:
 */
public class GsonUtils {

    private static final GsonBuilder GSON_INSTANCE = new GsonBuilder();
    private static volatile Gson gson;

    private GsonUtils() {}

    static {
        gson = GSON_INSTANCE.create();
    }

    public static Gson getGson() {
        return gson;
    }

    public static synchronized void registerTypeAdapter(Type type, TypeAdapter typeAdapter) {
        GSON_INSTANCE.registerTypeAdapter(type, typeAdapter);
        gson = GSON_INSTANCE.create();
    }

}
