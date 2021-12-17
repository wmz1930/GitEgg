package com.gitegg.code.generator.enums;


import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: NumberMinEnum
 * @Description: NumberMaxEnum
 * @author GitEgg
 * @date 2020年09月19日 下午11:49:45
 */
public enum NumberMinEnum {

    /**
     * byte
     */
    BYTE("Byte", String.valueOf(Byte.MIN_VALUE)),

    /**
     * Short
     */
    SHORT("Short", String.valueOf(Short.MIN_VALUE)),

    /**
     * Integer
     */
    INTEGER("Integer", String.valueOf(Integer.MIN_VALUE)),

    /**
     * 更新时间
     */
    LONG("Long", String.valueOf(Long.MIN_VALUE)),

    /**
     * 更新者
     */
    FLOAT("Float", String.valueOf(Float.MIN_VALUE)),

    /**
     * 是否删除
     */
    DOUBLE("Double", String.valueOf(Double.MIN_VALUE));

    public String type;

    public String range;

    NumberMinEnum(String type, String range) {
        this.type = type;
        this.range = range;
    }

    public static String getRange(String type) {
        NumberMinEnum[] rangeEnums = values();
        for (NumberMinEnum numberMinEnum : rangeEnums) {
            if (numberMinEnum.getType() == type) {
                return numberMinEnum.getRange();
            }
        }
        return null;
    }

    public static List<String> getNumberMinEnumList() {
        //获取枚举值转list集合
        //这个model是自定义的一个类  放了两个字段，一个枚举值  一个枚举名称
        List<String> list = new ArrayList<>();
        for (NumberMinEnum numberMinEnum : values()) {
            list.add(numberMinEnum.getRange());
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
