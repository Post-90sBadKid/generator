package com.wry.generator.exception;


import com.wry.generator.result.ResultEnum;

/**
 * 自定义异常类
 */

public class CustomException extends RuntimeException {

    /**
     * 异常状态码
     */
    private final String code;
    /**
     * 异常信息
     */
    private final String msg;


    public CustomException(String code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public CustomException(ResultEnum resultEnum) {
        super(resultEnum.getMsg());
        this.code = resultEnum.getCode();
        this.msg = resultEnum.getMsg();
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
