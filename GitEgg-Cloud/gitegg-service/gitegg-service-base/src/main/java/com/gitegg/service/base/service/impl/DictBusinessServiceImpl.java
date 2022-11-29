package com.gitegg.service.base.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitegg.platform.base.constant.DictConstant;
import com.gitegg.platform.base.constant.GitEggConstant;
import com.gitegg.platform.base.exception.BusinessException;
import com.gitegg.platform.base.util.BeanCopierUtils;
import com.gitegg.service.base.dto.*;
import com.gitegg.service.base.entity.Dict;
import com.gitegg.service.base.entity.DictBusiness;
import com.gitegg.service.base.mapper.DictBusinessMapper;
import com.gitegg.service.base.service.IDictBusinessService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
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
    
    private final RedisTemplate redisTemplate;
    
    /**
     * 是否开启租户模式
     */
    @Value("${tenant.enable}")
    private Boolean enable;

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
    
        // 新增成功后，更新缓存
        if (result)
        {
            DictBusiness updateDict = this.getById(dictBusiness.getId());
            UpdateDictCache updateDictCache = BeanCopierUtils.copyByClass(dictBusiness, UpdateDictCache.class);
            updateDictCache.setTenantId(updateDict.getTenantId());
            this.addOrUpdateDictConfigCache(updateDictCache);
        }
        
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
    
        // 修改成功后，更新缓存
        if (result)
        {
            DictBusiness updateDict = this.getById(dictBusiness.getId());
            UpdateDictCache updateDictCache = BeanCopierUtils.copyByClass(dictBusiness, UpdateDictCache.class);
            updateDictCache.setOldDictCode(dictBusinessOld.getDictCode());
            updateDictCache.setTenantId(updateDict.getTenantId());
            this.addOrUpdateDictConfigCache(updateDictCache);
        }
        
        return result;
    }

    @Override
    public boolean deleteDictBusiness(Long dictBusinessId) {
        
        // 查出待删除的字典，数据库删除成功后，删除缓存
        DictBusiness dict = this.getById(dictBusinessId);
    
        LambdaQueryWrapper<DictBusiness> wpd = new LambdaQueryWrapper<>();
        wpd.and(e -> e.eq(DictBusiness::getId, dictBusinessId).or().eq(DictBusiness::getParentId, dictBusinessId));
        boolean result = remove(wpd);
    
        // 删除成功后，更新缓存
        if (result)
        {
            this.deleteDictConfigCache(dict);
        }
        
        return result;
    }

    @Override
    public boolean batchDeleteDictBusiness(List<Long> dictBusinessIds) {
        
        // 查出待删除的字典，数据库删除成功后，删除缓存
        List<DictBusiness> dictBusinessList = this.listByIds(dictBusinessIds);
    
        LambdaQueryWrapper<DictBusiness> wpd = new LambdaQueryWrapper<>();
        wpd.and(e -> e.in(DictBusiness::getId, dictBusinessIds).or().in(DictBusiness::getParentId, dictBusinessIds));
        boolean result = remove(wpd);
        
        if (result && !CollectionUtils.isEmpty(dictBusinessList))
        {
            for(DictBusiness dict: dictBusinessList)
            {
                this.deleteDictConfigCache(dict);
            }
        }
        
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
    
    /**
     * 排除多租户，查询初始化业务数据字典
     * @param dict
     * @return
     */
    @Override
    public List<DictBusinessDTO> initDictBusinessList(QueryDictBusinessDTO dict) {
        List<DictBusinessDTO> dictInfoList = dictBusinessMapper.initDictBusinessList(dict);
        return dictInfoList;
    }
    
    /**
     * 初始化业务数据词典配置
     * @return
     */
    @Override
    public void initDictBusinessConfig() {
        QueryDictBusinessDTO queryDictBusinessDTO = new QueryDictBusinessDTO();
        queryDictBusinessDTO.setDictStatus(GitEggConstant.ENABLE);
        List<DictBusinessDTO> dictList = this.initDictBusinessList(queryDictBusinessDTO);
        // 判断是否开启了租户模式，如果开启了，那么角色权限需要按租户进行分类存储
        if (enable) {
            // 以租户分组
            Map<Long, List<DictBusinessDTO>> dictListMap = dictList.stream().collect(Collectors.groupingBy(DictBusinessDTO::getTenantId));
            dictListMap.forEach((key, value) -> {
                String redisKey = DictConstant.DICT_TENANT_MAP_PREFIX + key + DictConstant.DICT_BUSINESS_TYPE;
                this.parseDictConfig(redisKey, dictList);
            });
        } else {
            this.parseDictConfig(DictConstant.DICT_MAP_PREFIX + DictConstant.DICT_BUSINESS_TYPE, dictList);
        }
    }
    
    private void parseDictConfig(String key, List<DictBusinessDTO> dictList) {
        // 以字典类型分组
        Map<Long, List<DictBusinessDTO>> dictTypeListMap = dictList.stream().collect(Collectors.groupingBy(DictBusinessDTO::getParentId));
        // 用于匹配id和code
        Map<Long, String> dictIdCodeMap = dictList.stream().collect(Collectors.toMap(DictBusinessDTO::getId, DictBusinessDTO::getDictCode));
        dictTypeListMap.forEach((typeKey, typeValue) -> {
            String dictKeyCode = null != dictIdCodeMap.get(typeKey) ? dictIdCodeMap.get(typeKey) : DictConstant.ALL_DICT_TYPE;
            String dictRedisKey = key + dictKeyCode;
            redisTemplate.delete(dictRedisKey);
            addDictConfig(dictRedisKey, typeValue);
        });
    }
    
    private void addDictConfig(String key, List<DictBusinessDTO> dictList) {
        
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
            
            String redisKey = DictConstant.DICT_MAP_PREFIX + DictConstant.DICT_BUSINESS_TYPE;
            if (enable) {
                redisKey = DictConstant.DICT_TENANT_MAP_PREFIX + dict.getTenantId() + DictConstant.DICT_BUSINESS_TYPE;
            }
            
            // 首先判断修改的是子字典还是父字典
            if (null != dict.getParentId() && dict.getParentId().longValue() != GitEggConstant.PARENT_ID)
            {
                // 修改子字典
                LambdaQueryWrapper<DictBusiness> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                lambdaQueryWrapper.eq(DictBusiness::getDictStatus, GitEggConstant.ENABLE).eq(DictBusiness::getId, dict.getParentId());
                DictBusiness dictParent = this.getOne(lambdaQueryWrapper);
                redisKey += dictParent.getDictCode();
            } else
            {
                // 当父字典code改变时，这里需要进行更新
                if (!StringUtils.isEmpty(dict.getOldDictCode())
                        && !dict.getDictCode().equals(dict.getOldDictCode()))
                {
                    QueryDictBusinessDTO queryDictBusinessDTO = new QueryDictBusinessDTO();
                    queryDictBusinessDTO.setDictStatus(GitEggConstant.ENABLE);
                    queryDictBusinessDTO.setParentId(dict.getId());
                    List<DictBusinessDTO> dictList = this.selectDictBusinessList(queryDictBusinessDTO);
                    redisTemplate.delete(redisKey + dict.getOldDictCode());
                    addDictConfig(redisKey + dict.getDictCode(), dictList);
                }
                // 修改父字典
                redisKey += DictConstant.ALL_DICT_TYPE;
            }
            
            QueryDictBusinessDTO queryDictBusinessDTO = new QueryDictBusinessDTO();
            queryDictBusinessDTO.setDictStatus(GitEggConstant.ENABLE);
            queryDictBusinessDTO.setParentId(dict.getParentId());
            List<DictBusinessDTO> dictList = this.selectDictBusinessList(queryDictBusinessDTO);
            
            redisTemplate.delete(redisKey);
            addDictConfig(redisKey, dictList);
        } catch (Exception e) {
            log.error("修改数据字典缓存失败：{}" , e);
        }
    }
    
    private void deleteDictConfigCache(DictBusiness dict) {
        try {
            String redisKey = DictConstant.DICT_MAP_PREFIX + DictConstant.DICT_BUSINESS_TYPE;
            if (enable) {
                redisKey = DictConstant.DICT_TENANT_MAP_PREFIX + dict.getTenantId() + DictConstant.DICT_BUSINESS_TYPE;
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
                LambdaQueryWrapper<DictBusiness> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                lambdaQueryWrapper.eq(DictBusiness::getDictStatus, GitEggConstant.ENABLE).eq(DictBusiness::getId, dict.getParentId());
                DictBusiness dictParent = this.getOne(lambdaQueryWrapper);
                String redisDictKey = redisKey + dictParent.getDictCode();
                redisTemplate.opsForHash().delete(redisDictKey, dict.getDictCode());
            }
        } catch (Exception e) {
            log.error("删除数据字典缓存失败：{}" , e);
        }
    }
}
