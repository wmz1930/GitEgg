package com.gitegg.platform.base.enums;


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
