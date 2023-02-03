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
import com.gitegg.code.generator.api.dto.GeneratorApiDTO;
import com.gitegg.code.generator.api.entity.GeneratorApi;
import com.gitegg.code.generator.api.service.IGeneratorApiService;
import com.gitegg.code.generator.config.dto.QueryConfigDTO;
import com.gitegg.code.generator.config.entity.Config;
import com.gitegg.code.generator.config.service.IConfigService;
import com.gitegg.code.generator.datasource.entity.Datasource;
import com.gitegg.code.generator.datasource.service.IDatasourceService;
import com.gitegg.code.generator.engine.GitEggDatabaseQuery;
import com.gitegg.code.generator.engine.constant.CodeGeneratorConstant;
import com.gitegg.code.generator.engine.dto.TableDTO;
import com.gitegg.code.generator.engine.enums.*;
import com.gitegg.code.generator.engine.service.IEngineService;
import com.gitegg.code.generator.engine.util.EngineUtils;
import com.gitegg.code.generator.field.dto.FieldDTO;
import com.gitegg.code.generator.field.dto.QueryFieldDTO;
import com.gitegg.code.generator.field.service.IFieldService;
import com.gitegg.code.generator.field.util.FieldUtils;
import com.gitegg.code.generator.join.entity.TableJoin;
import com.gitegg.code.generator.join.service.ITableJoinService;
import com.gitegg.platform.base.constant.GitEggConstant;
import com.gitegg.platform.base.enums.BaseEntityEnum;
import com.gitegg.platform.base.result.Result;
import com.gitegg.platform.base.util.BeanCopierUtils;
import com.gitegg.platform.code.generator.constant.GitEggCodeGeneratorConstant;
import com.gitegg.platform.code.generator.engine.GitEggFreemarkerTemplateEngine;
import com.gitegg.platform.mybatis.entity.BaseEntity;
import com.gitegg.service.system.client.feign.IResourceFeign;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 代码生成器接口类
 *
 * @author GitEgg
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class EngineServiceImpl implements IEngineService {

    private final IConfigService configService;

    private final IDatasourceService datasourceService;

    private final ITableJoinService tableJoinService;

    private final IResourceFeign resourceFeign;
    
    private final IGeneratorApiService generatorApiService;
    
    @Value("${system.defaultDir}")
    private String defaultDir;

    /**
     * 解决循环依赖问题
     */
    private IFieldService fieldService;

    @Autowired
    public void setFieldService(@Lazy IFieldService fieldService) {
        this.fieldService = fieldService;
    }
    
    /**
     * 查询某个数据源的所有表
     * @param queryConfigDTO
     * @return
     */
    @Override
    public List<TableDTO> queryTableList(QueryConfigDTO queryConfigDTO) {
        Datasource datasource = datasourceService.getById(queryConfigDTO.getDatasourceId());
        DataSourceConfig dataSourceConfig = new DataSourceConfig.Builder(datasource.getUrl(), datasource.getUsername(), datasource.getPassword()).build();
        ConfigBuilder configBuilder = new ConfigBuilder(null, dataSourceConfig, null, null, null, null);
        List<TableDTO> tableInfos = (new GitEggDatabaseQuery(configBuilder)).queryDatasourceTables();
        return tableInfos;
    }
    
    /**
     * 查询某个数据源表的字段信息
     * @param datasourceId
     * @param tableNames
     * @return
     */
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
    
    /**
     * 查询某个代码生成配置里面所有的字段
     * @param queryConfigDTO
     * @return
     */
    @Override
    public List<TableInfo> queryConfigFields(QueryConfigDTO queryConfigDTO) {
        List<String> tableNames = new ArrayList<>();
        String tableName = queryConfigDTO.getTableName();
        tableNames.add(tableName);

        Long id = queryConfigDTO.getId();

        // 查询是否有联表
        if (CodeGeneratorConstant.TABLE_DATA_TYPE_JOIN_QUERY.equals(queryConfigDTO.getTableType())
            || CodeGeneratorConstant.TABLE_DATA_TYPE_MAIN_SUB.equals(queryConfigDTO.getTableType()))
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
        return tableInfoList;
    }
    
    /**
     * 执行代码生成
     * @param queryConfigDTO
     * @return
     */
    @Override
    public boolean processGenerateCode(QueryConfigDTO queryConfigDTO){

        Config config = configService.getById(queryConfigDTO.getId());

        // 单表或者多表查询
        if (config.getTableType().equals(CodeGeneratorConstant.TABLE_DATA_TYPE_SINGLE) || config.getTableType().equals(CodeGeneratorConstant.TABLE_DATA_TYPE_JOIN_QUERY))
        {
            this.processGenerateSingleCode(queryConfigDTO, config);
        } else if(config.getTableType().equals(CodeGeneratorConstant.TABLE_DATA_TYPE_MAIN_SUB))
        {
            // 主表子表
            this.processGenerateMainSubCode(queryConfigDTO, config);
        }
        return true;
    }
    
    /**
     * 单表或者多表查询
     * @param queryConfigDTO
     * @param config
     * @return
     */
    private boolean processGenerateSingleCode(QueryConfigDTO queryConfigDTO, Config config){
    
        // 自定义生成参数
        Map<String, Object> customMap = new HashMap<>();
        customMap.put(GitEggCodeGeneratorConstant.CONFIG, config);
        
        QueryFieldDTO queryFieldDTO = new QueryFieldDTO();
        queryFieldDTO.setGenerationId(queryConfigDTO.getId());
        List<FieldDTO> fieldDTOS = fieldService.queryFieldList(queryFieldDTO);
        customMap.put(GitEggCodeGeneratorConstant.FIELDS, fieldDTOS);
        
        // 如果是联合查询，这里需要处理联合查询的数据
        if (config.getTableType().equals(CodeGeneratorConstant.TABLE_DATA_TYPE_JOIN_QUERY))
        {
            //获取join表配置
            this.generateJoinFields(queryConfigDTO, customMap, fieldDTOS, config);
        }
        
        //提取表单的字段
        List<FieldDTO> formFieldDTOS = fieldDTOS.stream().filter(f->f.getFormAdd() || f.getFormEdit()).collect(Collectors.toList());
        customMap.put(GitEggCodeGeneratorConstant.FORM_FIELDS, formFieldDTOS);
        
        //提取有数据字典或API接口的字段，用于界面遍历
        this.generateDictAndApi(fieldDTOS,  customMap);

        // 状态处理
        this.generateStatus( fieldDTOS,  customMap, config);

        //baseEntity里面有的，DTO中需要排除的字段
        List<String> baseEntityFieldList = BaseEntityEnum.getBaseEntityFieldList();
        customMap.put(GitEggCodeGeneratorConstant.BASE_ENTITY_FIELD_LIST, baseEntityFieldList);

        //查询数据源配置
        Datasource datasource = datasourceService.getById(config.getDatasourceId());
    
        //cloud中的微服务路由名称，boot中的controller服务前缀
        String serviceName = config.getServiceName();
    
        // 代码保存方式：服务器下载SERVER_DOWNLOAD、本地覆盖LOCAL_OVERLAY
        String codeSaveType = config.getCodeSaveType();
        //后端代码路径
        String serviceCodePath = defaultDir + File.separator + "java";
        //前端代码路径
        String frontCodePath = defaultDir + File.separator + "vue";
        if (CodeGeneratorConstant.SAVE_TYPE_LOCAL_OVERLAY.equals(codeSaveType))
        {
            serviceCodePath = config.getServiceCodePath();
            frontCodePath = config.getFrontCodePath();
        }
        
        //页面代码目录，前端代码默认放到路径下的views目录，这里如果添加了目录名，那么会添加到该目录下
        String frontCodeDir = config.getFrontCodeDir();
        
        //父级包名
        String parent = config.getParentPackage();
        //父级包下的模块名称
        String moduleName = config.getModuleCode();
        //最后生成的代码存放目录
        String codeDirPath =  (parent + StrUtil.DOT + moduleName).replace(StrUtil.DOT, File.separator) + File.separator;

        // 查询资源权限表最大的id，用于生成资源权限脚本
        Long maxId = GitEggConstant.ZERO_LONG;
        Result<Object> result = resourceFeign.queryResourceMaxId();
        if (null != result && result.isSuccess())
        {
            maxId = Long.parseLong(result.getData().toString()) + GitEggConstant.ONE_LONG;
        }
        Long finalMaxId = maxId;
    
        //Variable used in lambda expression should be final or effectively final
        String finalServiceCodePath = serviceCodePath;
        String finalFrontCodePath = frontCodePath;
        FastAutoGenerator.create(datasource.getUrl(), datasource.getUsername(), datasource.getPassword())
                .globalConfig(builder -> {
                    //全局配置
                    String author = GitEggCodeGeneratorConstant.AUTHOR;
                    builder.author(author) // 设置作者
                            .enableSwagger() // 开启 swagger 模式
                            .fileOverride() // 覆盖已生成文件
                            .disableOpenDir()
                            .outputDir(finalServiceCodePath + GitEggCodeGeneratorConstant.JAVA_PATH); // 指定输出目录
                })
                .packageConfig(builder -> {
                    //包配置
                    Map<OutputFile, String> pathInfoMap = new HashMap<>();
                    pathInfoMap.put(OutputFile.mapperXml, finalServiceCodePath + GitEggCodeGeneratorConstant.RESOURCES_PATH + codeDirPath + CodeGeneratorConstant.MAPPER);
                    builder.parent(parent) // 设置父包名
                            .moduleName(moduleName) // 设置父包模块名
                            .pathInfo(pathInfoMap); // 自定义生成路径
                })
                .injectionConfig(builder -> {
                    // 自定义代码生成目录处理
                    this.processInjectionConfig( builder, customMap, config, serviceName, frontCodeDir, finalServiceCodePath,
                            codeDirPath, finalFrontCodePath, finalMaxId);
                })
                .strategyConfig(builder -> {
                    builder
                            .addInclude(config.getTableName())
                            .addTablePrefix(config.getTablePrefix())
                            .entityBuilder()
                            .enableLombok()
                            .enableTableFieldAnnotation() // 实体字段注解
                            .superClass(BaseEntity.class)
                            .addSuperEntityColumns(BaseEntityEnum.TENANT_ID.field, BaseEntityEnum.CREATE_TIME.field,
                                    BaseEntityEnum.CREATOR.field, BaseEntityEnum.UPDATE_TIME.field, BaseEntityEnum.OPERATOR.field, BaseEntityEnum.DEL_FLAG.field)
                            .naming(NamingStrategy.underline_to_camel)
                            //基于数据库字段填充
                            .addTableFills(new Column(BaseEntityEnum.CREATE_TIME.field, FieldFill.INSERT))
                            //基于模型属性填充
                            .addTableFills(new Column(BaseEntityEnum.UPDATE_TIME.field, FieldFill.INSERT_UPDATE))
                            .controllerBuilder()
                            .enableRestStyle()
                            .enableHyphenStyle()
                            .mapperBuilder()
//                            .enableMapperAnnotation()
                            .enableBaseResultMap()
                            .enableBaseColumnList()
                    ;
                })
                .templateConfig(builder -> {
                    if (config.getCodeType().equals(CodeGeneratorConstant.CODE_FRONT)) {
                        builder.disable();
                    }
                    /**
                     * 使用mapperFtlBasePath的为差异模板，使用serverFtlBasePath为通用模板
                     * 即：相同模板时使用single，不同模板时，使用tableType动态获取
                     */
                    // 如果是联合查询，这里需要处理联合查询的数据
                    String mapperFtlBasePath = EngineUtils.getServerFtlPath(config.getTableType());
                    builder.mapperXml(mapperFtlBasePath + CustomFileEnum.MAPPER_XML_FILE.path);
                    
                    // 无论什么tableType，这里都是使用的相同的java
                    String serverFtlBasePath = EngineUtils.getServerFtlPath(TableTypeEnum.SINGLE.code);
                    // 自定义文件模板路径
                    builder.entity(serverFtlBasePath + CustomFileEnum.ENTITY_FILE.path)
                            .service(serverFtlBasePath + CustomFileEnum.SERVICE_FILE.path)
                            .serviceImpl(serverFtlBasePath + CustomFileEnum.SERVICE_IMPL_FILE.path)
                            .mapper(serverFtlBasePath + CustomFileEnum.MAPPER_FILE.path)
                            .controller(serverFtlBasePath + CustomFileEnum.CONTROLLER_FILE.path)
                            .build();
                })
                // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .templateEngine(new GitEggFreemarkerTemplateEngine())
                .execute();
        return true;
    }
    
    /**
     * 连接表字段处理，提取联表查询中的字段
     * @param queryConfigDTO
     * @param customMap
     * @param fieldDTOS
     * @param config
     */
    private void generateJoinFields(QueryConfigDTO queryConfigDTO, Map<String, Object> customMap,
              List<FieldDTO> fieldDTOS, Config config){
        //获取join表配置
        QueryWrapper<TableJoin> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(CodeGeneratorConstant.GENERATION_ID, queryConfigDTO.getId());
        List<TableJoin> tableJoinList = tableJoinService.list(queryWrapper);
        Map<String, String> joinTableAliasMap = tableJoinList.stream().collect(Collectors.toMap(TableJoin::getJoinTableName, TableJoin::getJoinTableAlias));
        Map<String, String> joinTableFieldsMap = tableJoinList.stream().collect(Collectors.toMap(TableJoin::getJoinTableName, TableJoin::getJoinTableSelect));
    
        //提取连接表的字段
        List<FieldDTO> joinFieldDTOS = fieldDTOS.stream().filter(f->f.getJoinId() == GitEggConstant.Number.ZERO).collect(Collectors.toList());
    
        List<FieldDTO> joinSelectFieldDTOS = new ArrayList<>();
        for (TableJoin tableJoin : tableJoinList)
        {
            String joinTableName = tableJoin.getJoinTableName();
            String tableSelect = joinTableFieldsMap.get(joinTableName);
            if (StringUtils.isEmpty(tableSelect))
            {
                List<String> fields = Arrays.asList(tableSelect.split(StrUtil.COMMA));
                List<FieldDTO> fieldDTOList = joinFieldDTOS.stream().filter(f->f.getJoinTableName().equals(joinTableName) && fields.contains(f.getFieldName())).collect(Collectors.toList());
                joinSelectFieldDTOS.addAll(fieldDTOList);
            }
        }
        customMap.put(GitEggCodeGeneratorConstant.FIELDS_JOIN, joinSelectFieldDTOS);
        customMap.put(GitEggCodeGeneratorConstant.TABLES_JOIN, tableJoinList);
    
        for (FieldDTO fieldDTO: fieldDTOS)
        {
            if (fieldDTO.getJoinId().longValue() == GitEggConstant.ZERO_LONG.longValue())
            {
                fieldDTO.setJoinTableAlias(config.getTableAlias());
            }
            else
            {
                fieldDTO.setJoinTableAlias(joinTableAliasMap.get(fieldDTO.getJoinTableName()));
            }
        }
    }
    
    /**
     * 数据字典和api接口处理
     * @param fieldDTOS
     * @param customMap
     */
    private void generateDictAndApi(List<FieldDTO> fieldDTOS, Map<String, Object> customMap)
    {
        //提取有数据字典或API接口的字段，用于界面遍历
        List<FieldDTO> dictCodeFieldDTOS = fieldDTOS.stream().filter(f-> FieldUtils.isDictCodeField(f.getControlType())).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(dictCodeFieldDTOS))
        {
            //取数据字典的字段
            List<FieldDTO> dictFieldDTOS = dictCodeFieldDTOS.stream().filter(f-> !StringUtils.isEmpty(f.getDictCode()) && !f.getDictCode().equals("API_DICT")).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(dictFieldDTOS))
            {
                customMap.put(GitEggCodeGeneratorConstant.DICT_CODE_FIELDS,new ArrayList<>());
                customMap.put(CodeGeneratorConstant.HAS_DICT, true);
                List<String> dictCodes = dictFieldDTOS.stream().map(FieldDTO::getDictCode).distinct().collect(Collectors.toList());
                customMap.put(CodeGeneratorConstant.DICT_CODE_LIST, dictCodes);
            }
        
            //取接口的字段
            List<FieldDTO> apiFieldDTOS = dictCodeFieldDTOS.stream().filter(f-> !StringUtils.isEmpty(f.getDictCode()) && f.getDictCode().equals("API_DICT")).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(apiFieldDTOS))
            {
                customMap.put(GitEggCodeGeneratorConstant.API_FIELDS,new ArrayList<>());
                customMap.put(CodeGeneratorConstant.HAS_API, true);
                List<Long> apiIds = apiFieldDTOS.stream().map(FieldDTO::getApiId).distinct().collect(Collectors.toList());
                // 这里需要批量查询出所有的接口，并赋值到配置中
                List<GeneratorApi> apiList = generatorApiService.listByIds(apiIds);
                Map<String, GeneratorApi> apiMap = apiList.stream().collect(HashMap::new, (map, generatorApi) -> map.put(generatorApi.getId().toString(), generatorApi), HashMap::putAll);
                customMap.put(CodeGeneratorConstant.API_LIST, apiList);
                customMap.put(CodeGeneratorConstant.API_MAP, apiMap);
            }
        }
    }
    
    /**
     * 状态字段处理
     * @param fieldDTOS
     * @param customMap
     * @param config
     */
    private void generateStatus(List<FieldDTO> fieldDTOS, Map<String, Object> customMap, Config config)
    {
        for (FieldDTO fieldDTO: fieldDTOS)
        {
            if (config.getStatusHandling() && fieldDTO.getFieldName().endsWith("status"))
            {
                customMap.put(CodeGeneratorConstant.HAS_STATUS, true);
                customMap.put(CodeGeneratorConstant.STATUS_NAME, fieldDTO.getEntityName());
            }
            if (fieldDTO.getFieldUnique())
            {
                customMap.put(CodeGeneratorConstant.CHECK_EXIST, true);
            }
            
            if ("PROVINCE_CITY_AREA".equals(fieldDTO.getControlType()))
            {
                customMap.put(CodeGeneratorConstant.HAS_PROVINCE, true);
            }
        }
    }

    /**
     * 代码生成目录处理
     *
     * 相同模板时使用single，不同模板时，使用tableType动态获取
     * dto、service、entity等java类都是通用的，只有mapper是动态获取的
     * @param builder
     * @param customMap
     * @param config
     * @param serviceName
     * @param frontCodeDir
     * @param finalServiceCodePath
     * @param codeDirPath
     * @param finalFrontCodePath
     * @param finalMaxId
     */
    private void processInjectionConfig(com.baomidou.mybatisplus.generator.config.InjectionConfig.Builder builder,Map<String, Object> customMap,
                                        Config config, String serviceName, String frontCodeDir, String finalServiceCodePath,
                                        String codeDirPath, String finalFrontCodePath, Long finalMaxId)
    {
        String tableName = config.getTableName();
        String tablePrefix = config.getTablePrefix();
        if (!StringUtils.isEmpty(tablePrefix))
        {
            tableName = tableName.replaceFirst(tablePrefix,StrUtil.EMPTY);
        }
    
        /**
         * START 设置代码源文件名称
         */
        String dtoName = StrUtil.upperFirst(StrUtil.toCamelCase(tableName));
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
        /**
         * END 设置代码源文件名称
         */
    
        // 设置自定义输出文件
        Map<String, String> customFileMap = new HashMap<>();
        
        // 通用的用single
        String serverFtlBasePath = EngineUtils.getServerFtlPath(TableTypeEnum.SINGLE.code);
    
        // 通用DTO
        customFileMap.put(dtoFile, serverFtlBasePath + CustomFileEnum.DTO_FILE.path);
        customFileMap.put(queryDtoFile, serverFtlBasePath + CustomFileEnum.QUERY_DTO.path);
    
        // 区分单表还是联表
        customFileMap.put(createDtoFile, serverFtlBasePath + CustomFileEnum.CREATE_DTO.path);
        customFileMap.put(updateDtoFile, serverFtlBasePath + CustomFileEnum.UPDATE_DTO.path);
    
        // 导入导出定义
        customFileMap.put(exportFile, serverFtlBasePath + CustomFileEnum.EXPORT.path);
        customFileMap.put(importFile, serverFtlBasePath + CustomFileEnum.IMPORT.path);
    
        // 生成权限菜单的数据库脚本
        String sqlFtlBasePath = EngineUtils.getSqlFtlPath(config.getTableType());
        customFileMap.put(sqlFile, sqlFtlBasePath + CustomFileEnum.SQL.path);
    
        //因为目前版本框架只支持自定义输出到other目录，所以这里利用重写AbstractTemplateEngine的outputCustomFile方法支持所有自定义文件输出目录
        Map<String, String> customFilePath = new HashMap<>();
    
        // 处理前端代码输出路径
        String servicePath = StrUtil.EMPTY;
        if (!StringUtils.isEmpty(serviceName))
        {
            int start = serviceName.indexOf(StrUtil.DASHED);
            if (start >= 0)
            {
                int end = serviceName.length();
                servicePath = serviceName.substring(start, end).replace(StrUtil.DASHED, File.separator);
            }
            else
            {
                servicePath = servicePath + File.separator + serviceName;
            }
        
            if (!StringUtils.isEmpty(frontCodeDir))
            {
                servicePath = frontCodeDir + servicePath ;
            }
        }
    
        //判断是否生成后端代码
        if (config.getCodeType().equals(CodeGeneratorConstant.CODE_ALL) || config.getCodeType().equals(CodeGeneratorConstant.CODE_SERVICE))
        {
            //dto
            String dtoPath = finalServiceCodePath + GitEggCodeGeneratorConstant.JAVA_PATH + codeDirPath + CodeGeneratorConstant.DTO;
            customFilePath.put(dtoFile, dtoPath);
            customFilePath.put(createDtoFile, dtoPath);
            customFilePath.put(updateDtoFile, dtoPath);
            customFilePath.put(queryDtoFile, dtoPath);
            // Export and Import
            String boPath = finalServiceCodePath + GitEggCodeGeneratorConstant.JAVA_PATH + codeDirPath + CodeGeneratorConstant.BO;
            customFilePath.put(exportFile, boPath);
            customFilePath.put(importFile, boPath);
            // SQL
            String sqlPath = finalServiceCodePath + GitEggCodeGeneratorConstant.RESOURCES_PATH + codeDirPath + CodeGeneratorConstant.MAPPER;
            customFilePath.put(sqlFile, sqlPath);
        
        }
        
        // 数据表格展示类型  table  tree_table table_table left_tree_table
        String tableShowType = config.getTableShowType();
        customMap.put(CodeGeneratorConstant.TABLE_SHOW_TYPE, tableShowType);
        
        //判断是否生成前端代码
        if (config.getCodeType().equals(CodeGeneratorConstant.CODE_ALL) || config.getCodeType().equals(CodeGeneratorConstant.CODE_FRONT))
        {
            String vueFileEntity = dtoName;
            // vue and js
            String vueFile = vueFileEntity + CodeGeneratorConstant.TABLE_VUE;
    
            String dataJsName = StrUtil.toSymbolCase(vueFileEntity, StrUtil.C_DOT);
            // VUE3使用ts，VUE2使用js
            String jsFile = dataJsName
                    + (CodeGeneratorConstant.VUE3.equals(config.getFrontType()) ? CodeGeneratorConstant.TS : CodeGeneratorConstant.JS);
        
            String vuePath = finalFrontCodePath + GitEggCodeGeneratorConstant.VUE_PATH + (StringUtils.isEmpty(servicePath)?StrUtil.EMPTY:(servicePath + File.separator)) + config.getModuleCode();
            String jsPath = finalFrontCodePath + GitEggCodeGeneratorConstant.JS_PATH + (StringUtils.isEmpty(servicePath)?StrUtil.EMPTY:(servicePath + File.separator)) + config.getModuleCode();
            customFilePath.put(vueFile, vuePath);
            customFilePath.put(jsFile, jsPath);
        
            customMap.put(GitEggCodeGeneratorConstant.VUE_TABLE_PATH, servicePath.replace(File.separator, StrUtil.SLASH) + StrUtil.SLASH + config.getModuleCode() + StrUtil.SLASH + vueFile);
            customMap.put(GitEggCodeGeneratorConstant.VUE_JS_PATH, (StringUtils.isEmpty(servicePath)?StrUtil.EMPTY:(servicePath.replace(File.separator, StrUtil.SLASH) + StrUtil.SLASH)) + config.getModuleCode() + StrUtil.SLASH + dataJsName);
        
            // 生成前端页面 VUE AND JS/TS
            String frontFtlBasePath = EngineUtils.getFrontFtlPath(config.getTableType(), config.getFrontType());
    
            // table_table页面，既要生成普通的table，也要生成table_table页面
            if (CodeGeneratorConstant.TABLE_TABLE.equals(tableShowType)) {
                String vueEditFile = vueFileEntity + CodeGeneratorConstant.EDIT + CodeGeneratorConstant.TABLE_VUE;
                // table_table都要用到table模板
                customFileMap.put(vueEditFile, frontFtlBasePath + "/" + CodeGeneratorConstant.TABLE + CustomFileEnum.VUE.path);
                customFilePath.put(vueEditFile, vuePath);
            }
            
            // tree_table/table都要用到table模板，所以这里需要做转换
            customFileMap.put(vueFile, frontFtlBasePath + "/" +
                    (CodeGeneratorConstant.TREE_TABLE.equals(tableShowType) ? CodeGeneratorConstant.TABLE : tableShowType) + CustomFileEnum.VUE.path);
            customFileMap.put(jsFile,  frontFtlBasePath + "/api" + (CodeGeneratorConstant.VUE3.equals(config.getFrontType()) ? CustomFileEnum.TS.path : CustomFileEnum.JS.path));
            
        
            // 如果是vue3，那么需要增加form.vue.ftl和schema.ts.ftl
            if (CodeGeneratorConstant.VUE3.equals(config.getFrontType()))
            {
                String dataTsName = dataJsName + ".data";
                customMap.put(CodeGeneratorConstant.DATA_TS_NAME, dataTsName);
                String vueFormFile = vueFileEntity + config.getFormType().replaceAll("Tab", "Detail") + CodeGeneratorConstant.FORM_VUE;
                String tsFile = dataTsName + CodeGeneratorConstant.TS;
                customMap.put(GitEggCodeGeneratorConstant.VUE_FORM_PATH, servicePath.replace(File.separator, StrUtil.SLASH) + StrUtil.SLASH + config.getModuleCode() + StrUtil.SLASH + vueFormFile);
                customMap.put(GitEggCodeGeneratorConstant.VUE_TS_PATH, servicePath.replace(File.separator, StrUtil.SLASH) + StrUtil.SLASH + config.getModuleCode() + StrUtil.SLASH + tsFile);
            
                customFileMap.put(vueFormFile, frontFtlBasePath + "/form/" + config.getFormType().replaceAll("Modal","").replaceAll("Drawer","")  + CustomFileEnum.VUE_FORM.path);
                customFileMap.put(tsFile,  frontFtlBasePath + "/schema" + CustomFileEnum.SCHEMA_TS.path);
                
                customFilePath.put(vueFormFile, vuePath);
                customFilePath.put(tsFile, vuePath);
            }
        }
    
        customMap.put(GitEggCodeGeneratorConstant.CUSTOM_FILE_PATH_MAP, customFilePath);
        customMap.put(GitEggCodeGeneratorConstant.RESOURCE_MAX_ID, finalMaxId);
    
        builder.customMap(customMap).customFile(customFileMap);
    }

    private boolean processGenerateMainSubCode(QueryConfigDTO queryConfigDTO, Config mainConfig){

        QueryFieldDTO queryFieldDTO = new QueryFieldDTO();
        queryFieldDTO.setGenerationId(queryConfigDTO.getId());
        List<FieldDTO> fieldDTOS = fieldService.queryFieldList(queryFieldDTO);

        QueryWrapper<TableJoin> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(CodeGeneratorConstant.GENERATION_ID, queryConfigDTO.getId());
        List<TableJoin> tableJoinList = tableJoinService.list(queryWrapper);

        // 生成代码时，先生成主表
        List<String> tableNames = Lists.newArrayList(mainConfig.getTableName());

        Map<String, TableJoin> tableJoinMap = new HashMap<>();
        // 将子表加入生成的list中
        if(!CollectionUtils.isEmpty(tableJoinList))
        {
            tableJoinList.stream().forEach(tableJoin->{
                tableNames.add(tableJoin.getJoinTableName());
                tableJoinMap.put(tableJoin.getJoinTableName(), tableJoin);
            });
        }

        boolean extendsFlag = mainConfig.getExtendsFlag();

        // 设置自定义配置
        Map<String, Object> customMap = new HashMap<>();

        Map<String, List<FieldDTO>> mainFieldsMap = new HashMap<>();

        for (String joinTableName : tableNames)
        {

            List<FieldDTO> fieldJoinDTOS = fieldDTOS.stream().filter(f->joinTableName.equals(f.getJoinTableName())).collect(Collectors.toList());

            //提取表单的字段
            List<FieldDTO> formFieldDTOS = fieldJoinDTOS.stream().filter(f->f.getFormAdd() || f.getFormEdit()).collect(Collectors.toList());

            //提取有数据字典的字段，用于界面遍历
            List<FieldDTO> dictCodeFieldDTOS = fieldJoinDTOS.stream().filter(f-> FieldUtils.isDictCodeField(f.getControlType())).collect(Collectors.toList());

            TableJoin tableJoin = tableJoinMap.get(joinTableName);

            Config config = BeanCopierUtils.copyByClass(mainConfig, Config.class);
            // 设置主表和子表的标识，用于选择不同的模板
            boolean mainModule = true;
            if (null != tableJoin && !tableJoin.getJoinTableName().equals(config.getTableName())
                    && !tableJoin.getJoinTableAlias().equals(config.getTableAlias()))
            {
                mainModule = false;
                config.setModuleCode(tableJoin.getModuleCode());
                config.setModuleName(tableJoin.getModuleName());
                config.setControllerPath(tableJoin.getControllerPath());
                config.setTableName(tableJoin.getJoinTableName());
                config.setTableAlias(tableJoin.getJoinTableAlias());
                config.setTablePrefix(tableJoin.getJoinTablePrefix());

                customMap.put(GitEggCodeGeneratorConstant.TABLES_JOIN, tableJoin);
                customMap.put(GitEggCodeGeneratorConstant.COLUMN_NAME, tableJoin.getAssociationId());
                customMap.put(GitEggCodeGeneratorConstant.ASSOCIATION_ID, StrUtil.toCamelCase(tableJoin.getAssociationId()));
            }

            customMap.put(GitEggCodeGeneratorConstant.CONFIG, config);
            customMap.put(GitEggCodeGeneratorConstant.FIELDS, fieldJoinDTOS);
            customMap.put(GitEggCodeGeneratorConstant.FORM_FIELDS, formFieldDTOS);
            customMap.put(GitEggCodeGeneratorConstant.DICT_CODE_FIELDS, dictCodeFieldDTOS);

            //baseEntity里面有的，DTO中需要排除的字段
            List<String> baseEntityFieldList = BaseEntityEnum.getBaseEntityFieldList();
            customMap.put(GitEggCodeGeneratorConstant.BASE_ENTITY_FIELD_LIST, baseEntityFieldList);

            //查询数据源配置
            Datasource datasource = datasourceService.getById(config.getDatasourceId());

            String serviceName = config.getServiceName();
            //前端代码路径
            String frontCodePath = config.getFrontCodePath();
            String frontCodeDir = config.getFrontCodeDir();
            //后端代码路径
            String serviceCodePath = config.getServiceCodePath();
            //自定义路径
            String parent = config.getParentPackage();
            String moduleName = config.getModuleCode();
            String codeDirPath =  (parent + StrUtil.DOT + moduleName).replace(StrUtil.DOT, File.separator) + File.separator;

            // 查询资源权限表最大的id，用于生成资源权限脚本
            Long maxId = GitEggConstant.ZERO_LONG;
            Result<Object> result = resourceFeign.queryResourceMaxId();
            if (null != result && result.isSuccess())
            {
                maxId = Long.parseLong(result.getData().toString()) + GitEggConstant.ONE_LONG;
            }
            Long finalMaxId = maxId;
            boolean finalMainModule = mainModule;
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

                        String tableName = config.getTableName();
                        String tablePrefix = config.getTablePrefix();
                        if (!StringUtils.isEmpty(tablePrefix))
                        {
                            tableName = tableName.replaceFirst(tablePrefix,StrUtil.EMPTY);
                        }

                        String dtoName = StrUtil.upperFirst(StrUtil.toCamelCase(tableName));
                        //dto
                        String dtoFile = dtoName + CodeGeneratorConstant.DTO_JAVA;
                        String createDtoFile = CodeGeneratorConstant.CREATE + dtoFile;
                        String updateDtoFile = CodeGeneratorConstant.UPDATE + dtoFile;
                        String queryDtoFile = CodeGeneratorConstant.QUERY + dtoFile;
                        String queryFile = CodeGeneratorConstant.QUERY + dtoName + CodeGeneratorConstant.FILE_JAVA;
                        //Export and Import
                        String exportFile = dtoName + CodeGeneratorConstant.EXPORT_JAVA;
                        String importFile = dtoName + CodeGeneratorConstant.IMPORT_JAVA;
                        // SQL
                        String sqlFile = dtoName + CodeGeneratorConstant.RESOURCE_SQL;

                        // 设置自定义输出文件
                        Map<String, String> customFileMap = new HashMap<>();

                        if (finalMainModule)
                        {
                            customFileMap.put(dtoFile, CustomFileEnum.DTO_FILE.path);
                            customFileMap.put(createDtoFile, CustomFileEnum.CREATE_DTO.path);
                            customFileMap.put(updateDtoFile, CustomFileEnum.UPDATE_DTO.path);
                            customFileMap.put(queryDtoFile, CustomFileEnum.QUERY_DTO.path);
                            // Export and Import
                            customFileMap.put(exportFile, CustomFileEnum.EXPORT.path);
                            customFileMap.put(importFile, CustomFileEnum.IMPORT.path);
                            // SQL
                            customFileMap.put(sqlFile, CustomFileEnum.SQL.path);
                        }
                        else
                        {
                            if (extendsFlag)
                            {
                                customFileMap.put(dtoFile, CustomFileMainSubEnum.DTO_FILE.path);
                                customFileMap.put(createDtoFile, CustomFileMainSubEnum.CREATE_DTO.path);
                                customFileMap.put(updateDtoFile, CustomFileMainSubEnum.UPDATE_DTO.path);
                                customFileMap.put(queryDtoFile, CustomFileMainSubEnum.QUERY_DTO.path);
                                customFileMap.put(queryFile, CustomFileMainSubEnum.QUERY.path);
                                // Export and Import
                                customFileMap.put(exportFile, CustomFileMainSubEnum.EXPORT.path);
                                customFileMap.put(importFile, CustomFileMainSubEnum.IMPORT.path);
                                // SQL
                                customFileMap.put(sqlFile, CustomFileMainSubEnum.SQL.path);
                            }
                            else
                            {
                                customFileMap.put(dtoFile, CustomFileMainSubExtendsEnum.DTO_FILE.path);
                                customFileMap.put(createDtoFile, CustomFileMainSubExtendsEnum.CREATE_DTO.path);
                                customFileMap.put(updateDtoFile, CustomFileMainSubExtendsEnum.UPDATE_DTO.path);
                                customFileMap.put(queryDtoFile, CustomFileMainSubExtendsEnum.QUERY_DTO.path);
                                customFileMap.put(queryFile, CustomFileMainSubExtendsEnum.QUERY.path);
                                // Export and Import
                                customFileMap.put(exportFile, CustomFileMainSubExtendsEnum.EXPORT.path);
                                customFileMap.put(importFile, CustomFileMainSubExtendsEnum.IMPORT.path);
                                // SQL
                                customFileMap.put(sqlFile, CustomFileMainSubExtendsEnum.SQL.path);
                            }


                            //整合主表字表字段
                            List<FieldDTO> fieldsUnion = new ArrayList<>(mainFieldsMap.get(GitEggCodeGeneratorConstant.MAIN_FIELDS));
                            fieldsUnion.addAll(fieldJoinDTOS);

                            //整合主表字表字段
                            List<FieldDTO> formFieldsUnion = new ArrayList<>(mainFieldsMap.get(GitEggCodeGeneratorConstant.MAIN_FORM_FIELDS));
                            formFieldsUnion.addAll(formFieldDTOS);

                            //整合主表字表字段
                            List<FieldDTO> dictCodeFieldsUnion = new ArrayList<>(mainFieldsMap.get(GitEggCodeGeneratorConstant.MAIN_DICTCODE_FIELDS));
                            dictCodeFieldsUnion.addAll(dictCodeFieldDTOS);

                            customMap.put(GitEggCodeGeneratorConstant.FIELDS_UNION, fieldsUnion);
                            customMap.put(GitEggCodeGeneratorConstant.FORM_FIELDS_UNION, formFieldsUnion);
                            customMap.put(GitEggCodeGeneratorConstant.DICT_CODE_FIELDS_UNION, dictCodeFieldsUnion);
                        }

                        //因为目前版本框架只支持自定义输出到other目录，所以这里利用重写AbstractTemplateEngine的outputCustomFile方法支持所有自定义文件输出目录
                        Map<String, String> customFilePath = new HashMap<>();

                        String servicePath = StrUtil.EMPTY;
                        //处理服务名
                        if (!StringUtils.isEmpty(serviceName))
                        {
                            int start = serviceName.indexOf(StrUtil.DASHED);

                            if (start >= 0)
                            {
                                int end = serviceName.length();
                                servicePath = serviceName.substring(start, end).replace(StrUtil.DASHED, File.separator);
                            }
                            else
                            {
                                servicePath = servicePath + File.separator + serviceName;
                            }

                            if (!StringUtils.isEmpty(frontCodeDir))
                            {
                                servicePath = frontCodeDir + servicePath ;
                            }
                        }

                        //判断是否生成后端代码
                        if (config.getCodeType().equals(CodeGeneratorConstant.CODE_ALL) || config.getCodeType().equals(CodeGeneratorConstant.CODE_SERVICE))
                        {
                            //dto
                            String dtoPath = serviceCodePath + GitEggCodeGeneratorConstant.JAVA_PATH + codeDirPath + CodeGeneratorConstant.DTO;
                            customFilePath.put(dtoFile, dtoPath);
                            customFilePath.put(createDtoFile, dtoPath);
                            customFilePath.put(updateDtoFile, dtoPath);
                            customFilePath.put(queryDtoFile, dtoPath);
                            customFilePath.put(queryFile, dtoPath);

                            if (finalMainModule)
                            {
                                String mainParentPackage = parent + StrUtil.DOT + moduleName;
                                customMap.put(GitEggCodeGeneratorConstant.MAIN_PACKAGE_PATH, mainParentPackage);
                                customMap.put(GitEggCodeGeneratorConstant.MAIN_DTO_NAME, dtoName + GitEggCodeGeneratorConstant.DTO);
                                customMap.put(GitEggCodeGeneratorConstant.MAIN_ENTITY_NAME, dtoName);
                                customMap.put(GitEggCodeGeneratorConstant.MAIN_TABLE_NAME, tableName);
                                customMap.put(GitEggCodeGeneratorConstant.MAIN_TABLE, mainConfig.getTableName());
                                customMap.put(GitEggCodeGeneratorConstant.MAIN_TABLE_ALIAS, mainConfig.getTableAlias());
                                customMap.put(GitEggCodeGeneratorConstant.MAIN_TABLE_FIELDS, fieldJoinDTOS);

                                String mainDomain = StrUtil.toCamelCase(tableName);


                                List<FieldDTO> mainFields = BeanCopierUtils.deepCopyList(fieldJoinDTOS);
                                mainFields.forEach((field) -> {
                                    field.setMainEntityName(mainDomain);
                                });

                                List<FieldDTO> mainFormFields = BeanCopierUtils.deepCopyList(formFieldDTOS);
                                mainFormFields.forEach((field) -> {
                                    field.setMainEntityName(mainDomain);
                                });

                                List<FieldDTO> mainDictCodeFields = BeanCopierUtils.deepCopyList(dictCodeFieldDTOS);
                                mainDictCodeFields.forEach((field) -> {
                                    field.setMainEntityName(mainDomain);
                                });

                                customMap.put(GitEggCodeGeneratorConstant.MAIN_FIELDS, mainFields);
                                customMap.put(GitEggCodeGeneratorConstant.MAIN_FORM_FIELDS, mainFormFields);
                                customMap.put(GitEggCodeGeneratorConstant.MAIN_DICTCODE_FIELDS, mainDictCodeFields);
                                customMap.put(GitEggCodeGeneratorConstant.MAIN_CONTROLLER_PATH, config.getControllerPath());

                                mainFieldsMap.put(GitEggCodeGeneratorConstant.MAIN_FIELDS, mainFields);
                                mainFieldsMap.put(GitEggCodeGeneratorConstant.MAIN_FORM_FIELDS, mainFormFields);
                                mainFieldsMap.put(GitEggCodeGeneratorConstant.MAIN_DICTCODE_FIELDS, mainDictCodeFields);
                            }

                            // Export and Import
                            String entityPath = serviceCodePath + GitEggCodeGeneratorConstant.JAVA_PATH + codeDirPath + CodeGeneratorConstant.ENTITY;
                            customFilePath.put(exportFile, entityPath);
                            customFilePath.put(importFile, entityPath);
                            // SQL
                            String sqlPath = serviceCodePath + GitEggCodeGeneratorConstant.RESOURCES_PATH + codeDirPath + CodeGeneratorConstant.MAPPER;
                            customFilePath.put(sqlFile, sqlPath);

                        }

                        //判断是否生成前端代码
                        if (config.getCodeType().equals(CodeGeneratorConstant.CODE_ALL) || config.getCodeType().equals(CodeGeneratorConstant.CODE_FRONT))
                        {
                            // vue and js
                            String vueFile = config.getModuleCode() + CodeGeneratorConstant.TABLE_VUE;
                            String jsFile = config.getModuleCode() + CodeGeneratorConstant.JS;

                            String vuePath = frontCodePath + GitEggCodeGeneratorConstant.VUE_PATH + (StringUtils.isEmpty(servicePath)?StrUtil.EMPTY:(servicePath + File.separator)) + config.getModuleCode();
                            String jsPath = frontCodePath + GitEggCodeGeneratorConstant.JS_PATH + (StringUtils.isEmpty(servicePath)?StrUtil.EMPTY:(servicePath + File.separator)) + config.getModuleCode();
                            customFilePath.put(vueFile, vuePath);
                            customFilePath.put(jsFile, jsPath);
                            // VUE AND JS
                            // TODO 要支持树形表、左树右表、左表右表、左表右树、左树右树形表、左树右树
                            if (finalMainModule) {
                                customFileMap.put(vueFile, CustomFileEnum.VUE.path);
                                customFileMap.put(jsFile, CustomFileEnum.JS.path);
                            }
                            else
                            {
                                if (extendsFlag)
                                {
                                    customFileMap.put(vueFile, CustomFileMainSubEnum.VUE.path);
                                    customFileMap.put(jsFile, CustomFileMainSubEnum.JS.path);
                                }
                                else
                                {
                                    customFileMap.put(vueFile, CustomFileMainSubExtendsEnum.VUE.path);
                                    customFileMap.put(jsFile, CustomFileMainSubExtendsEnum.JS.path);
                                }
                            }
                            customMap.put(GitEggCodeGeneratorConstant.VUE_TABLE_PATH, servicePath.replace(File.separator, StrUtil.SLASH) + StrUtil.SLASH + config.getModuleCode() + StrUtil.SLASH + vueFile);
                            customMap.put(GitEggCodeGeneratorConstant.VUE_JS_PATH, (StringUtils.isEmpty(servicePath)?StrUtil.EMPTY:(servicePath.replace(File.separator, StrUtil.SLASH) + StrUtil.SLASH)) + config.getModuleCode() + StrUtil.SLASH + config.getModuleCode());
                        }

                        customMap.put(GitEggCodeGeneratorConstant.CUSTOM_FILE_PATH_MAP, customFilePath);
                        customMap.put(GitEggCodeGeneratorConstant.RESOURCE_MAX_ID, finalMaxId);

                        builder.customMap(customMap)
                                .customFile(customFileMap);
                    })
                    .strategyConfig(builder -> {
                        builder
                                .addInclude(config.getTableName())
                                .addTablePrefix(config.getTablePrefix())
                                .entityBuilder()
                                .enableLombok()
                                .enableTableFieldAnnotation() // 实体字段注解
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
//                        .enableMapperAnnotation()
                                .enableBaseResultMap()
                                .enableBaseColumnList()
                        ;
                    })
                    .templateConfig(builder -> {
                        if (config.getCodeType().equals(CodeGeneratorConstant.CODE_FRONT)) {
                            builder.disable();
                        }
                        if (finalMainModule)
                        {
                            builder.service(CustomFileMainSubEnum.SERVICE_MAIN_FILE.path)
                                   .serviceImpl(CustomFileMainSubEnum.SERVICE_IMPL_MAIN_FILE.path)
                                    .controller(CustomFileMainSubEnum.CONTROLLER_MAIN_FILE.path)
                                    .entity(CustomFileEnum.ENTITY_FILE.path)
                                    .mapper(CustomFileEnum.MAPPER_FILE.path)
                                   .mapperXml(CustomFileMainSubEnum.MAPPER_MAIN_XML_FILE.path);
                        }
                        else {
                            if (extendsFlag)
                            {
                                builder.entity(CustomFileMainSubEnum.ENTITY_FILE.path)
                                        .service(CustomFileMainSubEnum.SERVICE_FILE.path)
                                        .serviceImpl(CustomFileMainSubEnum.SERVICE_IMPL_FILE.path)
                                        .mapper(CustomFileMainSubEnum.MAPPER_FILE.path)
                                        .mapperXml(CustomFileMainSubEnum.MAPPER_XML_FILE.path)
                                        .controller(CustomFileMainSubEnum.CONTROLLER_FILE.path)
                                        .build();
                            }
                            else
                            {
                                builder.entity(CustomFileMainSubExtendsEnum.ENTITY_FILE.path)
                                        .service(CustomFileMainSubExtendsEnum.SERVICE_FILE.path)
                                        .serviceImpl(CustomFileMainSubExtendsEnum.SERVICE_IMPL_FILE.path)
                                        .mapper(CustomFileMainSubExtendsEnum.MAPPER_FILE.path)
                                        .mapperXml(CustomFileMainSubExtendsEnum.MAPPER_XML_FILE.path)
                                        .controller(CustomFileMainSubExtendsEnum.CONTROLLER_FILE.path)
                                        .build();
                            }
                        }
                    })
                    // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                    .templateEngine(new GitEggFreemarkerTemplateEngine())
                    .execute();
        }
        return true;
    }
}
