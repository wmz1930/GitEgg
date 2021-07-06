package com.gitegg.service.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gitegg.service.system.entity.Organization;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 组织表 Mapper 接口
 * </p>
 *
 * @author gitegg
 * @since 2018-05-19
 */
public interface OrganizationMapper extends BaseMapper<Organization> {

    /**
     * 查询组织机构树
     * @param parentId
     * @return
     */
    List<Organization> queryOrganizationTreeProc(Long parentId);

    /**
     * 查询组织机构树
     * @param organization
     * @return
     */
    List<Organization> selectOrganizationChidlren(@Param("org") Organization organization);


}
