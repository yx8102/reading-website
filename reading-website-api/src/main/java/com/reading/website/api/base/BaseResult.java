package com.reading.website.api.base;

import lombok.Data;

import java.io.Serializable;

/**
 * 返回参数
 *
 * @yx8102 2019/1/8
 */
@Data
public class BaseResult<T> implements Serializable {
    /**
     * 是否成功
     */
    private Boolean success;

    /**
     * 数据
     */
    private T data;

    /**
     * 状态码
     */
    private int code;

    /**
     * 信息
     */
    private String message;

    /**
     * 分页信息
     */
    private Page page;

    public BaseResult() {
        this.success = true;
    }

    private BaseResult(Boolean success, T data, int code, String message) {
        this.success = success;
        this.data = data;
        this.code = code;
        this.message = message;
    }

    private BaseResult(Boolean success, T data, int code, String message, Page page) {
        this.success = success;
        this.data = data;
        this.code = code;
        this.message = message;
        this.page = page;
    }
    public static <T> BaseResult<T> rightReturn (T data) {
        return new BaseResult<T>(true, data, StatusCodeEnum.SUCCESS.getCode(), "");
    }

    public static <T> BaseResult<T> rightReturn (T data, Page page) {
        return new BaseResult<T>(true, data, StatusCodeEnum.SUCCESS.getCode(), "", page);
    }

    public static <T> BaseResult<T> rightReturn (T data, int code, String message) {
        return new BaseResult<T>(true, data, code, message);
    }

    public static <T> BaseResult<T> errorReturn (T data, int code, String message) {
        return new BaseResult<T>(false, data, code, message);
    }

    public static <T> BaseResult<T> errorReturn (int code, String message) {
        return new BaseResult<T>(false, null, code, message);
    }

}
