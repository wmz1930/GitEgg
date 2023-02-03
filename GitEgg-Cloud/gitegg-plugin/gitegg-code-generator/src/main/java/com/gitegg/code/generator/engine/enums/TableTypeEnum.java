package com.gitegg.code.generator.engine.enums;


/**
 * @ClassCode: TableTypeEnum
 * @Description: 联表数据类型
 * @author GitEgg
 * @date 2020年09月19日 下午11:49:45
 */
public enum TableTypeEnum {

    /**
     * 单表
     */
    SINGLE("single", "单表"),

    /**
     * 多表查询
     */
    JOIN_QUERY("join_query", "多表查询"),

    /**
     * 主表子表
     */
    MAIN_SUB("main_sub", "主表子表");

    public String code;

    public String name;

    TableTypeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
