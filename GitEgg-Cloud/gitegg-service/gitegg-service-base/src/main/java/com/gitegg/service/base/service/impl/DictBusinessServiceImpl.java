package com.gitegg.service.base.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitegg.platform.base.constant.GitEggConstant;
import com.gitegg.platform.base.exception.BusinessException;
import com.gitegg.service.base.dto.DictBusinessDTO;
import com.gitegg.service.base.dto.QueryDictBusinessDTO;
import com.gitegg.service.base.entity.DictBusiness;
import com.gitegg.service.base.mapper.DictBusinessMapper;
import com.gitegg.service.base.service.IDictBusinessService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 数据字典表 服务实现类
 * </p>
 *
 * @author GitEgg
 * @since 2018-10-28
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Service
public class DictBusinessServiceImpl extends ServiceImpl<DictBusinessMapper, DictBusiness> implements IDictBusinessService {

    private final DictBusinessMapper dictBusinessMapper;

    @Override
    public Page<DictBusinessDTO> selectDictBusinessList(Page<DictBusinessDTO> page, QueryDictBusinessDTO dictBusiness) {
        Page<DictBusinessDTO> pageDictInfo = dictBusinessMapper.selectDictBusinessList(page, dictBusiness);
        return pageDictInfo;
    }

    @Override
    public List<DictBusinessDTO> selectDictBusinessList(QueryDictBusinessDTO dictBusiness) {
        List<DictBusinessDTO> dictInfoList = dictBusinessMapper.selectDictBusinessList(dictBusiness);
        return dictInfoList;
    }

    @Override
    public boolean createDictBusiness(DictBusiness dictBusiness) {
        QueryWrapper<DictBusiness> qwDictCreate = new QueryWrapper<>();
        qwDictCreate.eq("dict_code", dictBusiness.getDictCode()).eq("parent_id", dictBusiness.getParentId());
        List<DictBusiness> dictBusinessList = list(qwDictCreate);
        if (!CollectionUtils.isEmpty(dictBusinessList)) {
            throw new BusinessException("字典值重复");
        }

        if(null != dictBusiness.getParentId() && dictBusiness.getParentId().longValue() != GitEggConstant.PARENT_ID.longValue())
        {
            DictBusiness dictBusinessParent = this.getById(dictBusiness.getParentId());
            String dictAncestors = dictBusinessParent.getAncestors();
            dictBusiness.setAncestors(dictAncestors + StrUtil.COMMA + dictBusiness.getParentId());
        }
        else
        {
            dictBusiness.setAncestors(GitEggConstant.PARENT_ID.toString());
        }
        boolean result = save(dictBusiness);
        return result;
    }

    @Override
    public boolean updateDictBusiness(DictBusiness dictBusiness) {
        QueryWrapper<DictBusiness> ew = new QueryWrapper<>();
        ew.eq("dict_code", dictBusiness.getDictCode()).eq("parent_id", dictBusiness.getParentId()).ne("id", dictBusiness.getId());
        List<DictBusiness> dictBusinessList = list(ew);
        if (!CollectionUtils.isEmpty(dictBusinessList)) {
            throw new BusinessException("字典值重复");
        }

        //判断是否修改了父级ID，如果改了，那么需要更新ancestors字段
        DictBusiness dictBusinessOld = this.getById(dictBusiness.getId());
        if (null != dictBusinessOld && null != dictBusinessOld.getParentId()
                && null != dictBusiness.getParentId() && dictBusinessOld.getParentId().longValue() != dictBusiness.getParentId().longValue())
        {
            DictBusiness dictBusinessParentNew = this.getById(dictBusiness.getParentId());
            //新的父级组合
            String ancestorsNew = null == dictBusinessParentNew ? GitEggConstant.PARENT_ID.toString() : dictBusinessParentNew.getAncestors() + StrUtil.COMMA + dictBusiness.getParentId();
            //设置新的父级组合
            dictBusiness.setAncestors(ancestorsNew);
            //旧的父级组合
            String ancestorsOld = dictBusinessOld.getAncestors();

            QueryDictBusinessDTO dictBusinessParent = new QueryDictBusinessDTO();
            dictBusinessParent.setParentId(dictBusiness.getId());
            //只查子节点
            dictBusinessParent.setIsLeaf(GitEggConstant.Number.ONE);
            List<DictBusiness> dictBusinessChildrenList = dictBusinessMapper.selectDictBusinessChildren(dictBusinessParent);
            if (!CollectionUtils.isEmpty(dictBusinessChildrenList)) {
                dictBusinessChildrenList = dictBusinessChildrenList.stream().map(dictBusinessA -> {dictBusinessA.setAncestors(dictBusinessA.getAncestors().replaceFirst(ancestorsOld, ancestorsNew)); return dictBusinessA;}).collect(Collectors.toList());
            }
            this.updateBatchById(dictBusinessChildrenList);
        }

        boolean result = updateById(dictBusiness);
        return result;
    }

    @Override
    public boolean deleteDictBusiness(Long dictBusinessId) {
        QueryWrapper<DictBusiness> wpd = new QueryWrapper<>();
        wpd.and(e -> e.eq("id", dictBusinessId).or().eq("parent_id", dictBusinessId));
        boolean result = remove(wpd);
        return result;
    }

    @Override
    public boolean batchDeleteDictBusiness(List<Long> dictBusinessIds) {
        QueryWrapper<DictBusiness> wpd = new QueryWrapper<>();
        wpd.and(e -> e.in("id", dictBusinessIds).or().in("parent_id", dictBusinessIds));
        boolean result = remove(wpd);
        return result;
    }

    @Override
    public List<DictBusiness> queryDictBusinessListByParentCode(String dictBusinessCode) {
        QueryWrapper<DictBusiness> parentEw = new QueryWrapper<>();
        parentEw.eq("dict_status", "1").eq("dict_code", dictBusinessCode);
        DictBusiness dictBusiness = getOne(parentEw);
        List<DictBusiness> dictList = null;
        if (null != dictBusiness) {
            QueryWrapper<DictBusiness> ewBusiness = new QueryWrapper<>();
            ewBusiness.eq("dict_status", "1").eq("parent_id", dictBusiness.getId());
            dictList = list(ewBusiness);
        }

        return dictList;
    }

    @Override
    public List<DictBusiness> queryDictBusinessTreeByParentId(Long parentId) {
        if (null == parentId) {
            parentId = 0L;
        }
        QueryDictBusinessDTO dictBusinessQuery = new QueryDictBusinessDTO();
        dictBusinessQuery.setParentId(parentId);
        List<DictBusiness> dictBusinessList = dictBusinessMapper.selectDictBusinessChildren(dictBusinessQuery);
        List<DictBusiness> dictBusinessResultList = new ArrayList<>();
        Map<Long, DictBusiness> dictBusinessMap = new HashMap<>();
        // 组装子父级目录
        for (DictBusiness dictBusiness : dictBusinessList) {
            dictBusinessMap.put(dictBusiness.getId(), dictBusiness);
        }
        for (DictBusiness dictBusiness : dictBusinessList) {
            Long treePId = dictBusiness.getParentId();
            DictBusiness pTreev = dictBusinessMap.get(treePId);
            if (null != pTreev && !dictBusiness.equals(pTreev)) {
                List<DictBusiness> nodes = pTreev.getChildren();
                if (null == nodes) {
                    nodes = new ArrayList<>();
                    pTreev.setChildren(nodes);
                }
                nodes.add(dictBusiness);
            } else {
                dictBusinessResultList.add(dictBusiness);
            }
        }
        return dictBusinessResultList;
    }
}
