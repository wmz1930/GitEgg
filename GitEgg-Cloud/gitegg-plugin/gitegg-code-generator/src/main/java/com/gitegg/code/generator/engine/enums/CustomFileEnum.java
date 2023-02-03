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
    ENTITY_FILE("ENTITY", "/entity/entity.java"),

    /**
     * SERVICE_FILE
     */
    SERVICE_FILE("SERVICE", "/service/service.java"),

    /**
     * SERVICE_IMPL_FILE
     */
    SERVICE_IMPL_FILE("SERVICE_IMPL", "/service/impl/serviceImpl.java"),

    /**
     * MAPPER_FILE
     */
    MAPPER_FILE("MAPPER", "/mapper/mapper.java"),

    /**
     * CONTROLLER_FILE
     */
    CONTROLLER_FILE("CONTROLLER", "/controller/controller.java"),

    /**
     * MAPPER_XML_FILE
     */
    MAPPER_XML_FILE("MAPPER_XML", "/mapper/mapper.xml"),

    /**
     * DTO_FILE
     */
    DTO_FILE("DTO", "/dto/dto.java.ftl"),

    /**
     * CREATE_DTO
     */
    CREATE_DTO("CREATE_DTO", "/dto/createDTO.java.ftl"),

    /**
     * UPDATE_DTO
     */
    UPDATE_DTO("UPDATE_DTO", "/dto/updateDTO.java.ftl"),

    /**
     * QUERY_DTO
     */
    QUERY_DTO("QUERY_DTO", "/dto/queryDTO.java.ftl"),

    /**
     * EXPORT
     */
    EXPORT("EXPORT", "/bo/exportBO.java.ftl"),

    /**
     * IMPORT
     */
    IMPORT("IMPORT", "/bo/importBO.java.ftl"),

    /**
     * SQL
     */
    SQL("SQL", "/resource.sql.ftl"),

    /**
     * VUE
     */
    VUE("VUE", ".vue.ftl"),

    /**
     * JS
     */
    JS("JS", "/api.js.ftl"),
    
    /**
     * VUE_FORM
     */
    VUE_FORM("VUE_FORM", "/Form.vue.ftl"),
    
    /**
     * SCHEMA_TS
     */
    SCHEMA_TS("SCHEMA_TS", "/schema.ts.ftl"),
    
    /**
     * TS
     */
    TS("TS", "/api.ts.ftl");

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
