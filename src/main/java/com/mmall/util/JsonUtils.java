package com.mmall.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * Json工具类
 * <p>
 * @Author LeifChen
 * @Date 2019-03-20
 */
@Slf4j
public class JsonUtils {

    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        // 对象的所有字段全部列入序列化
        objectMapper.setSerializationInclusion(Inclusion.ALWAYS);
        // 取消默认转换timestamps形式
        objectMapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, false);
        // 忽略空bean转json的错误
        objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
        // 格式化日期格式：yyyy-MM-dd HH:mm:ss
        objectMapper.setDateFormat(new SimpleDateFormat(DateTimeUtils.STANDARD_FORMAT));
        // 忽略在json字符串中存在，但是在java对象中不存在对应属性的情况。防止错误
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * 对象转成json字符串
     * @param obj 对象
     * @param <T>
     * @return
     */
    public static <T> String obj2String(T obj) {
        if (obj == null) {
            return null;
        }
        try {
            return obj instanceof String ? (String) obj : objectMapper.writeValueAsString(obj);
        } catch (IOException e) {
            log.warn("Parse object to String error", e);
            return null;
        }
    }

    /**
     * 对象转成json字符串并格式化
     * @param obj 对象
     * @param <T>
     * @return
     */
    public static <T> String obj2StringPretty(T obj) {
        if (obj == null) {
            return null;
        }
        try {
            return obj instanceof String ? (String) obj : objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (IOException e) {
            log.warn("Parse object to String error", e);
            return null;
        }
    }

    /**
     * json字符串转成对象
     * @param str   json字符串
     * @param clazz 对象类型
     * @param <T>
     * @return
     */
    public static <T> T string2Obj(String str, Class<T> clazz) {
        if (StringUtils.isEmpty(str) || clazz == null) {
            return null;
        }

        try {
            return clazz.equals(String.class) ? (T) str : objectMapper.readValue(str, clazz);
        } catch (IOException e) {
            log.warn("Parse String to Object error", e);
            return null;
        }
    }

    /**
     * json字符串转成复杂对象
     * @param str           json字符串
     * @param typeReference 参考类型
     * @param <T>
     * @return
     */
    public static <T> T string2Obj(String str, TypeReference<T> typeReference) {
        if (StringUtils.isEmpty(str) || typeReference == null) {
            return null;
        }

        try {
            return typeReference.getType().equals(String.class) ? (T) str : objectMapper.readValue(str, typeReference);
        } catch (IOException e) {
            log.warn("Parse String to Object error", e);
            return null;
        }
    }

    /**
     * json字符串转成对象集合
     * @param str             json字符串
     * @param collectionClass 集合对象类型
     * @param elementClasses  元素对象类型
     * @param <T>
     * @return
     */
    public static <T> T string2Obj(String str, Class<?> collectionClass, Class<?>... elementClasses) {
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
        try {
            return objectMapper.readValue(str, javaType);
        } catch (IOException e) {
            log.warn("Parse String to Object error", e);
            return null;
        }
    }
}
