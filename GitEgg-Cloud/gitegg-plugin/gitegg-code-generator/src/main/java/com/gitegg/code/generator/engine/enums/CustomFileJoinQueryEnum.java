package com.gitegg.code.generator.engine.enums;


/**
 * @ClassName: CustomFileJoinQueryEnum
 * @Description: 自定义文件列表
 * @author GitEgg
 * @date 2020年09月19日 下午11:49:45
 */
public enum CustomFileJoinQueryEnum {

    /**
     * MAPPER_XML_FILE
     */
    MAPPER_XML_FILE("MAPPER_XML", "/templates/joinQuery/mapper.xml"),

    /**
     * CREATE_DTO
     */
    CREATE_DTO("CREATE_DTO", "/templates/joinQuery/createDTO.java.ftl"),

    /**
     * UPDATE_DTO
     */
    UPDATE_DTO("UPDATE_DTO", "/templates/joinQuery/updateDTO.java.ftl");

    public String name;

    public String path;

    CustomFileJoinQueryEnum(String name, String path) {
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
