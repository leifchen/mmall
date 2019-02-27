package com.mmall.util;

import org.apache.commons.codec.binary.Hex;

import java.security.MessageDigest;

/**
 * MD5加密工具类
 * <p>
 * @Author LeifChen
 * @Date 2019-02-26
 */
public class Md5Utils {

    public static String encode(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            str += PropertiesUtil.getProperty("password.salt", "");
            return Hex.encodeHexString(md.digest(str.getBytes())).toUpperCase();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
