package com.gitegg.code.generator.engine.enums;


/**
 * @ClassName: CustomFileEnum
 * @Description: 自定义文件列表
 * @author GitEgg
 * @date 2020年09月19日 下午11:49:45
 */
public enum CustomFileEnum {

    /**
     * DTO_FILE
     */
    DTO_FILE("DTO", "/templates/dto.java.ftl"),

    /**
     * CREATE_DTO
     */
    CREATE_DTO("CREATE_DTO", "/templates/createDTO.java.ftl"),

    /**
     * UPDATE_DTO
     */
    UPDATE_DTO("UPDATE_DTO", "/templates/updateDTO.java.ftl"),

    /**
     * QUERY_DTO
     */
    QUERY_DTO("QUERY_DTO", "/templates/queryDTO.java.ftl"),

    /**
     * EXPORT
     */
    EXPORT("EXPORT", "/templates/entityExport.java.ftl"),

    /**
     * IMPORT
     */
    IMPORT("IMPORT", "/templates/entityImport.java.ftl"),

    /**
     * SQL
     */
    SQL("SQL", "/templates/resource.sql.ftl"),

    /**
     * VUE
     */
    VUE("VUE", "/templates/pageListTable.vue.ftl"),

    /**
     * JS
     */
    JS("JS", "/templates/pageListTable.js.ftl");

    public String name;

    public String path;

    CustomFileEnum(String name, String path) {
        this.name = name;
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
