package com.chloneda.jutils.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.JSONLibDataFormatSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;

import javax.xml.crypto.dsig.keyinfo.KeyValue;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by chloneda
 * Description: Json操作工具类，基于fastjson封装
 */
public final class FastjsonUtils {

    /**
     * 默认json格式化方式
     */
    public static final SerializerFeature[] DEFAULT_FORMAT = {
            SerializerFeature.WriteDateUseDateFormat,
            SerializerFeature.WriteEnumUsingToString,
            SerializerFeature.WriteNonStringKeyAsString,
            SerializerFeature.QuoteFieldNames,
            SerializerFeature.SkipTransientField,
            SerializerFeature.SortField,
            SerializerFeature.PrettyFormat};

    private FastjsonUtils() {}

    /**
     * 从json获取指定key的字符串
     *
     * @param json json字符串
     * @param key  字符串的key
     * @return 指定key的值
     */
    public static Object getStringFromJSONObject(final String json, final String key) {
        Objects.requireNonNull(json, "json is null");
        return JSON.parseObject(json).getString(key);
    }

    /**
     * 将字符串转换成JSON字符串
     *
     * @param jsonString json字符串
     * @return 转换成的json对象
     */
    public static JSONObject getJSONFromString(final String jsonString) {
        if (Objects.nonNull(jsonString)) {
            return new JSONObject();
        }
        return JSON.parseObject(jsonString);
    }

    /**
     * 将json字符串，转换成指定java bean
     *
     * @param jsonStr   json串对象
     * @param beanClass 指定的bean
     * @param <T>       任意bean的类型
     * @return 转换后的java bean对象
     */
    public static <T> T toBean(String jsonStr, Class<T> beanClass) {
        Objects.requireNonNull(jsonStr, "jsonStr is null");
        JSONObject jo = JSON.parseObject(jsonStr);
        jo.put(JSON.DEFAULT_TYPE_KEY, beanClass.getName());
        return JSON.parseObject(jo.toJSONString(), beanClass);
    }

    /**
     * @param obj 需要转换的java bean
     * @param <T> 入参对象类型泛型
     * @return 对应的json字符串
     */
    public static <T> String toJson(T obj) {
        Objects.requireNonNull(obj, "obj is null");
        return JSON.toJSONString(obj, DEFAULT_FORMAT);
    }

    /**
     * 通过Map生成一个json字符串
     *
     * @param map 需要转换的map
     * @return json串
     */
    public static String toJson(Map<String, Object> map) {
        Objects.requireNonNull(map, "map is null");
        return JSON.toJSONString(map, DEFAULT_FORMAT);
    }

    /**
     * 美化传入的json,使得该json字符串容易查看
     *
     * @param jsonString 需要处理的json串
     * @return 美化后的json串
     */
    public static String prettyFormatJson(String jsonString) {
        Objects.requireNonNull(jsonString, "jsonString is null");
        return JSON.toJSONString(getJSONFromString(jsonString), true);
    }

    /**
     * 将传入的json字符串转换成Map
     *
     * @param jsonString 需要处理的json串
     * @return 对应的map
     */
    public static Map<String, Object> toMap(String jsonString) {
        Objects.requireNonNull(jsonString, "jsonString is null");
        return getJSONFromString(jsonString);
    }


    /**
     *
     */
    private static final SerializeConfig config;

    static {
        config = new SerializeConfig();
        config.put(java.util.Date.class, new JSONLibDataFormatSerializer()); // 使用和json-lib兼容的日期输出格式
        config.put(java.sql.Date.class, new JSONLibDataFormatSerializer()); // 使用和json-lib兼容的日期输出格式
    }

    private static final SerializerFeature[] features = {SerializerFeature.WriteMapNullValue, // 输出空置字段
        SerializerFeature.WriteNullListAsEmpty, // list字段如果为null，输出为[]，而不是null
        SerializerFeature.WriteNullNumberAsZero, // 数值字段如果为null，输出为0，而不是null
        SerializerFeature.WriteNullBooleanAsFalse, // Boolean字段如果为null，输出为false，而不是null
        SerializerFeature.WriteNullStringAsEmpty // 字符类型字段如果为null，输出为""，而不是null
    };


    public static String toJSONString(Object object) {
        return JSON.toJSONString(object, config, features);
    }

    public static String toJSONNoFeatures(Object object) {
        return JSON.toJSONString(object, config, SerializerFeature.DisableCircularReferenceDetect);
    }


    public static Object toBean(String text) {
        return JSON.parse(text);
    }

    public static <T> T toBean2(String text, Class<T> clazz) {
        return JSON.parseObject(text, clazz);
    }

    // 转换为数组
    public static <T> Object[] toArray(String text) {
        return toArray(text, null);
    }

    // 转换为数组
    public static <T> Object[] toArray(String text, Class<T> clazz) {
        return JSON.parseArray(text, clazz).toArray();
    }

    // 转换为List
    public static <T> List<T> toList(String text, Class<T> clazz) {
        return JSON.parseArray(text, clazz);
    }

    /**
     * 将javabean转化为序列化的json字符串
     * @param keyvalue
     * @return
     */
    public static Object beanToJson(KeyValue keyvalue) {
        String textJson = JSON.toJSONString(keyvalue);
        Object objectJson  = JSON.parse(textJson);
        return objectJson;
    }

    /**
     * 将string转化为序列化的json字符串
     * @param
     * @return
     */
    public static Object textToJson(String text) {
        Object objectJson  = JSON.parse(text);
        return objectJson;
    }

    /**
     * json字符串转化为map
     * @param s
     * @return
     */
    public static Map stringToCollect(String s) {
        Map m = JSONObject.parseObject(s);
        return m;
    }

    /**
     * 将map转化为string
     * @param m
     * @return
     */
    public static String collectToString(Map m) {
        String s = JSONObject.toJSONString(m);
        return s;
    }



    /**
     * 把对象转换为指定对象
     */
    public static <T> T toObjectFromSource(Object source,Class<T> target) {
        return toBean(toJSONString(source),target);
    }

}
