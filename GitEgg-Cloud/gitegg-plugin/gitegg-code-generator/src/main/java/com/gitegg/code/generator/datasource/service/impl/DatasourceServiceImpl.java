package com.gitegg.code.generator.datasource.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import com.gitegg.code.generator.datasource.entity.Datasource;
import com.gitegg.code.generator.datasource.mapper.DatasourceMapper;
import com.gitegg.code.generator.datasource.service.IDatasourceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.gitegg.platform.base.util.BeanCopierUtils;
import com.gitegg.code.generator.datasource.dto.DatasourceDTO;
import com.gitegg.code.generator.datasource.dto.CreateDatasourceDTO;
import com.gitegg.code.generator.datasource.dto.UpdateDatasourceDTO;
import com.gitegg.code.generator.datasource.dto.QueryDatasourceDTO;

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
public class DatasourceServiceImpl extends ServiceImpl<DatasourceMapper, Datasource> implements IDatasourceService {

    private final DatasourceMapper datasourceMapper;

    /**
    * 分页查询数据源配置表列表
    * @param page
    * @param queryDatasourceDTO
    * @return
    */
    @Override
    public Page<DatasourceDTO> queryDatasourceList(Page<DatasourceDTO> page, QueryDatasourceDTO queryDatasourceDTO) {
        Page<DatasourceDTO> datasourceInfoList = datasourceMapper.queryDatasourceList(page, queryDatasourceDTO);
        return datasourceInfoList;
    }

    /**
     * 查询数据源配置表列表
     * @param queryDatasourceDTO
     * @return
     */
    @Override
    public List<DatasourceDTO> queryDatasourceList(QueryDatasourceDTO queryDatasourceDTO) {
        List<DatasourceDTO> datasourceInfoList = datasourceMapper.queryDatasourceList(queryDatasourceDTO);
        return datasourceInfoList;
    }

    /**
    * 查询数据源配置表详情
    * @param queryDatasourceDTO
    * @return
    */
    @Override
    public DatasourceDTO queryDatasource(QueryDatasourceDTO queryDatasourceDTO) {
        DatasourceDTO datasourceDTO = datasourceMapper.queryDatasource(queryDatasourceDTO);
        return datasourceDTO;
    }

    /**
    * 创建数据源配置表
    * @param datasource
    * @return
    */
    @Override
    public boolean createDatasource(CreateDatasourceDTO datasource) {
        Datasource datasourceEntity = BeanCopierUtils.copyByClass(datasource, Datasource.class);
        boolean result = this.save(datasourceEntity);
        return result;
    }

    /**
    * 更新数据源配置表
    * @param datasource
    * @return
    */
    @Override
    public boolean updateDatasource(UpdateDatasourceDTO datasource) {
        Datasource datasourceEntity = BeanCopierUtils.copyByClass(datasource, Datasource.class);
        QueryWrapper<Datasource> wrapper = new QueryWrapper<>();
        wrapper.eq("id", datasourceEntity.getId());
        boolean result = this.update(datasourceEntity, wrapper);
        return result;
    }

    /**
    * 删除数据源配置表
    * @param datasourceId
    * @return
    */
    @Override
    public boolean deleteDatasource(Long datasourceId) {
        boolean result = this.removeById(datasourceId);
        return result;
    }

    /**
    * 批量删除数据源配置表
    * @param datasourceIds
    * @return
    */
    @Override
    public boolean batchDeleteDatasource(List<Long> datasourceIds) {
        boolean result = this.removeByIds(datasourceIds);
        return result;
    }
}
