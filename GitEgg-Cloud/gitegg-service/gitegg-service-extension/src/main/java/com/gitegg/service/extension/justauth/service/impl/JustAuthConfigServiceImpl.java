package com.gitegg.service.extension.justauth.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import com.gitegg.service.extension.justauth.entity.JustAuthConfig;
import com.gitegg.service.extension.justauth.mapper.JustAuthConfigMapper;
import com.gitegg.service.extension.justauth.service.IJustAuthConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.gitegg.platform.base.util.BeanCopierUtils;
import com.gitegg.service.extension.justauth.dto.JustAuthConfigDTO;
import com.gitegg.service.extension.justauth.dto.CreateJustAuthConfigDTO;
import com.gitegg.service.extension.justauth.dto.UpdateJustAuthConfigDTO;
import com.gitegg.service.extension.justauth.dto.QueryJustAuthConfigDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 租户第三方登录功能配置表 服务实现类
 * </p>
 *
 * @author GitEgg
 * @since 2022-05-16
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class JustAuthConfigServiceImpl extends ServiceImpl<JustAuthConfigMapper, JustAuthConfig> implements IJustAuthConfigService {

    private final JustAuthConfigMapper justAuthConfigMapper;

    /**
    * 分页查询租户第三方登录功能配置表列表
    * @param page
    * @param queryJustAuthConfigDTO
    * @return
    */
    @Override
    public Page<JustAuthConfigDTO> queryJustAuthConfigList(Page<JustAuthConfigDTO> page, QueryJustAuthConfigDTO queryJustAuthConfigDTO) {
        Page<JustAuthConfigDTO> justAuthConfigInfoList = justAuthConfigMapper.queryJustAuthConfigList(page, queryJustAuthConfigDTO);
        return justAuthConfigInfoList;
    }

    /**
    * 查询租户第三方登录功能配置表列表
    * @param queryJustAuthConfigDTO
    * @return
    */
    @Override
    public List<JustAuthConfigDTO> queryJustAuthConfigList(QueryJustAuthConfigDTO queryJustAuthConfigDTO) {
        List<JustAuthConfigDTO> justAuthConfigInfoList = justAuthConfigMapper.queryJustAuthConfigList(queryJustAuthConfigDTO);
        return justAuthConfigInfoList;
    }

    /**
    * 查询租户第三方登录功能配置表详情
    * @param queryJustAuthConfigDTO
    * @return
    */
    @Override
    public JustAuthConfigDTO queryJustAuthConfig(QueryJustAuthConfigDTO queryJustAuthConfigDTO) {
        JustAuthConfigDTO justAuthConfigDTO = justAuthConfigMapper.queryJustAuthConfig(queryJustAuthConfigDTO);
        return justAuthConfigDTO;
    }

    /**
    * 创建租户第三方登录功能配置表
    * @param justAuthConfig
    * @return
    */
    @Override
    public boolean createJustAuthConfig(CreateJustAuthConfigDTO justAuthConfig) {
        JustAuthConfig justAuthConfigEntity = BeanCopierUtils.copyByClass(justAuthConfig, JustAuthConfig.class);
        boolean result = this.save(justAuthConfigEntity);
        return result;
    }

    /**
    * 更新租户第三方登录功能配置表
    * @param justAuthConfig
    * @return
    */
    @Override
    public boolean updateJustAuthConfig(UpdateJustAuthConfigDTO justAuthConfig) {
        JustAuthConfig justAuthConfigEntity = BeanCopierUtils.copyByClass(justAuthConfig, JustAuthConfig.class);
        boolean result = this.updateById(justAuthConfigEntity);
        return result;
    }

    /**
    * 删除租户第三方登录功能配置表
    * @param justAuthConfigId
    * @return
    */
    @Override
    public boolean deleteJustAuthConfig(Long justAuthConfigId) {
        boolean result = this.removeById(justAuthConfigId);
        return result;
    }

    /**
    * 批量删除租户第三方登录功能配置表
    * @param justAuthConfigIds
    * @return
    */
    @Override
    public boolean batchDeleteJustAuthConfig(List<Long> justAuthConfigIds) {
        boolean result = this.removeByIds(justAuthConfigIds);
        return result;
    }
}
