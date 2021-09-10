package com.gitegg.code.generator.datasource.mapper;

import com.gitegg.code.generator.datasource.entity.Datasource;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.gitegg.code.generator.datasource.dto.DatasourceDTO;
import com.gitegg.code.generator.datasource.dto.QueryDatasourceDTO;

import java.util.List;

/**
 * <p>
 * 数据源配置表 Mapper 接口
 * </p>
 *
 * @author GitEgg
 * @since 2021-08-18 16:39:49
 */
public interface DatasourceMapper extends BaseMapper<Datasource> {

    /**
    * 分页查询数据源配置表列表
    * @param page
    * @param datasourceDTO
    * @return
    */
    Page<DatasourceDTO> queryDatasourceList(Page<DatasourceDTO> page, @Param("datasource") QueryDatasourceDTO datasourceDTO);

    /**
     * 查询数据源配置表列表
     * @param datasourceDTO
     * @return
     */
    List<DatasourceDTO> queryDatasourceList(@Param("datasource") QueryDatasourceDTO datasourceDTO);

    /**
    * 查询数据源配置表信息
    * @param datasourceDTO
    * @return
    */
    DatasourceDTO queryDatasource(@Param("datasource") QueryDatasourceDTO datasourceDTO);
}
