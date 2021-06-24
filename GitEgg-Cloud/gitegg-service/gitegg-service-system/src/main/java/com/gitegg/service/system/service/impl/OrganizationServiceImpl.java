package com.gitegg.service.system.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitegg.platform.base.exception.BusinessException;
import com.gitegg.service.system.entity.Organization;
import com.gitegg.service.system.mapper.OrganizationMapper;
import com.gitegg.service.system.service.IOrganizationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author gitegg
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class OrganizationServiceImpl extends ServiceImpl<OrganizationMapper, Organization>
        implements IOrganizationService {

    private final OrganizationMapper organizationMapper;

    /**
     * queryOrgList
     * 
     * @Title: queryOrgList
     * @Description: 查询所有的组织结构树
     * @param parentId
     * @return List<Organization>
     */
    @Override
    public List<Organization> queryOrgList(Long parentId) {
        List<Organization> orgList;
        try {
            if (null == parentId) {
                parentId = 0l;
            }
            orgList = organizationMapper.queryOrganizationTreeProc(parentId);
        } catch (Exception e) {
            log.error("查询组织树失败:", e);
            throw new BusinessException("查询组织树失败");
        }
        return orgList;
    }

    @Override
    public List<Organization> queryOrganizationByPanentId(Long parentId) {
        if (null == parentId) {
            parentId = 0l;
        }
        List<Organization> orgs = new ArrayList<>();
        try {
            List<Organization> orgList = organizationMapper.queryOrganizationTreeProc(parentId);
            Map<Long, Organization> organizationMap = new HashMap<>();
            // 组装子父级目录
            for (Organization organization : orgList) {
                organizationMap.put(organization.getId(), organization);
            }
            for (Organization organization : orgList) {
                Long treePId = organization.getParentId();
                Organization pTreev = organizationMap.get(treePId);
                if (null != pTreev && !organization.equals(pTreev)) {
                    List<Organization> nodes = pTreev.getChildren();
                    if (null == nodes) {
                        nodes = new ArrayList<Organization>();
                        pTreev.setChildren(nodes);
                    }
                    nodes.add(organization);
                } else {
                    orgs.add(organization);
                }
            }
        } catch (Exception e) {
            log.error("查询组织树失败:", e);
            throw new BusinessException("查询组织树失败");
        }
        return orgs;
    }

    @Override
    public boolean createOrganization(Organization organization) {
        QueryWrapper<Organization> ew = new QueryWrapper<>();
        ew.eq("organization_name", organization.getOrganizationName()).or().eq("organization_key", organization.getOrganizationKey());
        List<Organization> organizationList = list(ew);
        if (!CollectionUtils.isEmpty(organizationList)) {
            throw new BusinessException("组织名称或组织标识已经存在");
        }
        boolean result = save(organization);
        return result;
    }

    @Override
    public boolean updateOrganization(Organization organization) {
        QueryWrapper<Organization> ew = new QueryWrapper<>();
        ew.ne("id", organization.getId()).and(e -> e.eq("organization_name", organization.getOrganizationName()).or().eq("organization_key", organization.getOrganizationKey()));
        List<Organization> organizationList = list(ew);
        if (!CollectionUtils.isEmpty(organizationList)) {
            throw new BusinessException("组织名称或组织标识已经存在");
        }
        boolean result = updateById(organization);
        return result;
    }

    @Override
    public boolean deleteOrganization(Long organizationId) {
        boolean result = false;
        if (null == organizationId)
        {
            throw new BusinessException("请选择要删除的组织");
        }
        List<Organization> orgList = organizationMapper.queryOrganizationTreeProc(organizationId);
        List<Long> orgIds = new ArrayList<>();
        for (Organization org: orgList)
        {
            orgIds.add(org.getId());
        }
        if (!CollectionUtils.isEmpty(orgIds))
        {
            result = removeByIds(orgIds);
        }
        return result;
    }
}
