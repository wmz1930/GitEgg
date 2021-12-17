package com.gitegg.code.generator.enums;


import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: NumberMaxEnum
 * @Description: NumberMaxEnum
 * @author GitEgg
 * @date 2020年09月19日 下午11:49:45
 */
public enum NumberMaxEnum {

    /**
     * byte
     */
    BYTE("Byte", String.valueOf(Byte.MAX_VALUE)),

    /**
     * Short
     */
    SHORT("Short", String.valueOf(Short.MAX_VALUE)),

    /**
     * Integer
     */
    INTEGER("Integer", String.valueOf(Integer.MAX_VALUE)),

    /**
     * 更新时间
     */
    LONG("Long", String.valueOf(Long.MAX_VALUE)),

    /**
     * 更新者
     */
    FLOAT("Float", String.valueOf(Float.MAX_VALUE)),

    /**
     * 是否删除
     */
    DOUBLE("Double", String.valueOf(Double.MAX_VALUE));

    public String type;

    public String range;

    NumberMaxEnum(String type, String range) {
        this.type = type;
        this.range = range;
    }

    public static String getRange(String type) {
        NumberMaxEnum[] rangeEnums = values();
        for (NumberMaxEnum numberMaxEnum : rangeEnums) {
            if (numberMaxEnum.getType() == type) {
                return numberMaxEnum.getRange();
            }
        }
        return null;
    }

    public static List<String> getNumberMaxEnumList() {
        //获取枚举值转list集合
        //这个model是自定义的一个类  放了两个字段，一个枚举值  一个枚举名称
        List<String> list = new ArrayList<>();
        for (NumberMaxEnum numberMaxEnum : values()) {
            list.add(numberMaxEnum.getRange());
        }
        return list;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

}
