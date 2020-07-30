package com.wry.generator.result;


/**
 * 返回结果的状态码及提示信息
 */

public enum ResultEnum {

    /**
     * 操作成功，状态码为：200
     * 提示信息为操作成功
     */
    SUCCESS_OPTION("200", "操作成功"),

    /**
     * 自定义异常
     * 提示信息为 数据源线程池名称已经存在
     */
    DATA_SOURCE_KEY_EXISTS("501", "数据源线程池名称已经存在"),

    /**
     * 自定义异常
     * 提示信息为 不支持当前数据库
     */
    CURRENT_DATASOURCE_NOT_SUPPORTED("502", "不支持当前数据库"),

    /**
     * 自定义异常
     * 提示信息为 不存在当前数据源
     */
    CURRENT_DATASOURCE_NOT_EXISTS("503", "不存在当前数据源"),

    /**
     * 自定义异常
     * 提示信息为 数据源连接错误
     */
    DATA_SOURCE_CONNECTION_ERROR("504", "数据源连接错误"),
    /**
     * 自定义异常
     * 提示信息为 方言切换失败
     */
    DIALECT_SWITCHING_FAILED("505", "方言切换失败"),
    ;

    /**
     * 返回的状态码
     */
    private String code;
    /**
     * 返回的提示信息
     */
    private String msg;

    ResultEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }


    public String getMsg() {
        return msg;
    }


    public void setCode(String code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
