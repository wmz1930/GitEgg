package com.gitegg.service.extension.justauth.mapper;

import com.gitegg.service.extension.justauth.entity.JustAuthSocial;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.gitegg.service.extension.justauth.dto.JustAuthSocialDTO;
import com.gitegg.service.extension.justauth.dto.QueryJustAuthSocialDTO;

import java.util.List;

/**
 * <p>
 * 租户第三方登录功能配置表 Mapper 接口
 * </p>
 *
 * @author GitEgg
 * @since 2022-05-19
 */
public interface JustAuthSocialMapper extends BaseMapper<JustAuthSocial> {

    /**
    * 分页查询租户第三方登录功能配置表列表
    * @param page
    * @param justAuthSocialDTO
    * @return
    */
    Page<JustAuthSocialDTO> queryJustAuthSocialList(Page<JustAuthSocialDTO> page, @Param("justAuthSocial") QueryJustAuthSocialDTO justAuthSocialDTO);

    /**
    * 查询租户第三方登录功能配置表列表
    * @param justAuthSocialDTO
    * @return
    */
    List<JustAuthSocialDTO> queryJustAuthSocialList(@Param("justAuthSocial") QueryJustAuthSocialDTO justAuthSocialDTO);

    /**
    * 查询租户第三方登录功能配置表信息
    * @param justAuthSocialDTO
    * @return
    */
    JustAuthSocialDTO queryJustAuthSocial(@Param("justAuthSocial") QueryJustAuthSocialDTO justAuthSocialDTO);
}
