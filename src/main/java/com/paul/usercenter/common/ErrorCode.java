package com.paul.usercenter.common;

/**
 * 错误码
 *
 *
 */
public enum ErrorCode {

    PARAMS_ERROR(40000,"请求参数错误",""),
    PARAMS_NULL_ERROR(40001,"请求数据为空",""),
    NO_AUTH(40101,"无权限",""),
    NO_LOGIN(40100,"未登录",""),
    SYSTEM_ERROR(50000,"系统内部异常","");


    private final int code;
    /**
     * 状态码信息
     */
    private final String message;
    /**
     * 状态码描述
     */

    private final String description;

    ErrorCode( int code,String message, String description) {
        this.message = message;
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }
}
