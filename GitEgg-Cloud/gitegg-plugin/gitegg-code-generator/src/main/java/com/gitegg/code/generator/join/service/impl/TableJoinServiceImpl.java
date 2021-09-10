package com.gitegg.code.generator.join.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import com.gitegg.code.generator.join.entity.TableJoin;
import com.gitegg.code.generator.join.mapper.TableJoinMapper;
import com.gitegg.code.generator.join.service.ITableJoinService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.gitegg.platform.base.util.BeanCopierUtils;
import com.gitegg.code.generator.join.dto.TableJoinDTO;
import com.gitegg.code.generator.join.dto.CreateTableJoinDTO;
import com.gitegg.code.generator.join.dto.UpdateTableJoinDTO;
import com.gitegg.code.generator.join.dto.QueryTableJoinDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 多表查询时的联合表配置 服务实现类
 * </p>
 *
 * @author GitEgg
 * @since 2021-09-03 11:38:07
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class TableJoinServiceImpl extends ServiceImpl<TableJoinMapper, TableJoin> implements ITableJoinService {

    private final TableJoinMapper tableJoinMapper;

    /**
    * 分页查询多表查询时的联合表配置列表
    * @param page
    * @param queryTableJoinDTO
    * @return
    */
    @Override
    public Page<TableJoinDTO> queryTableJoinList(Page<TableJoinDTO> page, QueryTableJoinDTO queryTableJoinDTO) {
        Page<TableJoinDTO> tableJoinInfoList = tableJoinMapper.queryTableJoinList(page, queryTableJoinDTO);
        return tableJoinInfoList;
    }

    /**
    * 查询多表查询时的联合表配置详情
    * @param queryTableJoinDTO
    * @return
    */
    @Override
    public TableJoinDTO queryTableJoin(QueryTableJoinDTO queryTableJoinDTO) {
        TableJoinDTO tableJoinDTO = tableJoinMapper.queryTableJoin(queryTableJoinDTO);
        return tableJoinDTO;
    }

    /**
    * 创建多表查询时的联合表配置
    * @param tableJoin
    * @return
    */
    @Override
    public boolean createTableJoin(CreateTableJoinDTO tableJoin) {
        TableJoin tableJoinEntity = BeanCopierUtils.copyByClass(tableJoin, TableJoin.class);
        boolean result = this.save(tableJoinEntity);
        return result;
    }

    /**
    * 更新多表查询时的联合表配置
    * @param tableJoin
    * @return
    */
    @Override
    public boolean updateTableJoin(UpdateTableJoinDTO tableJoin) {
        TableJoin tableJoinEntity = BeanCopierUtils.copyByClass(tableJoin, TableJoin.class);
        QueryWrapper<TableJoin> wrapper = new QueryWrapper<>();
        wrapper.eq("id", tableJoinEntity.getId());
        boolean result = this.update(tableJoinEntity, wrapper);
        return result;
    }

    /**
    * 删除多表查询时的联合表配置
    * @param tableJoinId
    * @return
    */
    @Override
    public boolean deleteTableJoin(Long tableJoinId) {
        boolean result = this.removeById(tableJoinId);
        return result;
    }

    /**
    * 批量删除多表查询时的联合表配置
    * @param tableJoinIds
    * @return
    */
    @Override
    public boolean batchDeleteTableJoin(List<Long> tableJoinIds) {
        boolean result = this.removeByIds(tableJoinIds);
        return result;
    }
}
