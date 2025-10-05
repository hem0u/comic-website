package com.comic.vo;

import lombok.Data;

@Data
public class ResultVO<T> {
    // 状态码：200成功，其他失败
    private int code;
    // 提示信息
    private String msg;
    // 返回数据
    private T data;

    // 成功且带数据
    public static <T> ResultVO<T> success(T data) {
        ResultVO<T> result = new ResultVO<>();
        result.setCode(200);
        result.setMsg("成功");
        result.setData(data);
        return result;
    }

    // 成功但不带数据
    public static <T> ResultVO<T> success() {
        return success(null);
    }

    // 失败
    public static <T> ResultVO<T> error(String msg) {
        ResultVO<T> result = new ResultVO<>();
        result.setCode(500);
        result.setMsg(msg);
        result.setData(null);
        return result;
    }
}