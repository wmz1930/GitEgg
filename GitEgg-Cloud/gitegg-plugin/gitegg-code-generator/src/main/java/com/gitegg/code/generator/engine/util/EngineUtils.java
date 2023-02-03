package com.gitegg.code.generator.engine.util;

/**
 * 代码生成通用工具类
 * @author GitEgg
 */
public class EngineUtils {
    
    /**
     * 根据版本获取服务代码模板路径
     *
     * @param tableType
     * @return
     */
    public static String getServerFtlPath(String tableType) {
        return "/templates/" + tableType + "/server";
    }
    
    /**
     * 根据版本获取前端代码模板路径
     *
     * @param tableType
     * @return
     */
    public static String getFrontFtlPath(String tableType, String frontType) {
        return "/templates/" + tableType + "/front/" + frontType;
    }
    
    /**
     * 根据版本获取sql模板路径
     *
     * @param tableType
     * @return
     */
    public static String getSqlFtlPath(String tableType) {
        return "/templates/" + tableType + "/sql/";
    }
    
    
    /**
     * 根据版本获取代码模板路径
     *
     * @param tableType
     * @param codeType
     * @param ftlName
     * @return
     */
    public static String getCodePath(String tableType, String codeType, String ftlName) {
        return "/templates/" + tableType + "/" + codeType + ftlName;
    }
}
