package com.gitegg.platform.mybatis.enums;


/**
 * @ClassName: DataPermissionTypeEnum
 * @Description: 数据权限类型枚举类
 * @author GitEgg
 * @date 2020年09月19日 下午11:49:45
 */
public enum DataPermissionTypeEnum {

    /**
     * 只能查看本人
     */
    DATA_PERMISSION_SELF("1", "DATA_PERMISSION_SELF"),

    /**
     * 只能查看本组织
     */
    DATA_PERMISSION_ORG("2", "DATA_PERMISSION_ORG"),

    /**
     * 只能查看本组织及子组织
     */
    DATA_PERMISSION_ORG_AND_CHILD("3", "DATA_PERMISSION_ORG_AND_CHILD"),

    /**
     * 可以查看所有数据
     */
    DATA_PERMISSION_ALL("4", "DATA_PERMISSION_ALL"),

    /**
     * 自定义数据权限
     */
    DATA_PERMISSION_CUSTOM("5", "DATA_PERMISSION_CUSTOM");


    public String level;

    public String type;

    DataPermissionTypeEnum(String level, String type) {
        this.level = level;
        this.type = type;
    }

    public static String getType(String level) {
        DataPermissionTypeEnum[] typeEnums = values();
        for (DataPermissionTypeEnum dataPermissionTypeEnum : typeEnums) {
            if (dataPermissionTypeEnum.getLevel() == level) {
                return dataPermissionTypeEnum.getType();
            }
        }
        return null;
    }

    public static String getLevel(String type) {
        DataPermissionTypeEnum[] typeEnums = values();
        for (DataPermissionTypeEnum dataPermissionTypeEnum : typeEnums) {
            if (dataPermissionTypeEnum.getType().equals(type)) {
                return dataPermissionTypeEnum.getLevel();
            }
        }
        return null;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
