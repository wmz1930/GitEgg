package com.gitegg.code.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

/**
 * 
 * @author  GitEgg
 * @date 2020/12/14
 **/
public class MybatisPlusTest {

    /**
     * <p>
     * 读取控制台内容
     * </p>
     */
    public static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder help = new StringBuilder();
        help.append("请输入" + tip + "：");
        System.out.println(help.toString());
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.isNotBlank(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }

    public static void main(String[] args) {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        // String projectPath = System.getProperty("user.dir");
        String projectPath = "D:/MyOpenCode/github_new/GitEgg/GitEgg-Cloud/gitegg-mall/gitegg-mall-order";
        String projectPortalPath = "D:/MyOpenCode/github_new/GitEgg/GitEgg-Portal/gitegg-portal-ant-design/src";

        gc.setOutputDir(projectPath + "/src/main/java");
        gc.setAuthor("GitEgg");
        gc.setOpen(false);
        gc.setSwagger2(true); // 实体属性 Swagger2 注解
        gc.setIdType(IdType.AUTO);
        gc.setFileOverride(true);
        // gc.setActiveRecord(false);// 不需要ActiveRecord特性的请改为false
        gc.setEnableCache(false);// XML 二级缓存
        gc.setBaseResultMap(true);// XML ResultMap
        gc.setBaseColumnList(true);// XML columList
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl(
            "jdbc:mysql://127.0.0.1/gitegg_cloud?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone = GMT");
        // dsc.setSchemaName("public");
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("root");
        mpg.setDataSource(dsc);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setModuleName(scanner("order"));
        pc.setParent("com.gitegg.mall");
        mpg.setPackageInfo(pc);

        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };

        // 如果模板引擎是 freemarker
        String templatePath = "/templates/mapper.xml.ftl";
        // 如果模板引擎是 velocity
        // String templatePath = "/templates/mapper.xml.vm";

        // 自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();
        // Mapper.xml 存放路径
        String mapperDir = "com/gitegg/mall/order/mapper/";
        // 自定义配置会被优先输出
        focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return projectPath + "/src/main/resources/" + mapperDir + tableInfo.getEntityName() + "Mapper"
                    + StringPool.DOT_XML;
            }
        });

        focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return projectPath + "/src/main/resources/" + mapperDir + tableInfo.getEntityName() + "Mapper"
                    + StringPool.DOT_XML;
            }
        });

        // sql
        focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return projectPath + "/src/main/resources/" + mapperDir + tableInfo.getEntityName() + "Resource.sql";
            }
        }.setTemplatePath("/templates/resource.sql.ftl"));

        // DTO.java
        String dtoDir = "com/gitegg/mall/order/dto/";
        focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return projectPath + "/src/main/java/" + dtoDir + tableInfo.getEntityName() + "DTO"
                    + StringPool.DOT_JAVA;
            }
        }.setTemplatePath("/templates/dto.java.ftl"));

        // CreateDTO.java
        focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return projectPath + "/src/main/java/" + dtoDir + "Create" + tableInfo.getEntityName() + "DTO"
                    + StringPool.DOT_JAVA;
            }
        }.setTemplatePath("/templates/createDTO.java.ftl"));

        // UpdateDTO.java
        focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return projectPath + "/src/main/java/" + dtoDir + "Update" + tableInfo.getEntityName() + "DTO"
                    + StringPool.DOT_JAVA;
            }
        }.setTemplatePath("/templates/updateDTO.java.ftl"));

        // QueryDTO.java
        focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return projectPath + "/src/main/java/" + dtoDir + "Query" + tableInfo.getEntityName() + "DTO"
                    + StringPool.DOT_JAVA;
            }
        }.setTemplatePath("/templates/queryDTO.java.ftl"));

        // 前端页面生成
        // vue
        focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return projectPortalPath + "/views/mall/order/" + tableInfo.getEntityPath() + "/"
                    + tableInfo.getEntityPath() + "Table.vue";
            }
        }.setTemplatePath("/templates/pageListTable.vue.ftl"));

        // js
        focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return projectPortalPath + "/api/mall/order/" + tableInfo.getEntityPath() + ".js";
            }
        }.setTemplatePath("/templates/pageListTable.js.ftl"));

        /*
        cfg.setFileCreate(new IFileCreate() {
            @Override
            public boolean isCreate(ConfigBuilder configBuilder, FileType fileType, String filePath) {
                // 判断自定义文件夹是否需要创建
                checkDir("调用默认方法创建的目录，自定义目录用");
                if (fileType == FileType.MAPPER) {
                    // 已经生成 mapper 文件判断存在，不想重新生成返回 false
                    return !new File(filePath).exists();
                }
                // 允许生成模板文件
                return true;
            }
        });
        */
        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);

        // 配置模板
        TemplateConfig templateConfig = new TemplateConfig();

        // 配置自定义输出模板
        // 指定自定义模板路径，注意不要带上.ftl/.vm, 会根据使用的模板引擎自动识别
        // templateConfig.setEntity("templates/entity2.java");
        // templateConfig.setService();
        // templateConfig.setController();
        // templateConfig.setService("templates/entity2.java");
        // templateConfig.setServiceImpl();
        templateConfig.setController("templates/controller.java");
        // templateConfig.setMapper();
        // templateConfig.setXml();

        templateConfig.setXml(null);
        mpg.setTemplate(templateConfig);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setSuperEntityClass("com.gitegg.platform.mybatis.entity.BaseEntity");
        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(true);
        // 公共父类
        // strategy.setSuperControllerClass("你自己的父类控制器,没有就不用设置!");
        // 写于父类中的公共字段
        strategy.setSuperEntityColumns(
            new String[] {"tenant_id", "create_time", "creator", "update_time", "operator", "del_flag"});
        strategy.setInclude(scanner("t_mall_order_sku").split(","));
        strategy.setControllerMappingHyphenStyle(true);
        strategy.setTablePrefix("t_mall_");
        strategy.setLogicDeleteFieldName("del_flag");

        List<TableFill> tableFillList = new ArrayList<>();
        TableFill tableFill1 = new TableFill("create_time", FieldFill.INSERT);
        TableFill tableFill2 = new TableFill("creator", FieldFill.INSERT);
        TableFill tableFill3 = new TableFill("update_time", FieldFill.UPDATE);
        TableFill tableFill4 = new TableFill("operator", FieldFill.UPDATE);
        tableFillList.add(tableFill1);
        tableFillList.add(tableFill2);
        tableFillList.add(tableFill3);
        tableFillList.add(tableFill4);
        strategy.setTableFillList(tableFillList);

        mpg.setStrategy(strategy);
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        mpg.execute();
    }
}
