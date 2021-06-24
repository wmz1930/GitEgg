package com.gitegg.service.base.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public boolean createDict(Dict dict) {
        QueryWrapper<Dict> qwDictCreate = new QueryWrapper<>();
        qwDictCreate.eq("dict_code", dict.getDictCode()).eq("parent_id", dict.getParentId());
        List<Dict> dictList = list(qwDictCreate);
        if (!CollectionUtils.isEmpty(dictList)) {
            throw new BusinessException("字典值重复");
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
    public List<Dict> queryDictListByPanentCode(String dictCode) {
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
    public List<DictDTO> queryDictTreeByPanentId(Integer parentId) {
        if (null == parentId) {
            parentId = 0;
        }
        List<DictDTO> dictList = dictMapper.queryDictTreeProc(parentId);
        List<DictDTO> dicts = new ArrayList<>();
        Map<Long, DictDTO> dictMap = new HashMap<>();
        // 组装子父级目录
        for (DictDTO dict : dictList) {
            dictMap.put(dict.getId(), dict);
        }
        for (DictDTO dict : dictList) {
            Long treePId = dict.getParentId();
            DictDTO pTreev = dictMap.get(treePId);
            if (null != pTreev && !dict.equals(pTreev)) {
                List<DictDTO> nodes = pTreev.getChildren();
                if (null == nodes) {
                    nodes = new ArrayList<DictDTO>();
                    pTreev.setChildren(nodes);
                }
                nodes.add(dict);
            } else {
                dicts.add(dict);
            }
        }
        return dicts;
    }
}
