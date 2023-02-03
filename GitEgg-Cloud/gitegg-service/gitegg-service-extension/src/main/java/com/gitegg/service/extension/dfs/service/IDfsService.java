package com.gitegg.service.extension.dfs.service;

import java.util.List;

import com.gitegg.service.extension.dfs.entity.Dfs;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import com.gitegg.service.extension.dfs.dto.DfsDTO;
import com.gitegg.service.extension.dfs.dto.CreateDfsDTO;
import com.gitegg.service.extension.dfs.dto.UpdateDfsDTO;
import com.gitegg.service.extension.dfs.dto.QueryDfsDTO;

/**
 * <p>
 * 分布式存储配置表 服务类
 * </p>
 *
 * @author GitEgg
 * @since 2021-05-06
 */
public interface IDfsService extends IService<Dfs> {

    /**
    * 分页查询分布式存储配置表列表
    * @param page
    * @param queryDfsDTO
    * @return
    */
    Page<DfsDTO> queryDfsList(Page<DfsDTO> page, QueryDfsDTO queryDfsDTO);

    /**
    * 查询分布式存储配置表详情
    * @param queryDfsDTO
    * @return
    */
    DfsDTO queryDfs(QueryDfsDTO queryDfsDTO);

    /**
    * 创建分布式存储配置表
    * @param dfs
    * @return
    */
    boolean createDfs(CreateDfsDTO dfs);

    /**
    * 更新分布式存储配置表
    * @param dfs
    * @return
    */
    boolean updateDfs(UpdateDfsDTO dfs);


    /**
     * 更改存储方式默认
     * @param dfsId
     * @return
     */
    boolean updateDfsDefault(Long dfsId);

    /**
    * 删除分布式存储配置表
    * @param dfsId
    * @return
    */
    boolean deleteDfs(Long dfsId);

    /**
    * 批量删除分布式存储配置表
    * @param dfsIds
    * @return
    */
    boolean batchDeleteDfs(List<Long> dfsIds);

    /**
     * 查询默认配置
     * @return
     */
    DfsDTO queryDefaultDfs(QueryDfsDTO queryDfsDTO);
}
