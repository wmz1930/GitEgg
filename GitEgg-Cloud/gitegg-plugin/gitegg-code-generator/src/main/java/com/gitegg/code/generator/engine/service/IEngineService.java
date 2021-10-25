package com.gitegg.code.generator.engine.service;

import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.gitegg.code.generator.config.dto.QueryConfigDTO;
import com.gitegg.code.generator.engine.dto.TableDTO;

import java.util.List;

/**
 * 代码生成器接口
 *
 * @author GitEgg
 */
public interface IEngineService {

    /**
     * 查询某个数据源的所有表
     *
     * @param queryConfigDTO
     * @return
     */
    List<TableDTO> queryTableList(QueryConfigDTO queryConfigDTO);

    /**
     * 查询某个数据源表的字段信息
     *
     * @param datasourceId
     * @param tableNames
     * @return
     */
    List<TableInfo> queryTableFields(String datasourceId, List<String> tableNames);

    /**
     * 查询某个代码生成配置里面所有的字段
     * @param queryConfigDTO
     * @return
     */
    List<TableInfo> queryConfigFields(QueryConfigDTO queryConfigDTO);

    /**
     * 执行代码生成
     * @param queryConfigDTO
     * @return
     */
    boolean processGenerateCode(QueryConfigDTO queryConfigDTO);
}
