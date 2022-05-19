package com.gitegg.service.extension.justauth.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import com.gitegg.service.extension.justauth.entity.JustAuthSource;
import com.gitegg.service.extension.justauth.mapper.JustAuthSourceMapper;
import com.gitegg.service.extension.justauth.service.IJustAuthSourceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.gitegg.platform.base.util.BeanCopierUtils;
import com.gitegg.service.extension.justauth.dto.JustAuthSourceDTO;
import com.gitegg.service.extension.justauth.dto.CreateJustAuthSourceDTO;
import com.gitegg.service.extension.justauth.dto.UpdateJustAuthSourceDTO;
import com.gitegg.service.extension.justauth.dto.QueryJustAuthSourceDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 租户第三方登录信息配置表 服务实现类
 * </p>
 *
 * @author GitEgg
 * @since 2022-05-19
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class JustAuthSourceServiceImpl extends ServiceImpl<JustAuthSourceMapper, JustAuthSource> implements IJustAuthSourceService {

    private final JustAuthSourceMapper justAuthSourceMapper;

    /**
    * 分页查询租户第三方登录信息配置表列表
    * @param page
    * @param queryJustAuthSourceDTO
    * @return
    */
    @Override
    public Page<JustAuthSourceDTO> queryJustAuthSourceList(Page<JustAuthSourceDTO> page, QueryJustAuthSourceDTO queryJustAuthSourceDTO) {
        Page<JustAuthSourceDTO> justAuthSourceInfoList = justAuthSourceMapper.queryJustAuthSourceList(page, queryJustAuthSourceDTO);
        return justAuthSourceInfoList;
    }

    /**
    * 查询租户第三方登录信息配置表列表
    * @param queryJustAuthSourceDTO
    * @return
    */
    @Override
    public List<JustAuthSourceDTO> queryJustAuthSourceList(QueryJustAuthSourceDTO queryJustAuthSourceDTO) {
        List<JustAuthSourceDTO> justAuthSourceInfoList = justAuthSourceMapper.queryJustAuthSourceList(queryJustAuthSourceDTO);
        return justAuthSourceInfoList;
    }

    /**
    * 查询租户第三方登录信息配置表详情
    * @param queryJustAuthSourceDTO
    * @return
    */
    @Override
    public JustAuthSourceDTO queryJustAuthSource(QueryJustAuthSourceDTO queryJustAuthSourceDTO) {
        JustAuthSourceDTO justAuthSourceDTO = justAuthSourceMapper.queryJustAuthSource(queryJustAuthSourceDTO);
        return justAuthSourceDTO;
    }

    /**
    * 创建租户第三方登录信息配置表
    * @param justAuthSource
    * @return
    */
    @Override
    public boolean createJustAuthSource(CreateJustAuthSourceDTO justAuthSource) {
        JustAuthSource justAuthSourceEntity = BeanCopierUtils.copyByClass(justAuthSource, JustAuthSource.class);
        boolean result = this.save(justAuthSourceEntity);
        return result;
    }

    /**
    * 更新租户第三方登录信息配置表
    * @param justAuthSource
    * @return
    */
    @Override
    public boolean updateJustAuthSource(UpdateJustAuthSourceDTO justAuthSource) {
        JustAuthSource justAuthSourceEntity = BeanCopierUtils.copyByClass(justAuthSource, JustAuthSource.class);
        boolean result = this.updateById(justAuthSourceEntity);
        return result;
    }

    /**
    * 删除租户第三方登录信息配置表
    * @param justAuthSourceId
    * @return
    */
    @Override
    public boolean deleteJustAuthSource(Long justAuthSourceId) {
        boolean result = this.removeById(justAuthSourceId);
        return result;
    }

    /**
    * 批量删除租户第三方登录信息配置表
    * @param justAuthSourceIds
    * @return
    */
    @Override
    public boolean batchDeleteJustAuthSource(List<Long> justAuthSourceIds) {
        boolean result = this.removeByIds(justAuthSourceIds);
        return result;
    }
}
