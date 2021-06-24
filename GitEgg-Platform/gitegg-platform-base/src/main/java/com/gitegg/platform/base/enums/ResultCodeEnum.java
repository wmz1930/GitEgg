package com.gitegg.platform.base.enums;


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
     * 未登录/登录超时
     */
    UNAUTHORIZED(401, "登录超时"),

    /**
     * 账号密码尝试次数过多，被锁定
     */
    PASSWORD_TRY_MAX_ERROR(402, "密码尝试次数过多，账号已被锁定"),

    /**
     * 没有权限
     */
    NO_PERMISSION(403, "当前用户无该接口权限"),

    /**
     * 操作失败
     */
    FAILED(410, "操作失败"),

    /**
     * 参数错误
     */
    PARAM_ERROR(411, "参数错误"),

    /**
     * 参数错误-已存在
     */
    INVALID_PARAM_EXIST(412, "请求参数已存在"),

    /**
     * 参数错误
     */
    INVALID_PARAM_EMPTY(413, "请求参数为空"),

    /**
     * 参数错误
     */
    PARAM_TYPE_MISMATCH(414, "参数类型不匹配"),

    /**
     * 参数错误
     */
    PARAM_VALID_ERROR(415, "参数校验失败"),

    /**
     * 参数错误
     */
    ILLEGAL_REQUEST(416, "非法请求"),

    /**
     * 验证码错误
     */
    INVALID_CAPTCHA_TYPE(417, "验证码类型不匹配"),

    /**
     * 验证码错误
     */
    INVALID_CAPTCHA(418, "验证码错误"),

    /**
     * 用户名或密码错误
     */
    INVALID_USERNAME_PASSWORD(419, "账号或密码错误"),

    /**
     *
     */
    INVALID_RE_PASSWORD(420, "两次输入密码不一致"),

    /**
     * 用户名或密码错误
     */
    INVALID_OLD_PASSWORD(421, "旧密码错误"),

    /**
     * 用户名重复
     */
    USERNAME_ALREADY_IN(422, "用户名已存在"),

    /**
     * 用户不存在
     */
    INVALID_USERNAME(423, "用户名不存在"),

    /**
     * 角色不存在
     */
    INVALID_ROLE(424, "角色不存在"),

    /**
     * 角色不存在
     */
    ROLE_USED(425, "角色使用中，不可删除"),

    /**
     * 已开启租户模式，未在请求中查询到租户信息
     */
    TENANT_NOT_FOUND(426, "已开启租户模式，未在请求中查询到租户信息"),

    /**
     * 密码错误次数超过最大限值，请选择验证码模式登录
     */
    INVALID_PASSWORD_CAPTCHA(427, "密码错误次数超过最大限值，请进行安全认证"),

    /**
     * 用户不存在
     */
    DISABLED_ACCOUNT(428, "账号已被禁用"),

    /**
     * 系统繁忙，请稍后重试
     */
    SYSTEM_BUSY(429, "系统繁忙，请稍后重试"),

    /**
     * 系统错误
     */
    ERROR(500, "系统错误");

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
