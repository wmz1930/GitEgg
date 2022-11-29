package com.gitegg.service.base.service.impl;

import java.util.*;
import java.util.stream.Collectors;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gitegg.platform.base.constant.AuthConstant;
import com.gitegg.platform.base.constant.DictConstant;
import com.gitegg.platform.base.constant.GitEggConstant;
import com.gitegg.platform.base.util.BeanCopierUtils;
import com.gitegg.platform.base.util.JsonUtils;
import com.gitegg.service.base.dto.UpdateDictCache;
import jodd.util.StringPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
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
import org.springframework.util.StringUtils;

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
    
    private final RedisTemplate redisTemplate;
    
    /**
     * 是否开启租户模式
     */
    @Value("${tenant.enable}")
    private Boolean enable;

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
        
        // 新增成功后，更新缓存
        if (result)
        {
            Dict updateDict = this.getById(dict.getId());
            UpdateDictCache updateDictCache = BeanCopierUtils.copyByClass(dict, UpdateDictCache.class);
            updateDictCache.setTenantId(updateDict.getTenantId());
            this.addOrUpdateDictConfigCache(updateDictCache);
        }
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
    
        // 修改成功后，更新缓存
        if (result)
        {
            Dict updateDict = this.getById(dict.getId());
            UpdateDictCache updateDictCache = BeanCopierUtils.copyByClass(dict, UpdateDictCache.class);
            updateDictCache.setOldDictCode(dictOld.getDictCode());
            updateDictCache.setTenantId(updateDict.getTenantId());
            this.addOrUpdateDictConfigCache(updateDictCache);
        }
        
        return result;
    }

    @Override
    public boolean deleteDict(Long dictId) {
        
        // 查出待删除的字典，数据库删除成功后，删除缓存
        Dict dict = this.getById(dictId);
        
        LambdaQueryWrapper<Dict> wpd = new LambdaQueryWrapper<>();
        wpd.and(e -> e.eq(Dict::getId, dictId).or().eq(Dict::getParentId, dictId));
        boolean result = remove(wpd);
        
        // 删除成功后，更新缓存
        if (result)
        {
            this.deleteDictConfigCache(dict);
        }
        return result;
    }

    @Override
    public boolean batchDeleteDict(List<Long> dictIds) {
        // 查出待删除的字典，数据库删除成功后，删除缓存
        List<Dict> dictList = this.listByIds(dictIds);
        
        LambdaQueryWrapper<Dict> wpd = new LambdaQueryWrapper<>();
        wpd.and(e -> e.in(Dict::getId, dictIds).or().in(Dict::getParentId, dictIds));
        boolean result = remove(wpd);
        if (result && !CollectionUtils.isEmpty(dictList))
        {
            for(Dict dict: dictList)
            {
                this.deleteDictConfigCache(dict);
            }
        }
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
    
    /**
     * 排除多租户，查询初始化数据字典
     * @param dict
     * @return
     */
    @Override
    public List<DictDTO> initDictList(QueryDictDTO dict) {
        List<DictDTO> dictInfoList = dictMapper.initDictList(dict);
        return dictInfoList;
    }
    
    /**
     * 初始化数据词典配置
     * @return
     */
    @Override
    public void initDictConfig() {
        QueryDictDTO queryDictDTO = new QueryDictDTO();
        queryDictDTO.setDictStatus(GitEggConstant.ENABLE);
        List<DictDTO> dictList = this.initDictList(queryDictDTO);
        // 判断是否开启了租户模式，如果开启了，那么角色权限需要按租户进行分类存储
        if (enable) {
            // 以租户分组
            Map<Long, List<DictDTO>> dictListMap = dictList.stream().collect(Collectors.groupingBy(DictDTO::getTenantId));
            dictListMap.forEach((key, value) -> {
                String redisKey = DictConstant.DICT_TENANT_MAP_PREFIX + key + DictConstant.DICT_SYSTEM_TYPE;
                this.parseDictConfig(redisKey, dictList);
            });
        } else {
            this.parseDictConfig(DictConstant.DICT_MAP_PREFIX + DictConstant.DICT_SYSTEM_TYPE, dictList);
        }
    }
    
    private void parseDictConfig(String key, List<DictDTO> dictList) {
        // 以字典类型分组
        Map<Long, List<DictDTO>> dictTypeListMap = dictList.stream().collect(Collectors.groupingBy(DictDTO::getParentId));
        // 用于匹配id和code
        Map<Long, String> dictIdCodeMap = dictList.stream().collect(Collectors.toMap(DictDTO::getId, DictDTO::getDictCode));
        dictTypeListMap.forEach((typeKey, typeValue) -> {
            String dictKeyCode = null != dictIdCodeMap.get(typeKey) ? dictIdCodeMap.get(typeKey) : DictConstant.ALL_DICT_TYPE;
            String dictRedisKey = key + dictKeyCode;
            redisTemplate.delete(dictRedisKey);
            addDictConfig(dictRedisKey, typeValue);
        });
    }
    
    private void addDictConfig(String key, List<DictDTO> dictList) {
        
        Map<String, String> dictMap = new TreeMap<>();
        Optional.ofNullable(dictList).orElse(new ArrayList<>()).forEach(dict -> {
            try {
                dictMap.put(dict.getDictCode(), dict.getDictName());
                redisTemplate.opsForHash().putAll(key, dictMap);
            } catch (Exception e) {
                log.error("初始化数据词典失败：{}" , e);
            }
        });
        
    }
    
    private void addOrUpdateDictConfigCache(UpdateDictCache dict) {
        try {
            
            String redisKey = DictConstant.DICT_MAP_PREFIX + DictConstant.DICT_SYSTEM_TYPE;
            if (enable) {
                redisKey = DictConstant.DICT_TENANT_MAP_PREFIX + dict.getTenantId() + DictConstant.DICT_SYSTEM_TYPE;
            }
            
            // 首先判断修改的是子字典还是父字典
            if (null != dict.getParentId() && dict.getParentId().longValue() != GitEggConstant.PARENT_ID)
            {
                // 修改子字典
                LambdaQueryWrapper<Dict> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                lambdaQueryWrapper.eq(Dict::getDictStatus, GitEggConstant.ENABLE).eq(Dict::getId, dict.getParentId());
                Dict dictParent = this.getOne(lambdaQueryWrapper);
                redisKey += dictParent.getDictCode();
            } else
            {
                // 当父字典code改变时，这里需要进行更新
                if (!StringUtils.isEmpty(dict.getOldDictCode())
                        && !dict.getDictCode().equals(dict.getOldDictCode()))
                {
                    QueryDictDTO queryDictDTO = new QueryDictDTO();
                    queryDictDTO.setDictStatus(GitEggConstant.ENABLE);
                    queryDictDTO.setParentId(dict.getId());
                    List<DictDTO> dictList = this.selectDictList(queryDictDTO);
                    redisTemplate.delete(redisKey + dict.getOldDictCode());
                    addDictConfig(redisKey + dict.getDictCode(), dictList);
                }
                // 修改父字典
                redisKey += DictConstant.ALL_DICT_TYPE;
            }
            
            QueryDictDTO queryDictDTO = new QueryDictDTO();
            queryDictDTO.setDictStatus(GitEggConstant.ENABLE);
            queryDictDTO.setParentId(dict.getParentId());
            List<DictDTO> dictList = this.selectDictList(queryDictDTO);
            
            redisTemplate.delete(redisKey);
            addDictConfig(redisKey, dictList);
        } catch (Exception e) {
            log.error("修改数据字典缓存失败：{}" , e);
        }
    }
    
    private void deleteDictConfigCache(Dict dict) {
        try {
            String redisKey = DictConstant.DICT_MAP_PREFIX + DictConstant.DICT_SYSTEM_TYPE;
            if (enable) {
                redisKey = DictConstant.DICT_TENANT_MAP_PREFIX + dict.getTenantId() + DictConstant.DICT_SYSTEM_TYPE;
            }
            // 子字典
            if (null != dict.getParentId() && dict.getParentId().longValue() == GitEggConstant.PARENT_ID)
            {
                // 删除父字典
                String redisDictKey = redisKey + dict.getDictCode();
                redisTemplate.delete(redisDictKey);
    
                // 删除总列表中的父字典
                String redisDictParentKey = redisKey + DictConstant.ALL_DICT_TYPE;
                redisTemplate.opsForHash().delete(redisDictParentKey, dict.getDictCode());
            } else {
                // 删除子字典
                LambdaQueryWrapper<Dict> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                lambdaQueryWrapper.eq(Dict::getDictStatus, GitEggConstant.ENABLE).eq(Dict::getId, dict.getParentId());
                Dict dictParent = this.getOne(lambdaQueryWrapper);
                String redisDictKey = redisKey + dictParent.getDictCode();
                redisTemplate.opsForHash().delete(redisDictKey, dict.getDictCode());
            }
        } catch (Exception e) {
            log.error("删除数据字典缓存失败：{}" , e);
        }
    }
}
