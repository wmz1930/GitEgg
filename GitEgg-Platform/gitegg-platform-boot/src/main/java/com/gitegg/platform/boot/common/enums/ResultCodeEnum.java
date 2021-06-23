package com.gitegg.platform.boot.common.enums;


/**
 * @ClassName: ResultCodeEnum
 * @Description: 自定义返回码枚举
 * @author GitEgg
 * @date 2020年09月19日 下午11:49:45
 */
public enum ResultCodeEnum {

    /**
     * 成功
     */
    SUCCESS(200, "操作成功"),

    /**
     * 系统错误
     */
    ERROR(500, "系统错误"),

    /**
     * 操作失败
     */
    FAILED(101, "操作失败"),

    /**
     * 未登录/登录超时
     */
    UNAUTHORIZED(102, "登录超时"),

    /**
     * 参数错误
     */
    PARAM_ERROR(103, "参数错误"),

    /**
     * 参数错误-已存在
     */
    INVALID_PARAM_EXIST(104, "请求参数已存在"),

    /**
     * 参数错误
     */
    INVALID_PARAM_EMPTY(105, "请求参数为空"),

    /**
     * 参数错误
     */
    PARAM_TYPE_MISMATCH(106, "参数类型不匹配"),

    /**
     * 参数错误
     */
    PARAM_VALID_ERROR(107, "参数校验失败"),

    /**
     * 参数错误
     */
    ILLEGAL_REQUEST(108, "非法请求"),

    /**
     * 验证码错误
     */
    INVALID_VCODE(204, "验证码错误"),

    /**
     * 用户名或密码错误
     */
    INVALID_USERNAME_PASSWORD(205, "账号或密码错误"),

    /**
     *
     */
    INVALID_RE_PASSWORD(206, "两次输入密码不一致"),

    /**
     * 用户名或密码错误
     */
    INVALID_OLD_PASSWORD(207, "旧密码错误"),

    /**
     * 用户名重复
     */
    USERNAME_ALREADY_IN(208, "用户名已存在"),

    /**
     * 用户不存在
     */
    INVALID_USERNAME(209, "用户名不存在"),

    /**
     * 角色不存在
     */
    INVALID_ROLE(210, "角色不存在"),

    /**
     * 角色不存在
     */
    ROLE_USED(211, "角色使用中，不可删除"),

    /**
     * 没有权限
     */
    NO_PERMISSION(403, "当前用户无该接口权限");

    public int code;

    public String msg;

    ResultCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
