package com.gitegg.service.extension.dfs.mapper;

import com.gitegg.service.extension.dfs.entity.Dfs;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.gitegg.service.extension.dfs.dto.DfsDTO;
import com.gitegg.service.extension.dfs.dto.QueryDfsDTO;

/**
 * <p>
 * 分布式存储配置表 Mapper 接口
 * </p>
 *
 * @author GitEgg
 * @since 2021-05-06
 */
public interface DfsMapper extends BaseMapper<Dfs> {

    /**
    * 查询分布式存储配置表列表
    * @param page
    * @param dfsDTO
    * @return
    */
    Page<DfsDTO> queryDfsList(Page<DfsDTO> page, @Param("dfs") QueryDfsDTO dfsDTO);

    /**
    * 查询分布式存储配置表信息
    * @param dfsDTO
    * @return
    */
    DfsDTO queryDfs(@Param("dfs") QueryDfsDTO dfsDTO);

    /**
     * 查询默认配置
     * @return
     */
    DfsDTO queryDefaultDfs();
}
