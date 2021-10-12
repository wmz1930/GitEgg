package com.gitegg.platform.code.generator.engine;

import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.io.File;
import java.util.Map;

/**
 * Freemarker 自定义输出自定义模板文件
 *
 * @author GitEgg
 * @since 2021-10-12
 */
public class GitEggFreemarkerTemplateEngine extends FreemarkerTemplateEngine {

    /**
     * 自定义输出自定义模板文件
     *
     * @param customFile 自定义配置模板文件信息
     * @param tableInfo  表信息
     * @param objectMap  渲染数据
     * @since 3.5.1
     */
    @Override
    protected void outputCustomFile( Map<String, String> customFile, TableInfo tableInfo, Map<String, Object> objectMap) {
        Map<String, String> customFilePath = (Map<String, String>)objectMap.get("customFilePathMap");
        customFile.forEach((key, value) -> {
            String otherPath = customFilePath.get(key);
            String fileName = String.format((otherPath + File.separator + "%s"), key);
            outputFile(new File(fileName), objectMap, value);
        });
    }
}
