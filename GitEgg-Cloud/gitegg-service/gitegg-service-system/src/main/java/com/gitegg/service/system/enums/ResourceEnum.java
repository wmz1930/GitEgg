package com.gitegg.service.system.enums;

/**
 * 资源权限类型枚举
 * @author GitEgg
 */

public enum ResourceEnum {

    /**
     * 模块
     */
    MODULE("1", "模块"),

    /**
     * 菜单
     */
    MENU("2", "菜单"),

    /**
     * 按钮
     */
    BUTTON("3", "按钮"),

    /**
     * 请求/链接
     */
    URI("4", "请求");

    public String code;

    public String value;

    ResourceEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
