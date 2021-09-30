package com.gitegg.service.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gitegg.service.system.entity.Organization;

import java.util.List;

/**
 *
 * @author gitegg
 */
public interface IOrganizationService extends IService<Organization> {

    /**
     * 查询机构列表
     * @param parentId
     * @return
     */
    List<Organization> queryOrganizationByParentId(Long parentId);

    /**
     * 查询机构列表，不组装父子节点
     * @param parentId
     * @return
     */
    List<Organization> queryOrgList(Long parentId);

    /**
     * 创建组织
     * @param organization
     * @return
     */
    boolean createOrganization(Organization organization);

    /**
     * 更新组织
     * @param organization
     * @return
     */
    boolean updateOrganization(Organization organization);

    /**
     * 删除组织
     * @param organizationId
     * @return
     */
    boolean deleteOrganization(Long organizationId);
}
