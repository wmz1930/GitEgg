package com.gitegg.service.extension.dfs.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gitegg.platform.base.constant.GitEggConstant;
import org.springframework.beans.factory.annotation.Autowired;
import com.gitegg.service.extension.dfs.entity.Dfs;
import com.gitegg.service.extension.dfs.mapper.DfsMapper;
import com.gitegg.service.extension.dfs.service.IDfsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.gitegg.platform.base.util.BeanCopierUtils;
import com.gitegg.service.extension.dfs.dto.DfsDTO;
import com.gitegg.service.extension.dfs.dto.CreateDfsDTO;
import com.gitegg.service.extension.dfs.dto.UpdateDfsDTO;
import com.gitegg.service.extension.dfs.dto.QueryDfsDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 分布式存储配置表 服务实现类
 * </p>
 *
 * @author GitEgg
 * @since 2021-05-06
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DfsServiceImpl extends ServiceImpl<DfsMapper, Dfs> implements IDfsService {

    private final DfsMapper dfsMapper;

    /**
    * 分页查询分布式存储配置表列表
    * @param page
    * @param queryDfsDTO
    * @return
    */
    @Override
    public Page<DfsDTO> queryDfsList(Page<DfsDTO> page, QueryDfsDTO queryDfsDTO) {
        Page<DfsDTO> dfsInfoList = dfsMapper.queryDfsList(page, queryDfsDTO);
        return dfsInfoList;
    }

    /**
    * 查询分布式存储配置表详情
    * @param queryDfsDTO
    * @return
    */
    @Override
    public DfsDTO queryDfs(QueryDfsDTO queryDfsDTO) {
        DfsDTO dfsDTO = dfsMapper.queryDfs(queryDfsDTO);
        return dfsDTO;
    }

    /**
    * 创建分布式存储配置表
    * @param dfs
    * @return
    */
    @Override
    public boolean createDfs(CreateDfsDTO dfs) {
        Dfs dfsEntity = BeanCopierUtils.copyByClass(dfs, Dfs.class);
        boolean result = this.save(dfsEntity);
        return result;
    }

    /**
    * 更新分布式存储配置表
    * @param dfs
    * @return
    */
    @Override
    public boolean updateDfs(UpdateDfsDTO dfs) {
        Dfs dfsEntity = BeanCopierUtils.copyByClass(dfs, Dfs.class);
        QueryWrapper<Dfs> wrapper = new QueryWrapper<>();
        wrapper.eq("id", dfsEntity.getId());
        boolean result = this.update(dfsEntity, wrapper);
        return result;
    }

    /**
     * 更新分布式存储配置默认
     * @param dfsId
     * @return
     */
    @Override
    public boolean updateDfsDefault(Long dfsId) {
        //将选择的记录改为默认，其他值改为非默认
        LambdaUpdateWrapper<Dfs> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(Dfs::getId, dfsId).set(Dfs::getDfsDefault, GitEggConstant.ENABLE);
        boolean result = this.update(lambdaUpdateWrapper);
        if (result) {
            LambdaUpdateWrapper<Dfs> lambdaUpdateWrapperNe = new LambdaUpdateWrapper<>();
            lambdaUpdateWrapperNe.ne(Dfs::getId, dfsId).set(Dfs::getDfsDefault, GitEggConstant.DISABLE);
            result = this.update(lambdaUpdateWrapperNe);
        }
        return result;
    }

    /**
    * 删除分布式存储配置表
    * @param dfsId
    * @return
    */
    @Override
    public boolean deleteDfs(Long dfsId) {
        boolean result = this.removeById(dfsId);
        return result;
    }

    /**
    * 批量删除分布式存储配置表
    * @param dfsIds
    * @return
    */
    @Override
    public boolean batchDeleteDfs(List<Long> dfsIds) {
        boolean result = this.removeByIds(dfsIds);
        return result;
    }

    /**
     * 查询默认配置
     * @return
     */
    @Override
    public DfsDTO queryDefaultDfs() {
        DfsDTO dfsDTO = dfsMapper.queryDefaultDfs();
        return dfsDTO;
    }
}
