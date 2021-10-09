package com.gitegg.code.generator.engine.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.fill.Column;
import com.baomidou.mybatisplus.generator.fill.Property;
import com.gitegg.code.generator.config.dto.QueryConfigDTO;
import com.gitegg.code.generator.config.entity.Config;
import com.gitegg.code.generator.config.service.IConfigService;
import com.gitegg.code.generator.datasource.entity.Datasource;
import com.gitegg.code.generator.datasource.service.IDatasourceService;
import com.gitegg.code.generator.engine.GitEggDatabaseQuery;
import com.gitegg.code.generator.engine.dto.TableDTO;
import com.gitegg.code.generator.engine.service.IEngineService;
import com.gitegg.code.generator.field.dto.FieldDTO;
import com.gitegg.code.generator.field.dto.QueryFieldDTO;
import com.gitegg.code.generator.field.dto.TableFieldDTO;
import com.gitegg.code.generator.field.service.IFieldService;
import com.gitegg.code.generator.join.entity.TableJoin;
import com.gitegg.code.generator.join.service.ITableJoinService;
import com.gitegg.platform.mybatis.entity.BaseEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 代码生成器接口类
 *
 * @author wanglei
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class EngineServiceImpl implements IEngineService {

    private final IConfigService configService;

    private final IDatasourceService datasourceService;

    private final ITableJoinService tableJoinService;

    private @Lazy IFieldService fieldService;

    @Override
    public List<TableDTO> queryTableList(QueryConfigDTO queryConfigDTO) {
        Datasource datasource = datasourceService.getById(queryConfigDTO.getDatasourceId());
        DataSourceConfig dataSourceConfig = new DataSourceConfig.Builder(datasource.getUrl(), datasource.getUsername(), datasource.getPassword()).build();
        ConfigBuilder configBuilder = new ConfigBuilder(null, dataSourceConfig, null, null, null, null);
        List<TableDTO> tableInfos = (new GitEggDatabaseQuery(configBuilder)).queryDatasourceTables();
        return tableInfos;
    }

    @Override
    public List<TableInfo> queryTableFields(String datasourceId, List<String> tableNames) {
        Datasource datasource = datasourceService.getById(datasourceId);
        DataSourceConfig dataSourceConfig = new DataSourceConfig.Builder(datasource.getUrl(), datasource.getUsername(), datasource.getPassword()).build();

        //设置有哪些表
        StrategyConfig strategyConfig = new StrategyConfig.Builder()
                .addInclude(tableNames.toArray(new String[]{}))
                .entityBuilder()
//                .superClass(BaseEntity.class)
                .enableChainModel()
                .enableLombok()
                .enableRemoveIsPrefix()
                .enableTableFieldAnnotation()
                .enableActiveRecord()
                .logicDeleteColumnName("del_flag")
                .logicDeletePropertyName("delFlag")
                .naming(NamingStrategy.underline_to_camel)
                .columnNaming(NamingStrategy.underline_to_camel)
//                .addSuperEntityColumns("tenant_id", "create_time", "creator", "update_time", "operator", "del_flag")
                .addTableFills(new Column("create_time", FieldFill.INSERT))
                .addTableFills(new Property("updateTime", FieldFill.INSERT_UPDATE))
                .idType(IdType.AUTO)
                .formatFileName("%sEntity")
                .build();

        ConfigBuilder configBuilder = new ConfigBuilder(null, dataSourceConfig, strategyConfig, null, null, null);
        List<TableInfo> tableInfoList = configBuilder.getTableInfoList();
        tableInfoList.forEach(tableInfo -> {
            System.out.println(tableInfo.getComment());
        });
        return tableInfoList;
    }

    @Override
    public List<TableInfo> queryConfigFields(QueryConfigDTO queryConfigDTO) {
        List<String> tableNames = new ArrayList<>();
        String tableName = queryConfigDTO.getTableName();
        tableNames.add(tableName);

        Long id = queryConfigDTO.getId();

        // 查询是否有联表
        if ("multi".equals(queryConfigDTO.getTableType()))
        {
            QueryWrapper<TableJoin> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("generation_id", id);
            List<TableJoin> tableJoinList = tableJoinService.list(queryWrapper);
            if(!CollectionUtils.isEmpty(tableJoinList))
            {
                tableJoinList.stream().forEach(tableJoin->{
                    tableNames.add(tableJoin.getJoinTableName());
                });
            }
        }

        Datasource datasource = datasourceService.getById(queryConfigDTO.getDatasourceId());
        DataSourceConfig dataSourceConfig = new DataSourceConfig.Builder(datasource.getUrl(), datasource.getUsername(), datasource.getPassword()).build();

        //设置有哪些表
        StrategyConfig strategyConfig = new StrategyConfig.Builder().addInclude(tableNames.toArray(new String[]{})).build();
        ConfigBuilder configBuilder = new ConfigBuilder(null, dataSourceConfig, strategyConfig, null, null, null);
        List<TableInfo> tableInfoList = configBuilder.getTableInfoList();
        tableInfoList.forEach(tableInfo -> {
            System.out.println(tableInfo.getComment());
        });
        return tableInfoList;
    }

    public boolean generatorCode(QueryConfigDTO queryConfigDTO){

        Config config = configService.getById(queryConfigDTO.getId());

        QueryFieldDTO queryFieldDTO = new QueryFieldDTO();
        queryFieldDTO.setGenerationId(queryConfigDTO.getId());
        List<FieldDTO> fieldDTOS = fieldService.queryFieldList(queryFieldDTO);

        //前端代码路径
        String frontCodePath = config.getFrontCodePath();
        //后端代码路径
        String serviceCodePath = config.getServiceCodePath();

        //基础配置
        String url = "jdbc:mysql://172.16.20.188/gitegg_cloud?zeroDateTimeBehavior=convertToNull&useUnicode=true&characterEncoding=utf8&all owMultiQueries=true&serverTimezone=Asia/Shanghai";
        String username = "myHisc";
        String password = "root4Hisc";

        //可选配置
        String driver = "";
        String dbType = "";
        String typeConvert = "";
        String keyWordsHandler = "";
        String dbQuery = "";
        String schema = "";



        FastAutoGenerator.create(url, username, password)
                .globalConfig(builder -> {
                    //全局配置
                    String author = "GitEgg";
                    builder.author(author) // 设置作者
                            .enableSwagger() // 开启 swagger 模式
                            .fileOverride() // 覆盖已生成文件
                            .outputDir(serviceCodePath); // 指定输出目录
                })
                .packageConfig(builder -> {
                    //包配置
                    String parent = config.getParentPackage();
                    String moduleName = config.getModuleName();
                    String codeDirPath =  (parent + "." + moduleName).replace(".", "/");

                    builder.parent(parent) // 设置父包名
                    .moduleName(moduleName) // 设置父包模块名
                    .other("dto")
                    .pathInfo(Collections.singletonMap(OutputFile.mapperXml, codeDirPath + "/mapper"))
                    .pathInfo(Collections.singletonMap(OutputFile.other, codeDirPath + "/dto")); // 设置mapperXml生成路径
                })
                .templateConfig(builder -> {
                    builder.entity("/templates/entity.java")
                            .service("/templates/service.java")
                            .serviceImpl("/templates/serviceImpl.java")
                            .mapper("/templates/mapper.java")
                            .mapperXml("/templates/mapper.xml")
                            .controller("/templates/controller.java");
                })
                .injectionConfig(builder -> {
                    String dtoName = StrUtil.upperFirst(config.getModuleCode());
                    builder.beforeOutputFile((tableInfo, objectMap) -> {
                        System.out.println("tableInfo: " + tableInfo.getEntityName() + " objectMap: " + objectMap.size());
                    })
                    .customMap(Collections.singletonMap("fields", fieldDTOS))
                    // DTO
                    .customFile(Collections.singletonMap(dtoName + "DTO.java", "/templates/dto.java.ftl"))
                    .customFile(Collections.singletonMap("Create" + dtoName + "DTO.java", "/templates/createDTO.java.ftl"))
                    .customFile(Collections.singletonMap("Update" + dtoName + "DTO.java", "/templates/updateDTO.java.ftl"))
                    .customFile(Collections.singletonMap("Query" + dtoName + "DTO.java", "/templates/queryDTO.java.ftl"))
                    // Export and Import
                    .customFile(Collections.singletonMap(dtoName + "Export.java", "/templates/entityExport.java.ftl"))
                    .customFile(Collections.singletonMap(dtoName + "Import.java", "/templates/entityImport.java.ftl"))
                     // SQL
                    .customFile(Collections.singletonMap(dtoName + "Resource.sql", "/templates/resource.sql.ftl"))
                    // 前端代码
                    // TODO 要支持树形表、左树右表、左表右表、左表右树、左树右树形表、左树右树
                    .customFile(Collections.singletonMap(config.getModuleCode() + "Table.vue", "/templates/pageListTable.vue.ftl"))
                    .customFile(Collections.singletonMap(config.getModuleCode() + ".js", "/templates/pageListTable.js.ftl"));
                })
                .strategyConfig(builder -> {
                    builder
                            .addInclude(config.getTableName())
                            .addTablePrefix(config.getTablePrefix())
                            .entityBuilder()
                            .enableLombok()
                            .superClass(BaseEntity.class)
                            .addSuperEntityColumns("tenant_id", "create_time", "creator", "update_time", "operator", "del_flag")
                            .naming(NamingStrategy.underline_to_camel)
                            .addTableFills(new Column("create_time", FieldFill.INSERT))	//基于数据库字段填充
                            .addTableFills(new Property("updateTime", FieldFill.INSERT_UPDATE))	//基于模型属性填充
                            .controllerBuilder()
                            .enableRestStyle()
                            .enableHyphenStyle()
                            .mapperBuilder()
//                            .enableMapperAnnotation()
                            .enableBaseResultMap()
                            .enableBaseColumnList()
                    ;
                })
                // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();
        return true;
    }
}
