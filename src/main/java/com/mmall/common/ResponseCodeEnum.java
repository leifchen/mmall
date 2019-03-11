package com.mmall.common;

/**
 * 返回编码枚举
 * <p>
 * @Author LeifChen
 * @Date 2019-02-26
 */
public enum ResponseCodeEnum {

    // 成功
    SUCCESS(0, "SUCCESS"),
    // 失败
    ERROR(1, "ERROR"),
    // 参数不合法
    ILLEGAL_ARGUMENT(2, "ILLEGAL_ARGUMENT"),
    // 需要登录
    NEED_LOGIN(10, "NEED_LOGIN");

    private final int code;
    private final String desc;

    ResponseCodeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
