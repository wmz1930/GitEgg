package com.gitegg.service.extension.justauth.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import com.gitegg.service.extension.justauth.entity.JustAuthSocial;
import com.gitegg.service.extension.justauth.mapper.JustAuthSocialMapper;
import com.gitegg.service.extension.justauth.service.IJustAuthSocialService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.gitegg.platform.base.util.BeanCopierUtils;
import com.gitegg.service.extension.justauth.dto.JustAuthSocialDTO;
import com.gitegg.service.extension.justauth.dto.CreateJustAuthSocialDTO;
import com.gitegg.service.extension.justauth.dto.UpdateJustAuthSocialDTO;
import com.gitegg.service.extension.justauth.dto.QueryJustAuthSocialDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 租户第三方登录功能配置表 服务实现类
 * </p>
 *
 * @author GitEgg
 * @since 2022-05-19
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class JustAuthSocialServiceImpl extends ServiceImpl<JustAuthSocialMapper, JustAuthSocial> implements IJustAuthSocialService {

    private final JustAuthSocialMapper justAuthSocialMapper;

    /**
    * 分页查询租户第三方登录功能配置表列表
    * @param page
    * @param queryJustAuthSocialDTO
    * @return
    */
    @Override
    public Page<JustAuthSocialDTO> queryJustAuthSocialList(Page<JustAuthSocialDTO> page, QueryJustAuthSocialDTO queryJustAuthSocialDTO) {
        Page<JustAuthSocialDTO> justAuthSocialInfoList = justAuthSocialMapper.queryJustAuthSocialList(page, queryJustAuthSocialDTO);
        return justAuthSocialInfoList;
    }

    /**
    * 查询租户第三方登录功能配置表列表
    * @param queryJustAuthSocialDTO
    * @return
    */
    @Override
    public List<JustAuthSocialDTO> queryJustAuthSocialList(QueryJustAuthSocialDTO queryJustAuthSocialDTO) {
        List<JustAuthSocialDTO> justAuthSocialInfoList = justAuthSocialMapper.queryJustAuthSocialList(queryJustAuthSocialDTO);
        return justAuthSocialInfoList;
    }

    /**
    * 查询租户第三方登录功能配置表详情
    * @param queryJustAuthSocialDTO
    * @return
    */
    @Override
    public JustAuthSocialDTO queryJustAuthSocial(QueryJustAuthSocialDTO queryJustAuthSocialDTO) {
        JustAuthSocialDTO justAuthSocialDTO = justAuthSocialMapper.queryJustAuthSocial(queryJustAuthSocialDTO);
        return justAuthSocialDTO;
    }

    /**
    * 创建租户第三方登录功能配置表
    * @param justAuthSocial
    * @return
    */
    @Override
    public boolean createJustAuthSocial(CreateJustAuthSocialDTO justAuthSocial) {
        JustAuthSocial justAuthSocialEntity = BeanCopierUtils.copyByClass(justAuthSocial, JustAuthSocial.class);
        boolean result = this.save(justAuthSocialEntity);
        return result;
    }

    /**
    * 更新租户第三方登录功能配置表
    * @param justAuthSocial
    * @return
    */
    @Override
    public boolean updateJustAuthSocial(UpdateJustAuthSocialDTO justAuthSocial) {
        JustAuthSocial justAuthSocialEntity = BeanCopierUtils.copyByClass(justAuthSocial, JustAuthSocial.class);
        boolean result = this.updateById(justAuthSocialEntity);
        return result;
    }

    /**
    * 删除租户第三方登录功能配置表
    * @param justAuthSocialId
    * @return
    */
    @Override
    public boolean deleteJustAuthSocial(Long justAuthSocialId) {
        boolean result = this.removeById(justAuthSocialId);
        return result;
    }

    /**
    * 批量删除租户第三方登录功能配置表
    * @param justAuthSocialIds
    * @return
    */
    @Override
    public boolean batchDeleteJustAuthSocial(List<Long> justAuthSocialIds) {
        boolean result = this.removeByIds(justAuthSocialIds);
        return result;
    }
}
