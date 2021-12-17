package com.gitegg.code.generator.engine.enums;


/**
 * @ClassName: CustomFileEnum
 * @Description: 自定义文件列表
 * @author GitEgg
 * @date 2020年09月19日 下午11:49:45
 */
public enum CustomFileMainSubExtendsEnum {

    /**
     * ENTITY_FILE
     */
    ENTITY_FILE("ENTITY", "/templates/mainSubExtends/entity.java"),

    /**
     * SERVICE_FILE
     */
    SERVICE_FILE("SERVICE", "/templates/mainSubExtends/service.java"),

    /**
     * SERVICE_IMPL_FILE
     */
    SERVICE_IMPL_FILE("SERVICE_IMPL", "/templates/mainSubExtends/serviceImpl.java"),

    /**
     * MAPPER_FILE
     */
    MAPPER_FILE("MAPPER", "/templates/mainSubExtends/mapper.java"),

    /**
     * CONTROLLER_FILE
     */
    CONTROLLER_FILE("CONTROLLER", "/templates/mainSubExtends/controller.java"),

    /**
     * MAPPER_XML_FILE
     */
    MAPPER_XML_FILE("MAPPER_XML", "/templates/mainSubExtends/mapper.xml"),

    /**
     * DTO_FILE
     */
    DTO_FILE("DTO", "/templates/mainSubExtends/dto.java.ftl"),

    /**
     * CREATE_DTO
     */
    CREATE_DTO("CREATE_DTO", "/templates/mainSubExtends/createDTO.java.ftl"),

    /**
     * UPDATE_DTO
     */
    UPDATE_DTO("UPDATE_DTO", "/templates/mainSubExtends/updateDTO.java.ftl"),

    /**
     * QUERY_DTO
     */
    QUERY_DTO("QUERY_DTO", "/templates/mainSubExtends/queryDTO.java.ftl"),

    /**
     * QUERY
     */
    QUERY("QUERY", "/templates/mainSubExtends/query.java.ftl"),

    /**
     * EXPORT
     */
    EXPORT("EXPORT", "/templates/mainSubExtends/entityExport.java.ftl"),

    /**
     * IMPORT
     */
    IMPORT("IMPORT", "/templates/mainSubExtends/entityImport.java.ftl"),

    /**
     * SQL
     */
    SQL("SQL", "/templates/mainSubExtends/resource.sql.ftl"),

    /**
     * VUE
     */
    VUE("VUE", "/templates/mainSubExtends/pageListTable.vue.ftl"),

    /**
     * JS
     */
    JS("JS", "/templates/mainSubExtends/pageListTable.js.ftl");

    public String name;

    public String path;

    CustomFileMainSubExtendsEnum(String name, String path) {
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
