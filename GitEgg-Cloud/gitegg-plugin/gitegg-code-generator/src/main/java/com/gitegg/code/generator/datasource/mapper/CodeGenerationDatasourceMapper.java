package com.gitegg.code.generator.datasource.mapper;

import com.gitegg.code.generator.datasource.entity.CodeGenerationDatasource;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.gitegg.code.generator.datasource.dto.CodeGenerationDatasourceDTO;
import com.gitegg.code.generator.datasource.dto.QueryCodeGenerationDatasourceDTO;

import java.util.List;

/**
 * <p>
 * 数据源配置表 Mapper 接口
 * </p>
 *
 * @author GitEgg
 * @since 2021-08-18 16:39:49
 */
public interface CodeGenerationDatasourceMapper extends BaseMapper<CodeGenerationDatasource> {

    /**
    * 分页查询数据源配置表列表
    * @param page
    * @param codeGenerationDatasourceDTO
    * @return
    */
    Page<CodeGenerationDatasourceDTO> queryCodeGenerationDatasourceList(Page<CodeGenerationDatasourceDTO> page, @Param("codeGenerationDatasource") QueryCodeGenerationDatasourceDTO codeGenerationDatasourceDTO);

    /**
     * 查询数据源配置表列表
     * @param codeGenerationDatasourceDTO
     * @return
     */
    List<CodeGenerationDatasourceDTO> queryCodeGenerationDatasourceList(@Param("codeGenerationDatasource") QueryCodeGenerationDatasourceDTO codeGenerationDatasourceDTO);

    /**
    * 查询数据源配置表信息
    * @param codeGenerationDatasourceDTO
    * @return
    */
    CodeGenerationDatasourceDTO queryCodeGenerationDatasource(@Param("codeGenerationDatasource") QueryCodeGenerationDatasourceDTO codeGenerationDatasourceDTO);
}
