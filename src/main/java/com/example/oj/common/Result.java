package com.example.oj.common;

import lombok.Data;

import java.io.Serializable;


/**
 * 后端统一返回结果
 * @param <T>
 */
@Data
public class Result<T> implements Serializable {

    private Integer code; //编码
    private String msg; //错误信息
    private T data; //数据

    public static <T> Result<T> success() {
        Result<T> result = new Result<T>();
        result.code = 200;

        return result;
    }

    public static <T> Result<T> success(T object) {
        Result<T> result = new Result<T>();
        result.data = object;
        result.code = 200;
        return result;
    }

    public static <T> Result<T> error(String msg) {
        Result result = new Result();
        result.msg = msg;
        result.code = 400;
        return result;
    }
    public static <T> Result<T> error(Integer code,String msg) {
        Result result = new Result();
        result.msg = msg;
        result.code = code;
        return result;
    }
    public static <T> Result<T> error(ErrorCode errorCode,String msg) {
        Result result = new Result();
        result.msg = msg;
        result.code = errorCode.getCode();
        return result;
    }
    public static <T> Result<T> build(T body, Integer code, String message) {
        Result<T> result = new Result<>();
        result.setData(body);
        result.setCode(code);
        result.setMsg(message);
        return result;
    }


}
