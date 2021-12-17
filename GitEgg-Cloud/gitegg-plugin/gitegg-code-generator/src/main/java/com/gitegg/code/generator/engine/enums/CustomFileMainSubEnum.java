package com.gitegg.code.generator.engine.enums;


/**
 * @ClassName: CustomFileEnum
 * @Description: 自定义文件列表
 * @author GitEgg
 * @date 2020年09月19日 下午11:49:45
 */
public enum CustomFileMainSubEnum {

    /**
     * ENTITY_FILE
     */
    ENTITY_FILE("ENTITY", "/templates/mainSub/entity.java"),

    /**
     * SERVICE_FILE
     */
    SERVICE_FILE("SERVICE", "/templates/mainSub/service.java"),

    /**
     * SERVICE_IMPL_FILE
     */
    SERVICE_IMPL_FILE("SERVICE_IMPL", "/templates/mainSub/serviceImpl.java"),

    /**
     * MAPPER_FILE
     */
    MAPPER_FILE("MAPPER", "/templates/mainSub/mapper.java"),

    /**
     * CONTROLLER_FILE
     */
    CONTROLLER_FILE("CONTROLLER", "/templates/mainSub/controller.java"),

    /**
     * MAPPER_XML_FILE
     */
    MAPPER_XML_FILE("MAPPER_XML", "/templates/mainSub/mapper.xml"),

    /**
     * SERVICE_MAIN_FILE
     */
    SERVICE_MAIN_FILE("SERVICE_MAIN", "/templates/mainSub/serviceMain.java"),

    /**
     * SERVICE_IMPL_MAIN_FILE
     */
    SERVICE_IMPL_MAIN_FILE("SERVICE_IMPL_MAIN", "/templates/mainSub/serviceImplMain.java"),

    /**
     * CONTROLLER_MAIN_FILE
     */
    CONTROLLER_MAIN_FILE("CONTROLLER_MAIN", "/templates/mainSub/controllerMain.java"),

    /**
     * MAPPER_MAIN_XML_FILE
     */
    MAPPER_MAIN_XML_FILE("MAPPER_MAIN_XML", "/templates/mainSub/mapperMain.xml"),

    /**
     * DTO_FILE
     */
    DTO_FILE("DTO", "/templates/mainSub/dto.java.ftl"),

    /**
     * CREATE_DTO
     */
    CREATE_DTO("CREATE_DTO", "/templates/mainSub/createDTO.java.ftl"),

    /**
     * UPDATE_DTO
     */
    UPDATE_DTO("UPDATE_DTO", "/templates/mainSub/updateDTO.java.ftl"),

    /**
     * QUERY_DTO
     */
    QUERY_DTO("QUERY_DTO", "/templates/mainSub/queryDTO.java.ftl"),

    /**
     * QUERY
     */
    QUERY("QUERY", "/templates/mainSub/query.java.ftl"),

    /**
     * EXPORT
     */
    EXPORT("EXPORT", "/templates/mainSub/entityExport.java.ftl"),

    /**
     * IMPORT
     */
    IMPORT("IMPORT", "/templates/mainSub/entityImport.java.ftl"),

    /**
     * SQL
     */
    SQL("SQL", "/templates/mainSub/resource.sql.ftl"),

    /**
     * VUE
     */
    VUE("VUE", "/templates/mainSub/pageListTable.vue.ftl"),

    /**
     * JS
     */
    JS("JS", "/templates/mainSub/pageListTable.js.ftl");

    public String name;

    public String path;

    CustomFileMainSubEnum(String name, String path) {
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
