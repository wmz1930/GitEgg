package com.gitegg.service.extension.justauth.service;

import java.util.List;

import com.gitegg.service.extension.justauth.entity.JustAuthSocial;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import com.gitegg.service.extension.justauth.dto.JustAuthSocialDTO;
import com.gitegg.service.extension.justauth.dto.CreateJustAuthSocialDTO;
import com.gitegg.service.extension.justauth.dto.UpdateJustAuthSocialDTO;
import com.gitegg.service.extension.justauth.dto.QueryJustAuthSocialDTO;

/**
 * <p>
 * 租户第三方登录功能配置表 服务类
 * </p>
 *
 * @author GitEgg
 * @since 2022-05-19
 */
public interface IJustAuthSocialService extends IService<JustAuthSocial> {

    /**
    * 分页查询租户第三方登录功能配置表列表
    * @param page
    * @param queryJustAuthSocialDTO
    * @return
    */
    Page<JustAuthSocialDTO> queryJustAuthSocialList(Page<JustAuthSocialDTO> page, QueryJustAuthSocialDTO queryJustAuthSocialDTO);

    /**
    * 查询租户第三方登录功能配置表列表
    * @param queryJustAuthSocialDTO
    * @return
    */
    List<JustAuthSocialDTO> queryJustAuthSocialList(QueryJustAuthSocialDTO queryJustAuthSocialDTO);

    /**
    * 查询租户第三方登录功能配置表详情
    * @param queryJustAuthSocialDTO
    * @return
    */
    JustAuthSocialDTO queryJustAuthSocial(QueryJustAuthSocialDTO queryJustAuthSocialDTO);

    /**
    * 创建租户第三方登录功能配置表
    * @param justAuthSocial
    * @return
    */
    boolean createJustAuthSocial(CreateJustAuthSocialDTO justAuthSocial);

    /**
    * 更新租户第三方登录功能配置表
    * @param justAuthSocial
    * @return
    */
    boolean updateJustAuthSocial(UpdateJustAuthSocialDTO justAuthSocial);

    /**
    * 删除租户第三方登录功能配置表
    * @param justAuthSocialId
    * @return
    */
    boolean deleteJustAuthSocial(Long justAuthSocialId);

    /**
    * 批量删除租户第三方登录功能配置表
    * @param justAuthSocialIds
    * @return
    */
    boolean batchDeleteJustAuthSocial(List<Long> justAuthSocialIds);
}
