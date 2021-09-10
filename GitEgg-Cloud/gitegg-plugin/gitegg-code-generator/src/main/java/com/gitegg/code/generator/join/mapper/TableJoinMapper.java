package com.gitegg.code.generator.join.mapper;

import com.gitegg.code.generator.join.entity.TableJoin;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.gitegg.code.generator.join.dto.TableJoinDTO;
import com.gitegg.code.generator.join.dto.QueryTableJoinDTO;

/**
 * <p>
 * 多表查询时的联合表配置 Mapper 接口
 * </p>
 *
 * @author GitEgg
 * @since 2021-09-03 11:38:07
 */
public interface TableJoinMapper extends BaseMapper<TableJoin> {

    /**
    * 查询多表查询时的联合表配置列表
    * @param page
    * @param tableJoinDTO
    * @return
    */
    Page<TableJoinDTO> queryTableJoinList(Page<TableJoinDTO> page, @Param("tableJoin") QueryTableJoinDTO tableJoinDTO);

    /**
    * 查询多表查询时的联合表配置信息
    * @param tableJoinDTO
    * @return
    */
    TableJoinDTO queryTableJoin(@Param("tableJoin") QueryTableJoinDTO tableJoinDTO);
}
