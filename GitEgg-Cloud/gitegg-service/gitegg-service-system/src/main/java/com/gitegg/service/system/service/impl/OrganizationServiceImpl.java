package com.gitegg.service.system.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gitegg.platform.base.constant.GitEggConstant;
import com.gitegg.service.system.entity.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitegg.boot.system.dto.QueryOrganizationDTO;
import com.gitegg.platform.base.constant.GitEggConstant;
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
                parentId = GitEggConstant.PARENT_ID;
            }
            Organization organizationParent = new Organization();
            organizationParent.setParentId(parentId);
            orgList = organizationMapper.selectOrganizationChildren(organizationParent);
        } catch (Exception e) {
            log.error("查询组织树失败:", e);
            throw new BusinessException("查询组织树失败");
        }
        return orgList;
    }

    @Override
    public List<Organization> queryOrganizationByParentId(Organization organization) {
        List<Organization> orgs;
        try {
            if (null == organization.getParentId()) {
                organization.setParentId(GitEggConstant.PARENT_ID);
            }
            List<Organization> orgList = organizationMapper.selectOrganizationChildren(organization);
            Map<Long, Organization> organizationMap = new HashMap<>();
            orgs = this.assembleOrganizationTree(orgList, organizationMap);
        } catch (Exception e) {
            log.error("查询组织树失败:", e);
            throw new BusinessException("查询组织树失败");
        }
        return orgs;
    }

    @Override
    public List<Organization> queryOrganizationList(Organization organization) {
        try {
            if (null == organization.getParentId()) {
                organization.setParentId(GitEggConstant.PARENT_ID);
            }
            return organizationMapper.selectOrganizationList(organization);
        } catch (Exception e) {
            log.error("查询组织树失败:", e);
            throw new BusinessException("查询组织树失败");
        }
    }

    @Override
    public boolean createOrganization(Organization organization) {
        LambdaQueryWrapper<Organization> ew = new LambdaQueryWrapper<>();
        ew.eq(Organization::getOrganizationName, organization.getOrganizationName()).or().eq(Organization::getOrganizationKey, organization.getOrganizationKey());
        List<Organization> organizationList = list(ew);
        if (!CollectionUtils.isEmpty(organizationList)) {
            throw new BusinessException("组织名称或组织标识已经存在");
        }

        if(null != organization.getParentId() && organization.getParentId().longValue() != GitEggConstant.PARENT_ID.longValue())
        {
            Organization organizationParent = this.getById(organization.getParentId());
            String parentAncestors = organizationParent.getAncestors();
            organization.setAncestors(parentAncestors + StrUtil.COMMA + organization.getParentId());
        }
        else
        {
            organization.setAncestors(GitEggConstant.PARENT_ID.toString());
        }

        boolean result = save(organization);
        return result;
    }

    @Override
    public boolean updateOrganization(Organization organization) {
        LambdaQueryWrapper<Organization> ew = new LambdaQueryWrapper<>();
        ew.ne(Organization::getId, organization.getId()).and(e -> e.eq(Organization::getOrganizationName, organization.getOrganizationName()).or().eq(Organization::getOrganizationKey, organization.getOrganizationKey()));
        List<Organization> organizationList = list(ew);
        if (!CollectionUtils.isEmpty(organizationList)) {
            throw new BusinessException("组织名称或组织标识已经存在");
        }

        //判断是否修改了父级组织ID，如果改了，那么需要修改本机构及本机构下所有的ancestors字段
        Organization organizationOld = this.getById(organization.getId());
        if (null != organizationOld && null != organizationOld.getParentId()
                && null != organization.getParentId() && organizationOld.getParentId().longValue() != organization.getParentId().longValue())
        {
            Organization organizationParentNew = this.getById(organization.getParentId());
            //新的父级组合
            String ancestorsNew = null == organizationParentNew ? GitEggConstant.PARENT_ID.toString() : organizationParentNew.getAncestors() + StrUtil.COMMA + organization.getParentId();
            //设置组织新的父级组合
            organization.setAncestors(ancestorsNew);
            //旧的父级组合
            String ancestorsOld = organizationOld.getAncestors();

            Organization organizationParent = new Organization();
            organizationParent.setParentId(organization.getId());
            //只查子节点
            organizationParent.setIsLeaf(GitEggConstant.Number.ONE);
            List<Organization> orgChildrenList = organizationMapper.selectOrganizationChildren(organizationParent);
            if (!CollectionUtils.isEmpty(orgChildrenList)) {
                orgChildrenList = orgChildrenList.stream().map(org -> {org.setAncestors(org.getAncestors().replaceFirst(ancestorsOld, ancestorsNew)); return org;}).collect(Collectors.toList());
            }
            this.updateBatchById(orgChildrenList);
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
        Organization organizationParent = new Organization();
        organizationParent.setParentId(organizationId);
        List<Organization> orgChildrenList = organizationMapper.selectOrganizationChildren(organizationParent);
        if (!CollectionUtils.isEmpty(orgChildrenList))
        {
            List<Long> orgIds = orgChildrenList.stream().map(Organization::getId).collect(Collectors.toList());
            result = removeByIds(orgIds);
        }
        return result;
    }

    /**
     * 组装子父级目录
     * @param organizationList
     * @param organizationMap
     * @return
     */
    private List<Organization> assembleOrganizationTree(List<Organization> organizationList, Map<Long, Organization> organizationMap)
    {
        List<Organization> organizations = new ArrayList<>();
        for (Organization organization : organizationList) {
            organizationMap.put(organization.getId(), organization);
        }
        for (Organization organization : organizationList) {
            Long treePId = organization.getParentId();
            Organization organizationTree = organizationMap.get(treePId);
            if (null != organizationTree && !organization.equals(organizationTree)) {
                List<Organization> nodes = organizationTree.getChildren();
                if (null == nodes) {
                    nodes = new ArrayList<>();
                    organizationTree.setChildren(nodes);
                }
                nodes.add(organization);
            } else {
                organizations.add(organization);
            }
        }
        return organizations;
    }
}
