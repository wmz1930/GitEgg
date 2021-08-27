package com.gitegg.code.generator.datasource.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import com.gitegg.code.generator.datasource.entity.CodeGenerationDatasource;
import com.gitegg.code.generator.datasource.mapper.CodeGenerationDatasourceMapper;
import com.gitegg.code.generator.datasource.service.ICodeGenerationDatasourceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.gitegg.platform.base.util.BeanCopierUtils;
import com.gitegg.code.generator.datasource.dto.CodeGenerationDatasourceDTO;
import com.gitegg.code.generator.datasource.dto.CreateCodeGenerationDatasourceDTO;
import com.gitegg.code.generator.datasource.dto.UpdateCodeGenerationDatasourceDTO;
import com.gitegg.code.generator.datasource.dto.QueryCodeGenerationDatasourceDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 数据源配置表 服务实现类
 * </p>
 *
 * @author GitEgg
 * @since 2021-08-18 16:39:49
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CodeGenerationDatasourceServiceImpl extends ServiceImpl<CodeGenerationDatasourceMapper, CodeGenerationDatasource> implements ICodeGenerationDatasourceService {

    private final CodeGenerationDatasourceMapper codeGenerationDatasourceMapper;

    /**
    * 分页查询数据源配置表列表
    * @param page
    * @param queryCodeGenerationDatasourceDTO
    * @return
    */
    @Override
    public Page<CodeGenerationDatasourceDTO> queryCodeGenerationDatasourceList(Page<CodeGenerationDatasourceDTO> page, QueryCodeGenerationDatasourceDTO queryCodeGenerationDatasourceDTO) {
        Page<CodeGenerationDatasourceDTO> codeGenerationDatasourceInfoList = codeGenerationDatasourceMapper.queryCodeGenerationDatasourceList(page, queryCodeGenerationDatasourceDTO);
        return codeGenerationDatasourceInfoList;
    }

    /**
     * 查询数据源配置表列表
     * @param queryCodeGenerationDatasourceDTO
     * @return
     */
    @Override
    public List<CodeGenerationDatasourceDTO> queryCodeGenerationDatasourceList(QueryCodeGenerationDatasourceDTO queryCodeGenerationDatasourceDTO) {
        List<CodeGenerationDatasourceDTO> codeGenerationDatasourceInfoList = codeGenerationDatasourceMapper.queryCodeGenerationDatasourceList(queryCodeGenerationDatasourceDTO);
        return codeGenerationDatasourceInfoList;
    }

    /**
    * 查询数据源配置表详情
    * @param queryCodeGenerationDatasourceDTO
    * @return
    */
    @Override
    public CodeGenerationDatasourceDTO queryCodeGenerationDatasource(QueryCodeGenerationDatasourceDTO queryCodeGenerationDatasourceDTO) {
        CodeGenerationDatasourceDTO codeGenerationDatasourceDTO = codeGenerationDatasourceMapper.queryCodeGenerationDatasource(queryCodeGenerationDatasourceDTO);
        return codeGenerationDatasourceDTO;
    }

    /**
    * 创建数据源配置表
    * @param codeGenerationDatasource
    * @return
    */
    @Override
    public boolean createCodeGenerationDatasource(CreateCodeGenerationDatasourceDTO codeGenerationDatasource) {
        CodeGenerationDatasource codeGenerationDatasourceEntity = BeanCopierUtils.copyByClass(codeGenerationDatasource, CodeGenerationDatasource.class);
        boolean result = this.save(codeGenerationDatasourceEntity);
        return result;
    }

    /**
    * 更新数据源配置表
    * @param codeGenerationDatasource
    * @return
    */
    @Override
    public boolean updateCodeGenerationDatasource(UpdateCodeGenerationDatasourceDTO codeGenerationDatasource) {
        CodeGenerationDatasource codeGenerationDatasourceEntity = BeanCopierUtils.copyByClass(codeGenerationDatasource, CodeGenerationDatasource.class);
        QueryWrapper<CodeGenerationDatasource> wrapper = new QueryWrapper<>();
        wrapper.eq("id", codeGenerationDatasourceEntity.getId());
        boolean result = this.update(codeGenerationDatasourceEntity, wrapper);
        return result;
    }

    /**
    * 删除数据源配置表
    * @param codeGenerationDatasourceId
    * @return
    */
    @Override
    public boolean deleteCodeGenerationDatasource(Long codeGenerationDatasourceId) {
        boolean result = this.removeById(codeGenerationDatasourceId);
        return result;
    }

    /**
    * 批量删除数据源配置表
    * @param codeGenerationDatasourceIds
    * @return
    */
    @Override
    public boolean batchDeleteCodeGenerationDatasource(List<Long> codeGenerationDatasourceIds) {
        boolean result = this.removeByIds(codeGenerationDatasourceIds);
        return result;
    }
}
