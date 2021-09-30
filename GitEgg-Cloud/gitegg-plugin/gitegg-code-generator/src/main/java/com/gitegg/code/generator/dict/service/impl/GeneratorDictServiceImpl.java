package com.gitegg.code.generator.dict.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitegg.code.generator.dict.dto.GeneratorDictDTO;
import com.gitegg.code.generator.dict.dto.QueryGeneratorDictDTO;
import com.gitegg.code.generator.dict.entity.GeneratorDict;
import com.gitegg.code.generator.dict.mapper.GeneratorDictMapper;
import com.gitegg.code.generator.dict.service.IGeneratorDictService;
import com.gitegg.platform.base.constant.GitEggConstant;
import com.gitegg.platform.base.exception.BusinessException;
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
public class GeneratorDictServiceImpl extends ServiceImpl<GeneratorDictMapper, GeneratorDict> implements IGeneratorDictService {

    private final GeneratorDictMapper generatorDictMapper;

    @Override
    public Page<GeneratorDictDTO> selectGeneratorDictList(Page<GeneratorDictDTO> page, QueryGeneratorDictDTO generatorDict) {
        Page<GeneratorDictDTO> pageDictInfo = generatorDictMapper.selectGeneratorDictList(page, generatorDict);
        return pageDictInfo;
    }

    @Override
    public List<GeneratorDictDTO> selectGeneratorDictList(QueryGeneratorDictDTO generatorDict) {
        List<GeneratorDictDTO> dictInfoList = generatorDictMapper.selectGeneratorDictList(generatorDict);
        return dictInfoList;
    }

    @Override
    public boolean createGeneratorDict(GeneratorDict generatorDict) {
        QueryWrapper<GeneratorDict> qwDictCreate = new QueryWrapper<>();
        qwDictCreate.eq("dict_code", generatorDict.getDictCode()).eq("parent_id", generatorDict.getParentId());
        List<GeneratorDict> generatorDictList = list(qwDictCreate);
        if (!CollectionUtils.isEmpty(generatorDictList)) {
            throw new BusinessException("字典值重复");
        }

        if(null != generatorDict.getParentId() && generatorDict.getParentId().longValue() != GitEggConstant.PARENT_ID.longValue())
        {
            GeneratorDict generatorDictParent = this.getById(generatorDict.getParentId());
            String dictAncestors = generatorDictParent.getAncestors();
            generatorDict.setAncestors(dictAncestors + StrUtil.COMMA + generatorDict.getParentId());
        }
        else
        {
            generatorDict.setAncestors(GitEggConstant.PARENT_ID.toString());
        }
        boolean result = save(generatorDict);
        return result;
    }

    @Override
    public boolean updateGeneratorDict(GeneratorDict generatorDict) {
        QueryWrapper<GeneratorDict> ew = new QueryWrapper<>();
        ew.eq("dict_code", generatorDict.getDictCode()).eq("parent_id", generatorDict.getParentId()).ne("id", generatorDict.getId());
        List<GeneratorDict> generatorDictList = list(ew);
        if (!CollectionUtils.isEmpty(generatorDictList)) {
            throw new BusinessException("字典值重复");
        }

        //判断是否修改了父级ID，如果改了，那么需要更新ancestors字段
        GeneratorDict generatorDictOld = this.getById(generatorDict.getId());
        if (null != generatorDictOld && null != generatorDictOld.getParentId()
                && null != generatorDict.getParentId() && generatorDictOld.getParentId().longValue() != generatorDict.getParentId().longValue())
        {
            GeneratorDict generatorDictParentNew = this.getById(generatorDict.getParentId());
            //新的父级组合
            String ancestorsNew = null == generatorDictParentNew ? GitEggConstant.PARENT_ID.toString() : generatorDictParentNew.getAncestors() + StrUtil.COMMA + generatorDict.getParentId();
            //设置新的父级组合
            generatorDict.setAncestors(ancestorsNew);
            //旧的父级组合
            String ancestorsOld = generatorDictOld.getAncestors();

            QueryGeneratorDictDTO generatorDictParent = new QueryGeneratorDictDTO();
            generatorDictParent.setParentId(generatorDict.getId());
            //只查子节点
            generatorDictParent.setIsLeaf(GitEggConstant.Number.ONE);
            List<GeneratorDict> generatorDictChildrenList = generatorDictMapper.selectGeneratorDictChildren(generatorDictParent);
            if (!CollectionUtils.isEmpty(generatorDictChildrenList)) {
                generatorDictChildrenList = generatorDictChildrenList.stream().map(generatorDictA -> {generatorDictA.setAncestors(generatorDictA.getAncestors().replaceFirst(ancestorsOld, ancestorsNew)); return generatorDictA;}).collect(Collectors.toList());
            }
            this.updateBatchById(generatorDictChildrenList);
        }

        boolean result = updateById(generatorDict);
        return result;
    }

    @Override
    public boolean deleteGeneratorDict(Long generatorDictId) {
        QueryWrapper<GeneratorDict> wpd = new QueryWrapper<>();
        wpd.and(e -> e.eq("id", generatorDictId).or().eq("parent_id", generatorDictId));
        boolean result = remove(wpd);
        return result;
    }

    @Override
    public boolean batchDeleteGeneratorDict(List<Long> generatorDictIds) {
        QueryWrapper<GeneratorDict> wpd = new QueryWrapper<>();
        wpd.and(e -> e.in("id", generatorDictIds).or().in("parent_id", generatorDictIds));
        boolean result = remove(wpd);
        return result;
    }

    @Override
    public List<GeneratorDict> queryGeneratorDictListByParentCode(String generatorDictCode) {
        QueryWrapper<GeneratorDict> parentEw = new QueryWrapper<>();
        parentEw.eq("dict_status", "1").eq("dict_code", generatorDictCode);
        GeneratorDict generatorDict = getOne(parentEw);
        List<GeneratorDict> dictList = null;
        if (null != generatorDict) {
            QueryWrapper<GeneratorDict> ewBusiness = new QueryWrapper<>();
            ewBusiness.eq("dict_status", "1").eq("parent_id", generatorDict.getId());
            dictList = list(ewBusiness);
        }

        return dictList;
    }

    @Override
    public List<GeneratorDict> queryGeneratorDictTreeByParentId(Long parentId) {
        if (null == parentId) {
            parentId = 0L;
        }
        QueryGeneratorDictDTO generatorDictQuery = new QueryGeneratorDictDTO();
        generatorDictQuery.setParentId(parentId);
        List<GeneratorDict> generatorDictList = generatorDictMapper.selectGeneratorDictChildren(generatorDictQuery);
        List<GeneratorDict> generatorDictResultList = new ArrayList<>();
        Map<Long, GeneratorDict> generatorDictMap = new HashMap<>();
        // 组装子父级目录
        for (GeneratorDict generatorDict : generatorDictList) {
            generatorDictMap.put(generatorDict.getId(), generatorDict);
        }
        for (GeneratorDict generatorDict : generatorDictList) {
            Long treePId = generatorDict.getParentId();
            GeneratorDict pTreev = generatorDictMap.get(treePId);
            if (null != pTreev && !generatorDict.equals(pTreev)) {
                List<GeneratorDict> nodes = pTreev.getChildren();
                if (null == nodes) {
                    nodes = new ArrayList<>();
                    pTreev.setChildren(nodes);
                }
                nodes.add(generatorDict);
            } else {
                generatorDictResultList.add(generatorDict);
            }
        }
        return generatorDictResultList;
    }
}
