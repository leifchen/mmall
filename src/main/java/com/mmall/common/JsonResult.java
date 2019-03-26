package com.mmall.common;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * 统一返回结果
 * <p>
 * @Author LeifChen
 * @Date 2019-02-26
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class JsonResult<T> implements Serializable {

    private int status;
    private String msg;
    private T data;

    private JsonResult(int status) {
        this.status = status;
    }

    private JsonResult(int status, T data) {
        this.status = status;
        this.data = data;
    }

    private JsonResult(int status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    private JsonResult(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    @JsonIgnore
    public boolean isSuccess() {
        return this.status == ResponseCodeEnum.SUCCESS.getCode();
    }

    public int getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }

    public static <T> JsonResult<T> success() {
        return new JsonResult<>(ResponseCodeEnum.SUCCESS.getCode());
    }

    public static <T> JsonResult<T> success(String msg) {
        return new JsonResult<>(ResponseCodeEnum.SUCCESS.getCode(), msg);
    }

    public static <T> JsonResult<T> success(T data) {
        return new JsonResult<>(ResponseCodeEnum.SUCCESS.getCode(), data);
    }

    public static <T> JsonResult<T> success(String msg, T data) {
        return new JsonResult<>(ResponseCodeEnum.SUCCESS.getCode(), msg, data);
    }

    public static <T> JsonResult<T> error() {
        return new JsonResult<>(ResponseCodeEnum.ERROR.getCode(), ResponseCodeEnum.ERROR.getDesc());
    }

    public static <T> JsonResult<T> error(String errorMessage) {
        return new JsonResult<>(ResponseCodeEnum.ERROR.getCode(), errorMessage);
    }

    public static <T> JsonResult<T> error(int errorCode, String errorMessage) {
        return new JsonResult<>(errorCode, errorMessage);
    }
}
