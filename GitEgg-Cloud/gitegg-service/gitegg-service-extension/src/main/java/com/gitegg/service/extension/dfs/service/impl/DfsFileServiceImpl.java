package com.gitegg.service.extension.dfs.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import com.gitegg.service.extension.dfs.entity.DfsFile;
import com.gitegg.service.extension.dfs.mapper.DfsFileMapper;
import com.gitegg.service.extension.dfs.service.IDfsFileService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.gitegg.platform.base.util.BeanCopierUtils;
import com.gitegg.service.extension.dfs.dto.DfsFileDTO;
import com.gitegg.service.extension.dfs.dto.CreateDfsFileDTO;
import com.gitegg.service.extension.dfs.dto.UpdateDfsFileDTO;
import com.gitegg.service.extension.dfs.dto.QueryDfsFileDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 分布式存储文件记录表 服务实现类
 * </p>
 *
 * @author GitEgg
 * @since 2021-05-08
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DfsFileServiceImpl extends ServiceImpl<DfsFileMapper, DfsFile> implements IDfsFileService {

    private final DfsFileMapper dfsFileMapper;

    /**
    * 分页查询分布式存储文件记录表列表
    * @param page
    * @param queryDfsFileDTO
    * @return
    */
    @Override
    public Page<DfsFileDTO> queryDfsFileList(Page<DfsFileDTO> page, QueryDfsFileDTO queryDfsFileDTO) {
        Page<DfsFileDTO> dfsFileInfoList = dfsFileMapper.queryDfsFileList(page, queryDfsFileDTO);
        return dfsFileInfoList;
    }

    /**
    * 查询分布式存储文件记录表详情
    * @param queryDfsFileDTO
    * @return
    */
    @Override
    public DfsFileDTO queryDfsFile(QueryDfsFileDTO queryDfsFileDTO) {
        DfsFileDTO dfsFileDTO = dfsFileMapper.queryDfsFile(queryDfsFileDTO);
        return dfsFileDTO;
    }

    /**
    * 创建分布式存储文件记录表
    * @param dfsFile
    * @return
    */
    @Override
    public boolean createDfsFile(CreateDfsFileDTO dfsFile) {
        DfsFile dfsFileEntity = BeanCopierUtils.copyByClass(dfsFile, DfsFile.class);
        boolean result = this.save(dfsFileEntity);
        return result;
    }

    /**
    * 更新分布式存储文件记录表
    * @param dfsFile
    * @return
    */
    @Override
    public boolean updateDfsFile(UpdateDfsFileDTO dfsFile) {
        DfsFile dfsFileEntity = BeanCopierUtils.copyByClass(dfsFile, DfsFile.class);
        QueryWrapper<DfsFile> wrapper = new QueryWrapper<>();
        wrapper.eq("id", dfsFileEntity.getId());
        boolean result = this.update(dfsFileEntity, wrapper);
        return result;
    }

    /**
    * 删除分布式存储文件记录表
    * @param dfsFileId
    * @return
    */
    @Override
    public boolean deleteDfsFile(Long dfsFileId) {
        boolean result = this.removeById(dfsFileId);
        return result;
    }

    /**
    * 批量删除分布式存储文件记录表
    * @param dfsFileIds
    * @return
    */
    @Override
    public boolean batchDeleteDfsFile(List<Long> dfsFileIds) {
        boolean result = this.removeByIds(dfsFileIds);
        return result;
    }
}
