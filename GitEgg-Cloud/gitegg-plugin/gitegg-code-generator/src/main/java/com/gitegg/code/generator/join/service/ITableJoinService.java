package com.gitegg.code.generator.join.service;

import java.util.List;

import com.gitegg.code.generator.join.entity.TableJoin;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import com.gitegg.code.generator.join.dto.TableJoinDTO;
import com.gitegg.code.generator.join.dto.CreateTableJoinDTO;
import com.gitegg.code.generator.join.dto.UpdateTableJoinDTO;
import com.gitegg.code.generator.join.dto.QueryTableJoinDTO;

/**
 * <p>
 * 多表查询时的联合表配置 服务类
 * </p>
 *
 * @author GitEgg
 * @since 2021-09-03 11:38:07
 */
public interface ITableJoinService extends IService<TableJoin> {

    /**
    * 分页查询多表查询时的联合表配置列表
    * @param page
    * @param queryTableJoinDTO
    * @return
    */
    Page<TableJoinDTO> queryTableJoinList(Page<TableJoinDTO> page, QueryTableJoinDTO queryTableJoinDTO);

    /**
    * 查询多表查询时的联合表配置详情
    * @param queryTableJoinDTO
    * @return
    */
    TableJoinDTO queryTableJoin(QueryTableJoinDTO queryTableJoinDTO);

    /**
    * 创建多表查询时的联合表配置
    * @param tableJoin
    * @return
    */
    boolean createTableJoin(CreateTableJoinDTO tableJoin);

    /**
    * 更新多表查询时的联合表配置
    * @param tableJoin
    * @return
    */
    boolean updateTableJoin(UpdateTableJoinDTO tableJoin);

    /**
    * 删除多表查询时的联合表配置
    * @param tableJoinId
    * @return
    */
    boolean deleteTableJoin(Long tableJoinId);

    /**
    * 批量删除多表查询时的联合表配置
    * @param tableJoinIds
    * @return
    */
    boolean batchDeleteTableJoin(List<Long> tableJoinIds);
}
