package com.gitegg.code.generator.field.util;

import java.util.Arrays;
import java.util.List;

/**
 * @author gitegg
 */
public class FieldUtils {

    /**
     * 判断是否是排除字段
     *
     * @param columnName
     * @return
     * @throws Exception
     */
    public static boolean isExcludeField(String columnName) {
        List<String> excludeFieldList = Arrays.asList("tenant_id", "create_time", "creator", "update_time", "operator", "del_flag");
        return excludeFieldList.contains(columnName);
    }
}
