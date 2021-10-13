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
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.fill.Column;
import com.gitegg.code.generator.config.dto.QueryConfigDTO;
import com.gitegg.code.generator.config.entity.Config;
import com.gitegg.code.generator.config.service.IConfigService;
import com.gitegg.code.generator.datasource.entity.Datasource;
import com.gitegg.code.generator.datasource.service.IDatasourceService;
import com.gitegg.code.generator.engine.GitEggDatabaseQuery;
import com.gitegg.code.generator.engine.constant.CodeGeneratorConstant;
import com.gitegg.code.generator.engine.dto.TableDTO;
import com.gitegg.code.generator.engine.enums.CustomFileEnum;
import com.gitegg.code.generator.engine.service.IEngineService;
import com.gitegg.code.generator.field.dto.FieldDTO;
import com.gitegg.code.generator.field.dto.QueryFieldDTO;
import com.gitegg.code.generator.field.service.IFieldService;
import com.gitegg.code.generator.join.entity.TableJoin;
import com.gitegg.code.generator.join.service.ITableJoinService;
import com.gitegg.platform.base.enums.BaseEntityEnum;
import com.gitegg.platform.code.generator.constant.GitEggCodeGeneratorConstant;
import com.gitegg.platform.code.generator.engine.GitEggFreemarkerTemplateEngine;
import com.gitegg.platform.mybatis.entity.BaseEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    /**
     * 解决循环依赖问题
     */
    private IFieldService fieldService;

    @Autowired
    public void setFieldService(@Lazy IFieldService fieldService) {
        this.fieldService = fieldService;
    }

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
                .enableChainModel()
                .enableLombok()
                .enableRemoveIsPrefix()
                .enableTableFieldAnnotation()
                .enableActiveRecord()
                .logicDeleteColumnName(BaseEntityEnum.DEL_FLAG.field)
                .logicDeletePropertyName(BaseEntityEnum.DEL_FLAG.entity)
                .naming(NamingStrategy.underline_to_camel)
                .columnNaming(NamingStrategy.underline_to_camel)
                .addTableFills(new Column(BaseEntityEnum.CREATE_TIME.field, FieldFill.INSERT))
                .addTableFills(new Column(BaseEntityEnum.UPDATE_TIME.field, FieldFill.INSERT_UPDATE))
                .idType(IdType.AUTO)
                .build();

        ConfigBuilder configBuilder = new ConfigBuilder(null, dataSourceConfig, strategyConfig, null, null, null);
        List<TableInfo> tableInfoList = configBuilder.getTableInfoList();
        return tableInfoList;
    }

    @Override
    public List<TableInfo> queryConfigFields(QueryConfigDTO queryConfigDTO) {
        List<String> tableNames = new ArrayList<>();
        String tableName = queryConfigDTO.getTableName();
        tableNames.add(tableName);

        Long id = queryConfigDTO.getId();

        // 查询是否有联表
        if (CodeGeneratorConstant.TABLE_DATA_TYPE_MULTI.equals(queryConfigDTO.getTableType()))
        {
            QueryWrapper<TableJoin> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq(CodeGeneratorConstant.GENERATION_ID, id);
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

    @Override
    public boolean processGenerateCode(QueryConfigDTO queryConfigDTO){

        Config config = configService.getById(queryConfigDTO.getId());

        QueryFieldDTO queryFieldDTO = new QueryFieldDTO();
        queryFieldDTO.setGenerationId(queryConfigDTO.getId());
        List<FieldDTO> fieldDTOS = fieldService.queryFieldList(queryFieldDTO);


        Map<String, Object> customMap = new HashMap<>();
        customMap.put(GitEggCodeGeneratorConstant.CONFIG, config);
        customMap.put(GitEggCodeGeneratorConstant.FIELDS, fieldDTOS);

        //查询数据源配置
        Datasource datasource = datasourceService.getById(config.getDatasourceId());


        String serviceName = config.getServiceName();
        //前端代码路径
        String frontCodePath = config.getFrontCodePath();
        //后端代码路径
        String serviceCodePath = config.getServiceCodePath();
        //自定义路径
        String parent = config.getParentPackage();
        String moduleName = config.getModuleCode();
        String codeDirPath =  (parent + StrUtil.DOT + moduleName).replace(StrUtil.DOT, File.separator) + File.separator;

        FastAutoGenerator.create(datasource.getUrl(), datasource.getUsername(), datasource.getPassword())
                .globalConfig(builder -> {
                    //全局配置
                    String author = GitEggCodeGeneratorConstant.AUTHOR;
                    builder.author(author) // 设置作者
                            .enableSwagger() // 开启 swagger 模式
                            .fileOverride() // 覆盖已生成文件
                            .disableOpenDir()
                            .outputDir(serviceCodePath + GitEggCodeGeneratorConstant.JAVA_PATH); // 指定输出目录
                })
                .packageConfig(builder -> {
                    //包配置
                    Map<OutputFile, String> pathInfoMap = new HashMap<>();
                    pathInfoMap.put(OutputFile.mapperXml, serviceCodePath + GitEggCodeGeneratorConstant.RESOURCES_PATH + codeDirPath + CodeGeneratorConstant.MAPPER);
                    builder.parent(parent) // 设置父包名
                            .moduleName(moduleName) // 设置父包模块名
                            .pathInfo(pathInfoMap); // 自定义生成路径
                })
                .injectionConfig(builder -> {

                    String dtoName = StrUtil.upperFirst(config.getModuleCode());

                    //dto
                    String dtoFile = dtoName + CodeGeneratorConstant.DTO_JAVA;
                    String createDtoFile = CodeGeneratorConstant.CREATE + dtoFile;
                    String updateDtoFile = CodeGeneratorConstant.UPDATE + dtoFile;
                    String queryDtoFile = CodeGeneratorConstant.QUERY + dtoFile;
                    //Export and Import
                    String exportFile = dtoName + CodeGeneratorConstant.EXPORT_JAVA;
                    String importFile = dtoName + CodeGeneratorConstant.IMPORT_JAVA;
                    // SQL
                    String sqlFile = dtoName + CodeGeneratorConstant.RESOURCE_SQL;
                    // vue and js
                    String vueFile = config.getModuleCode() + CodeGeneratorConstant.TABLE_VUE;
                    String jsFile = config.getModuleCode() + CodeGeneratorConstant.JS;

                    //因为目前版本框架只支持自定义输出到other目录，所以这里利用重写AbstractTemplateEngine的outputCustomFile方法支持所有自定义文件输出目录
                    Map<String, String> customFilePath = new HashMap<>();
                    //dto
                    String dtoPath = serviceCodePath + GitEggCodeGeneratorConstant.JAVA_PATH + codeDirPath + CodeGeneratorConstant.DTO;
                    customFilePath.put(dtoFile, dtoPath);
                    customFilePath.put(createDtoFile, dtoPath);
                    customFilePath.put(updateDtoFile, dtoPath);
                    customFilePath.put(queryDtoFile, dtoPath);
                    // Export and Import
                    String entityPath = serviceCodePath + GitEggCodeGeneratorConstant.JAVA_PATH + codeDirPath + CodeGeneratorConstant.ENTITY;
                    customFilePath.put(exportFile, entityPath);
                    customFilePath.put(importFile, entityPath);
                    // SQL
                    String sqlPath = serviceCodePath + GitEggCodeGeneratorConstant.RESOURCES_PATH + codeDirPath + CodeGeneratorConstant.MAPPER;
                    customFilePath.put(sqlFile, sqlPath);
                    // VUE AND JS
                    int start = serviceName.indexOf(StrUtil.DASHED);
                    int end = serviceName.length();
                    String servicePath = serviceName.substring(start, end).replace(StrUtil.DASHED, File.separator);
                    String vuePath = frontCodePath + GitEggCodeGeneratorConstant.VUE_PATH + servicePath + File.separator + config.getModuleCode();
                    String jsPath = frontCodePath + GitEggCodeGeneratorConstant.JS_PATH + servicePath + File.separator + config.getModuleCode();
                    customFilePath.put(vueFile, vuePath);
                    customFilePath.put(jsFile, jsPath);

                    customMap.put(GitEggCodeGeneratorConstant.CUSTOM_FILE_PATH_MAP, customFilePath);

                    // 设置自定义输出文件
                    Map<String, String> customFileMap = new HashMap<>();
                    customFileMap.put(dtoFile, CustomFileEnum.DTO_FILE.path);
                    customFileMap.put(createDtoFile, CustomFileEnum.CREATE_DTO.path);
                    customFileMap.put(updateDtoFile, CustomFileEnum.UPDATE_DTO.path);
                    customFileMap.put(queryDtoFile, CustomFileEnum.QUERY_DTO.path);
                    // Export and Import
                    customFileMap.put(exportFile, CustomFileEnum.EXPORT.path);
                    customFileMap.put(importFile, CustomFileEnum.IMPORT.path);
                    // SQL
                    customFileMap.put(sqlFile, CustomFileEnum.SQL.path);
                    // VUE AND JS
                    // TODO 要支持树形表、左树右表、左表右表、左表右树、左树右树形表、左树右树
                    customFileMap.put(vueFile, CustomFileEnum.VUE.path);
                    customFileMap.put(jsFile, CustomFileEnum.JS.path);

                    builder.customMap(customMap)
                            .customFile(customFileMap);
                })
                .strategyConfig(builder -> {
                    builder
                            .addInclude(config.getTableName())
                            .addTablePrefix(config.getTablePrefix())
                            .entityBuilder()
                            .enableLombok()
                            .superClass(BaseEntity.class)
                            .addSuperEntityColumns(BaseEntityEnum.TENANT_ID.field, BaseEntityEnum.CREATE_TIME.field,
                                    BaseEntityEnum.CREATOR.field, BaseEntityEnum.UPDATE_TIME.field, BaseEntityEnum.OPERATOR.field, BaseEntityEnum.DEL_FLAG.field)
                            .naming(NamingStrategy.underline_to_camel)
                            .addTableFills(new Column(BaseEntityEnum.CREATE_TIME.field, FieldFill.INSERT))	//基于数据库字段填充
                            .addTableFills(new Column(BaseEntityEnum.UPDATE_TIME.field, FieldFill.INSERT_UPDATE))	//基于模型属性填充
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
                .templateEngine(new GitEggFreemarkerTemplateEngine())
                .execute();
        return true;
    }
}
