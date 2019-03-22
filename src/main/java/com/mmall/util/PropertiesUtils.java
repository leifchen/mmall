package com.mmall.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * 配置文件工具类
 * <p>
 * @Author LeifChen
 * @Date 2019-02-27
 */
@Slf4j
public class PropertiesUtils {

    private static Properties props;

    static {
        String fileName = "mmall.properties";
        props = new Properties();
        try {
            props.load(new InputStreamReader(PropertiesUtils.class.getClassLoader().getResourceAsStream(fileName), StandardCharsets.UTF_8));
        } catch (IOException e) {
            log.error("配置文件读取异常", e);
        }
    }

    public static String getProperty(String key) {
        String value = props.getProperty(key.trim());
        if (StringUtils.isBlank(value)) {
            return null;
        }
        return value.trim();
    }

    public static String getProperty(String key, String defaultValue) {
        String value = props.getProperty(key.trim());
        if (StringUtils.isBlank(value)) {
            value = defaultValue;
        }
        return value.trim();
    }
}
