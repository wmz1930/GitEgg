package com.gitegg.service.base.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import cn.hutool.core.util.StrUtil;
import com.gitegg.platform.base.constant.GitEggConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitegg.platform.base.exception.BusinessException;
import com.gitegg.service.base.dto.DictDTO;
import com.gitegg.service.base.dto.QueryDictDTO;
import com.gitegg.service.base.entity.Dict;
import com.gitegg.service.base.mapper.DictMapper;
import com.gitegg.service.base.service.IDictService;

import lombok.RequiredArgsConstructor;

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
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements IDictService {

    private final DictMapper dictMapper;

    @Override
    public Page<DictDTO> selectDictList(Page<DictDTO> page, QueryDictDTO dict) {
        Page<DictDTO> pageDictInfo = dictMapper.selectDictList(page, dict);
        return pageDictInfo;
    }

    @Override
    public List<DictDTO> selectDictList(QueryDictDTO dict) {
        List<DictDTO> dictInfoList = dictMapper.selectDictList(dict);
        return dictInfoList;
    }

    @Override
    public boolean createDict(Dict dict) {
        QueryWrapper<Dict> qwDictCreate = new QueryWrapper<>();
        qwDictCreate.eq("dict_code", dict.getDictCode()).eq("parent_id", dict.getParentId());
        List<Dict> dictList = list(qwDictCreate);
        if (!CollectionUtils.isEmpty(dictList)) {
            throw new BusinessException("字典值重复");
        }

        if(null != dict.getParentId() && dict.getParentId().longValue() != GitEggConstant.PARENT_ID.longValue())
        {
            Dict dictParent = this.getById(dict.getParentId());
            String dictAncestors = dictParent.getAncestors();
            dict.setAncestors(dictAncestors + StrUtil.COMMA + dict.getParentId());
        }
        else
        {
            dict.setAncestors(GitEggConstant.PARENT_ID.toString());
        }

        boolean result = save(dict);
        return result;
    }

    @Override
    public boolean updateDict(Dict dict) {
        QueryWrapper<Dict> ew = new QueryWrapper<>();
        ew.eq("dict_code", dict.getDictCode()).eq("parent_id", dict.getParentId()).ne("id", dict.getId());
        List<Dict> dictList = list(ew);
        if (!CollectionUtils.isEmpty(dictList)) {
            throw new BusinessException("字典值重复");
        }

        //判断是否修改了父级ID，如果改了，那么需要更新ancestors字段
        Dict dictOld = this.getById(dict.getId());
        if (null != dictOld && null != dictOld.getParentId()
                && null != dict.getParentId() && dictOld.getParentId().longValue() != dict.getParentId().longValue())
        {
            Dict dictParentNew = this.getById(dict.getParentId());
            //新的父级组合
            String ancestorsNew = null == dictParentNew ? GitEggConstant.PARENT_ID.toString() : dictParentNew.getAncestors() + StrUtil.COMMA + dict.getParentId();
            //设置新的父级组合
            dict.setAncestors(ancestorsNew);
            //旧的父级组合
            String ancestorsOld = dictOld.getAncestors();

            QueryDictDTO dictParent = new QueryDictDTO();
            dictParent.setParentId(dict.getId());
            //只查子节点
            dictParent.setIsLeaf(GitEggConstant.Number.ONE);
            List<Dict> dictChildrenList = dictMapper.selectDictChildren(dictParent);
            if (!CollectionUtils.isEmpty(dictChildrenList)) {
                dictChildrenList = dictChildrenList.stream().map(dictA -> {dictA.setAncestors(dictA.getAncestors().replaceFirst(ancestorsOld, ancestorsNew)); return dictA;}).collect(Collectors.toList());
            }
            this.updateBatchById(dictChildrenList);
        }

        boolean result = updateById(dict);
        return result;
    }

    @Override
    public boolean deleteDict(Long dictId) {
        QueryWrapper<Dict> wpd = new QueryWrapper<>();
        wpd.and(e -> e.eq("id", dictId).or().eq("parent_id", dictId));
        boolean result = remove(wpd);
        return result;
    }

    @Override
    public boolean batchDeleteDict(List<Long> dictIds) {
        QueryWrapper<Dict> wpd = new QueryWrapper<>();
        wpd.and(e -> e.in("id", dictIds).or().in("parent_id", dictIds));
        boolean result = remove(wpd);
        return result;
    }

    @Override
    public List<Dict> queryDictListByParentCode(String dictCode) {
        QueryWrapper<Dict> parentEw = new QueryWrapper<>();
        parentEw.eq("dict_status", "1").eq("dict_code", dictCode);
        Dict dict = getOne(parentEw);
        List<Dict> dictList = null;
        if (null != dict) {
            QueryWrapper<Dict> ew = new QueryWrapper<>();
            ew.eq("dict_status", "1").eq("parent_id", dict.getId());
            dictList = list(ew);
        }

        return dictList;
    }

    @Override
    public List<Dict> queryDictTreeByParentId(Long parentId) {
        if (null == parentId) {
            parentId = 0L;
        }
        QueryDictDTO dictQuery = new QueryDictDTO();
        dictQuery.setParentId(parentId);
        List<Dict> dictList = dictMapper.selectDictChildren(dictQuery);
        List<Dict> dictResultList = new ArrayList<>();
        Map<Long, Dict> dictMap = new HashMap<>();
        // 组装子父级目录
        for (Dict dict : dictList) {
            dictMap.put(dict.getId(), dict);
        }
        for (Dict dict : dictList) {
            Long treePId = dict.getParentId();
            Dict pTreev = dictMap.get(treePId);
            if (null != pTreev && !dict.equals(pTreev)) {
                List<Dict> nodes = pTreev.getChildren();
                if (null == nodes) {
                    nodes = new ArrayList<>();
                    pTreev.setChildren(nodes);
                }
                nodes.add(dict);
            } else {
                dictResultList.add(dict);
            }
        }
        return dictResultList;
    }
}
