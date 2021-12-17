package com.gitegg.code.generator.engine.enums;


/**
 * @ClassName: CustomFileEnum
 * @Description: 自定义文件列表
 * @author GitEgg
 * @date 2020年09月19日 下午11:49:45
 */
public enum CustomFileEnum {

    /**
     * ENTITY_FILE
     */
    ENTITY_FILE("ENTITY", "/templates/singleTable/entity.java"),

    /**
     * SERVICE_FILE
     */
    SERVICE_FILE("SERVICE", "/templates/singleTable/service.java"),

    /**
     * SERVICE_IMPL_FILE
     */
    SERVICE_IMPL_FILE("SERVICE_IMPL", "/templates/singleTable/serviceImpl.java"),

    /**
     * MAPPER_FILE
     */
    MAPPER_FILE("MAPPER", "/templates/singleTable/mapper.java"),

    /**
     * CONTROLLER_FILE
     */
    CONTROLLER_FILE("CONTROLLER", "/templates/singleTable/controller.java"),

    /**
     * MAPPER_XML_FILE
     */
    MAPPER_XML_FILE("MAPPER_XML", "/templates/singleTable/mapper.xml"),

    /**
     * DTO_FILE
     */
    DTO_FILE("DTO", "/templates/singleTable/dto.java.ftl"),

    /**
     * CREATE_DTO
     */
    CREATE_DTO("CREATE_DTO", "/templates/singleTable/createDTO.java.ftl"),

    /**
     * UPDATE_DTO
     */
    UPDATE_DTO("UPDATE_DTO", "/templates/singleTable/updateDTO.java.ftl"),

    /**
     * QUERY_DTO
     */
    QUERY_DTO("QUERY_DTO", "/templates/singleTable/queryDTO.java.ftl"),

    /**
     * EXPORT
     */
    EXPORT("EXPORT", "/templates/singleTable/entityExport.java.ftl"),

    /**
     * IMPORT
     */
    IMPORT("IMPORT", "/templates/singleTable/entityImport.java.ftl"),

    /**
     * SQL
     */
    SQL("SQL", "/templates/singleTable/resource.sql.ftl"),

    /**
     * VUE
     */
    VUE("VUE", "/templates/singleTable/pageListTable.vue.ftl"),

    /**
     * JS
     */
    JS("JS", "/templates/singleTable/pageListTable.js.ftl");

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
