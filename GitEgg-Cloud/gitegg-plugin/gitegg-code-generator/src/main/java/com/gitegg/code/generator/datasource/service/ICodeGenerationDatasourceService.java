package com.gitegg.code.generator.datasource.service;

import java.util.List;

import com.gitegg.code.generator.datasource.entity.CodeGenerationDatasource;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import com.gitegg.code.generator.datasource.dto.CodeGenerationDatasourceDTO;
import com.gitegg.code.generator.datasource.dto.CreateCodeGenerationDatasourceDTO;
import com.gitegg.code.generator.datasource.dto.UpdateCodeGenerationDatasourceDTO;
import com.gitegg.code.generator.datasource.dto.QueryCodeGenerationDatasourceDTO;

/**
 * <p>
 * 数据源配置表 服务类
 * </p>
 *
 * @author GitEgg
 * @since 2021-08-18 16:39:49
 */
public interface ICodeGenerationDatasourceService extends IService<CodeGenerationDatasource> {

    /**
    * 分页查询数据源配置表列表
    * @param page
    * @param queryCodeGenerationDatasourceDTO
    * @return
    */
    Page<CodeGenerationDatasourceDTO> queryCodeGenerationDatasourceList(Page<CodeGenerationDatasourceDTO> page, QueryCodeGenerationDatasourceDTO queryCodeGenerationDatasourceDTO);

    /**
     * 查询数据源配置表列表
     * @param queryCodeGenerationDatasourceDTO
     * @return
     */
    List<CodeGenerationDatasourceDTO> queryCodeGenerationDatasourceList(QueryCodeGenerationDatasourceDTO queryCodeGenerationDatasourceDTO);

    /**
    * 查询数据源配置表详情
    * @param queryCodeGenerationDatasourceDTO
    * @return
    */
    CodeGenerationDatasourceDTO queryCodeGenerationDatasource(QueryCodeGenerationDatasourceDTO queryCodeGenerationDatasourceDTO);

    /**
    * 创建数据源配置表
    * @param codeGenerationDatasource
    * @return
    */
    boolean createCodeGenerationDatasource(CreateCodeGenerationDatasourceDTO codeGenerationDatasource);

    /**
    * 更新数据源配置表
    * @param codeGenerationDatasource
    * @return
    */
    boolean updateCodeGenerationDatasource(UpdateCodeGenerationDatasourceDTO codeGenerationDatasource);

    /**
    * 删除数据源配置表
    * @param codeGenerationDatasourceId
    * @return
    */
    boolean deleteCodeGenerationDatasource(Long codeGenerationDatasourceId);

    /**
    * 批量删除数据源配置表
    * @param codeGenerationDatasourceIds
    * @return
    */
    boolean batchDeleteCodeGenerationDatasource(List<Long> codeGenerationDatasourceIds);
}
