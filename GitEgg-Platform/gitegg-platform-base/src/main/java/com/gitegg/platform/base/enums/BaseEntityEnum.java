package com.gitegg.platform.base.enums;


import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: BaseEntityEnum
 * @Description: BaseEntityEnum
 * @author GitEgg
 * @date 2020年09月19日 下午11:49:45
 */
public enum BaseEntityEnum {

    /**
     * 租户id
     */
    TENANT_ID("tenant_id", "tenantId"),

    /**
     * 创建时间
     */
    CREATE_TIME("create_time", "createTime"),

    /**
     * 创建者
     */
    CREATOR("creator", "creator"),

    /**
     * 更新时间
     */
    UPDATE_TIME("update_time", "updateTime"),

    /**
     * 更新者
     */
    OPERATOR("operator", "operator"),

    /**
     * 是否删除
     */
    DEL_FLAG("del_flag", "delFlag");

    public String field;

    public String entity;

    BaseEntityEnum(String field, String entity) {
        this.field = field;
        this.entity = entity;
    }

    public static List<String> getBaseEntityFieldList() {
        //获取枚举值转list集合
        //这个model是自定义的一个类  放了两个字段，一个枚举值  一个枚举名称
        List<String> list = new ArrayList<>();
        for (BaseEntityEnum baseEntityEnum : values()) {
            list.add(baseEntityEnum.getField());
        }
        return list;
    }


    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }
}
