package com.gitegg.service.extension.wx.miniapp.service.impl;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.hutool.crypto.SecureUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitegg.service.extension.wx.miniapp.bo.MiniappExportBO;
import com.gitegg.service.extension.wx.miniapp.bo.MiniappImportBO;
import com.gitegg.service.extension.wx.miniapp.dto.CreateMiniappDTO;
import com.gitegg.service.extension.wx.miniapp.dto.MiniappDTO;
import com.gitegg.service.extension.wx.miniapp.dto.QueryMiniappDTO;
import com.gitegg.service.extension.wx.miniapp.dto.UpdateMiniappDTO;
import com.gitegg.service.extension.wx.miniapp.entity.Miniapp;
import com.gitegg.service.extension.wx.miniapp.mapper.MiniappMapper;
import com.gitegg.service.extension.wx.miniapp.service.IMiniappService;
import com.gitegg.platform.base.constant.AuthConstant;
import com.gitegg.platform.base.constant.GitEggConstant;
import com.gitegg.platform.base.exception.BusinessException;
import com.gitegg.platform.base.util.BeanCopierUtils;
import com.gitegg.platform.base.util.JsonUtils;
import com.gitegg.platform.boot.util.GitEggAuthUtils;
import com.gitegg.platform.wechat.miniapp.config.GitEggWxMaRedissonConfigImpl;
import com.gitegg.platform.wechat.miniapp.constant.MiniappConstant;
import jodd.util.StringPool;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 微信小程序配置 服务实现类
 * </p>
 *
 * @author GitEgg
 * @since 2023-07-16
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MiniappServiceImpl extends ServiceImpl<MiniappMapper, Miniapp> implements IMiniappService {

    private final MiniappMapper miniappMapper;

    private final RedisTemplate redisTemplate;

    private final RedissonClient redissonClient;

    /**
     * 是否开启租户模式
     */
    @Value("${tenant.enable}")
    private Boolean enable;

    /**
     * 解决循环依赖问题
     */
    private WxMaService wxMaService;

    @Autowired
    public void setWxMaService(@Lazy WxMaService wxMaService) {
        this.wxMaService = wxMaService;
    }

    /**
    * 分页查询微信小程序配置列表
    * @param page
    * @param queryMiniappDTO
    * @return
    */
    @Override
    public Page<MiniappDTO> queryMiniappList(Page<MiniappDTO> page, QueryMiniappDTO queryMiniappDTO) {
        Page<MiniappDTO> miniappInfoList = miniappMapper.queryMiniappList(page, queryMiniappDTO);
        return miniappInfoList;
    }

    /**
    * 查询微信小程序配置列表
    * @param queryMiniappDTO
    * @return
    */
    @Override
    public List<MiniappDTO> queryMiniappList(QueryMiniappDTO queryMiniappDTO) {
        List<MiniappDTO> miniappInfoList = miniappMapper.queryMiniappList(queryMiniappDTO);
        return miniappInfoList;
    }

    /**
    * 查询微信小程序配置详情
    * @param queryMiniappDTO
    * @return
    */
    @Override
    public MiniappDTO queryMiniapp(QueryMiniappDTO queryMiniappDTO) {
        MiniappDTO miniappDTO = miniappMapper.queryMiniapp(queryMiniappDTO);
        return miniappDTO;
    }

    /**
    * 创建微信小程序配置
    * @param miniapp
    * @return
    */
    @Override
    public boolean createMiniapp(CreateMiniappDTO miniapp) {
        Miniapp miniappEntity = BeanCopierUtils.copyByClass(miniapp, Miniapp.class);
        try {
            String miniappEntityStr = JsonUtils.objToJson(miniappEntity);
            miniappEntity.setMd5(SecureUtil.md5(miniappEntityStr));
        } catch (Exception e) {
            log.error("创建微信小程序配置时，md5加密失败:{}", e);
            throw new BusinessException("创建微信小程序配置时，md5加密失败:" + e);
        }
        boolean result = this.save(miniappEntity);
        if (result)
        {
            // 更新到缓存
            Miniapp miniappEntityLocal = this.getById(miniappEntity.getId());
            MiniappDTO miniappDTO = BeanCopierUtils.copyByClass(miniappEntityLocal, MiniappDTO.class);
            this.addOrUpdateMiniappCache(miniappDTO);
        }
        return result;
    }

    /**
    * 更新微信小程序配置
    * @param miniapp
    * @return
    */
    @Override
    public boolean updateMiniapp(UpdateMiniappDTO miniapp) {
        Miniapp miniappEntity = BeanCopierUtils.copyByClass(miniapp, Miniapp.class);
        Miniapp miniappEntityOld = this.getById(miniappEntity.getId());
        try {
            String miniappEntityStr = JsonUtils.objToJson(miniappEntity);
            miniappEntity.setMd5(SecureUtil.md5(miniappEntityStr));
        } catch (Exception e) {
            log.error("创建微信小程序配置时，md5加密失败:{}", e);
            throw new BusinessException("创建微信小程序配置时，md5加密失败:" + e);
        }
        boolean result = this.updateById(miniappEntity);
        if (result)
        {
            // 把旧的删掉
            MiniappDTO miniappDTOOld = BeanCopierUtils.copyByClass(miniappEntityOld, MiniappDTO.class);
            this.deleteMiniappCache(miniappDTOOld);
            // 更新到缓存
            Miniapp miniappEntityLocal = this.getById(miniappEntity.getId());
            MiniappDTO miniappDTO = BeanCopierUtils.copyByClass(miniappEntityLocal, MiniappDTO.class);
            this.addOrUpdateMiniappCache(miniappDTO);
        }
        return result;
    }

    /**
    * 删除微信小程序配置
    * @param miniappId
    * @return
    */
    @Override
    public boolean deleteMiniapp(Long miniappId) {
        // 从缓存删除
        Miniapp miniappEntity = this.getById(miniappId);
        MiniappDTO miniappDTO = BeanCopierUtils.copyByClass(miniappEntity, MiniappDTO.class);
        this.deleteMiniappCache(miniappDTO);
        // 从数据库中删除
        boolean result = this.removeById(miniappId);
        return result;
    }
    /**
    * 批量删除微信小程序配置
    * @param miniappIds
    * @return
    */
    @Override
    public boolean batchDeleteMiniapp(List<Long> miniappIds) {
        // 从缓存删除
        List<Miniapp> miniappEntityList = this.listByIds(miniappIds);
        if (!CollectionUtils.isEmpty(miniappEntityList))
        {
            for(Miniapp miniappDelete: miniappEntityList)
            {
                MiniappDTO miniappDTO = BeanCopierUtils.copyByClass(miniappDelete, MiniappDTO.class);
                this.deleteMiniappCache(miniappDTO);
            }
        }
        // 从数据库中删除
        boolean result = this.removeByIds(miniappIds);
        return result;
    }
    /**
    * 导出微信小程序配置列表
    * @param queryMiniappDTO
    * @return
    */
    @Override
    public List<MiniappExportBO> exportMiniappList(QueryMiniappDTO queryMiniappDTO) {
        List<MiniappExportBO> miniappExportList = new ArrayList<>();
        List<MiniappDTO> miniappInfoList = this.queryMiniappList(queryMiniappDTO);
        if (!CollectionUtils.isEmpty(miniappInfoList))
        {
            for (MiniappDTO miniappInfo : miniappInfoList) {
                MiniappExportBO miniappExportBO = BeanCopierUtils.copyByClass(miniappInfo, MiniappExportBO.class);
                miniappExportList.add(miniappExportBO);
            }
        }
        return miniappExportList;
    }
    /**
    * 导入微信小程序配置列表
    * @param file
    * @return
    */
    @Override
    public boolean importMiniappList(MultipartFile file) {
        boolean importSuccess = false;
        try {
            List<MiniappImportBO> miniappImportList = EasyExcel.read(file.getInputStream(), MiniappImportBO.class, null).sheet().doReadSync();
            if (!CollectionUtils.isEmpty(miniappImportList))
            {
                List<Miniapp> miniappList = new ArrayList<>();
                miniappImportList.stream().forEach(miniappImportBO-> {
                    miniappList.add(BeanCopierUtils.copyByClass(miniappImportBO, Miniapp.class));
                });
                importSuccess = this.saveBatch(miniappList);
            }
        } catch (IOException e) {
            log.error("批量导入微信小程序配置数据时发生错误:{}", e);
            throw new BusinessException("批量导入失败:" + e);
        }
        return importSuccess;
    }

    /**
     * 初始化微信小程序配置表列表
     * @return
     */
    @Override
    public void initMiniappList() {
        QueryMiniappDTO miniappDTO = new QueryMiniappDTO();
        miniappDTO.setStatus(String.valueOf(GitEggConstant.ENABLE));
        // 这里初始化所有的配置，不再只初始化已启用的配置
        List<MiniappDTO> miniappInfoList = miniappMapper.initMiniappList(miniappDTO);

        // 判断是否开启了租户模式，如果开启了，那么需要按租户进行分类存储
        if (enable) {
            Map<String, List<MiniappDTO>> miniappListMap =
                    miniappInfoList.stream().collect(Collectors.groupingBy(MiniappDTO::getAppid));
            miniappListMap.forEach((key, value) -> {
                String redisKey = MiniappConstant.WX_MINIAPP_TENANT_CONFIG_KEY + key;
                redisTemplate.delete(redisKey);
                addMiniapp(redisKey, value);
            });
        } else {
            redisTemplate.delete(MiniappConstant.WX_MINIAPP_CONFIG_KEY);
            addMiniapp(MiniappConstant.WX_MINIAPP_CONFIG_KEY, miniappInfoList);
        }
    }

    /**
     * 通过appid获取租户id，忽略租户插件
     * @param miniappDTO
     * @return
     */
    @Override
    public MiniappDTO getMiniapp(QueryMiniappDTO miniappDTO) {
        return miniappMapper.getMiniapp(miniappDTO);
    }

    /**
     * 通过appid获取appid，忽略租户插件
     * @param miniappId
     * @return
     */
    @Override
    public String getMiniappId(String miniappId) {
        if (enable) {
            // 如果前端传了租户，那么先使用前端的租户，如果没有传租户，那么从系统中查询租户
            String tenantId = GitEggAuthUtils.getTenantId();
            if (!StringUtils.isEmpty(tenantId))
            {
                String miniappStr = (String) redisTemplate.opsForHash().get(MiniappConstant.WX_MINIAPP_TENANT_CONFIG_KEY + miniappId, tenantId);
                if (!StringUtils.isEmpty(miniappStr))
                {
                    // 转为系统配置对象
                    try {
                        // 从缓存获取配置对象，如果md5配置和系统配置不一样，那么需要重新add
                        MiniappDTO miniappDTO = JsonUtils.jsonToPojo(miniappStr, MiniappDTO.class);
                        return this.ifConfig(miniappDTO);
                    } catch (Exception e) {
                        log.error("获取微信小程序配置时发生异常：{}", e);
                        throw new BusinessException("获取微信小程序配置时发生异常。");
                    }
                }
                // 缓存配置中没有也需要直接返回，因为有可能是配置文件配置的
                return tenantId + StringPool.UNDERSCORE + miniappId;
            } else {
                String redisKey = MiniappConstant.WX_MINIAPP_TENANT_CONFIG_KEY + miniappId;
                // 取缓存中所有appid的配置租户，如果存在多个租户，那么提示错误，让前端选择租户；如果只有一个租户，那么返回
                List<Object> values = redisTemplate.opsForHash().values(redisKey);
                if (!CollectionUtils.isEmpty(values))
                {
                    if (values.size() > GitEggConstant.Number.ONE)
                    {
                        throw new BusinessException("此小程序配置在多个租户下，请选择所需要操作的租户。");
                    }

                    String miniappConfig = (String) values.stream().findFirst().orElse(null);
                    try {
                        MiniappDTO miniappConfigDTO = JsonUtils.jsonToPojo(miniappConfig, MiniappDTO.class);
                        return this.ifConfig(miniappConfigDTO);
                    } catch (Exception e) {
                        log.error("获取缓存小程序配置失败:{}", e);
                        throw new BusinessException("小程序已被禁用，请联系管理员");
                    }
                }
                else
                {
                    return AuthConstant.DEFAULT_TENANT_ID + StringPool.UNDERSCORE + miniappId;
                }

            }
        } else {
            return AuthConstant.DEFAULT_TENANT_ID + StringPool.UNDERSCORE + miniappId;
        }
    }

    /**
     * 根据AppId切换当前ThreadLocal的微信小程序
     * @param miniappId
     * @return
     */
    @Override
    public boolean switchover(String miniappId)
    {
        String appId = this.getMiniappId(miniappId);
        return wxMaService.switchover(appId);
    }

    private String ifConfig(MiniappDTO miniappDTO)
    {
        if (null != miniappDTO && String.valueOf(GitEggConstant.DISABLE).equals(miniappDTO.getStatus()))
        {
            throw new BusinessException("小程序已被禁用，请联系管理员");
        }

        if (wxMaService.switchover(miniappDTO.getTenantId() + StringPool.UNDERSCORE + miniappDTO.getAppid()))
        {
            GitEggWxMaRedissonConfigImpl config = (GitEggWxMaRedissonConfigImpl)wxMaService.getWxMaConfig();
            String md5Local = config.getMd5();
            String md5Config = miniappDTO.getMd5();
            // 如果没有md5值，那么是系统配置文件配置的，不需要处理
            if (!StringUtils.isEmpty(md5Local) && !md5Local.equals(md5Config))
            {
                this.addConfig(miniappDTO);
            }
        }
        else
        {
            // 系统中不存在，则重新add
            this.addConfig(miniappDTO);
        }
        return miniappDTO.getTenantId() + StringPool.UNDERSCORE + miniappDTO.getAppid();
    }

    private void addMiniapp(String key, List<MiniappDTO> miniappList) {
        Map<String, String> miniappMap = new TreeMap<>();
        Optional.ofNullable(miniappList).orElse(new ArrayList<>()).forEach(config -> {
            try {
                miniappMap.put(config.getTenantId().toString(), JsonUtils.objToJson(config));
                redisTemplate.opsForHash().putAll(key, miniappMap);
                // wxMaService增加config
                this.addConfig(config);
            } catch (Exception e) {
                log.error("初始化微信小程序配置失败：{}" , e);
            }
        });
    }

    private void addOrUpdateMiniappCache(MiniappDTO miniappDTO) {
        try {

            String redisKey = MiniappConstant.WX_MINIAPP_CONFIG_KEY;
            if (enable) {
                redisKey = MiniappConstant.WX_MINIAPP_TENANT_CONFIG_KEY + miniappDTO.getAppid();
            }
            redisTemplate.opsForHash().put(redisKey, miniappDTO.getTenantId().toString(), JsonUtils.objToJson(miniappDTO));

            // wxMaService增加config
            this.addConfig(miniappDTO);

        } catch (Exception e) {
            log.error("初始化微信小程序配置失败：{}" , e);
        }
    }

    private void deleteMiniappCache(MiniappDTO miniappDTO) {
        try {

            String redisKey = MiniappConstant.WX_MINIAPP_CONFIG_KEY;
            if (enable) {
                redisKey = MiniappConstant.WX_MINIAPP_TENANT_CONFIG_KEY + miniappDTO.getAppid();
            }
            redisTemplate.opsForHash().delete(redisKey, miniappDTO.getTenantId().toString(), JsonUtils.objToJson(miniappDTO));
            // wxMaService删除config
            this.removeConfig(miniappDTO);
        } catch (Exception e) {
            log.error("初始化微信小程序配置失败：{}" , e);
        }
    }

    private void addConfig(MiniappDTO miniappDTO){
        // wxMaService增加config
        GitEggWxMaRedissonConfigImpl config = new GitEggWxMaRedissonConfigImpl(redissonClient);

        config.setTenantId(null != miniappDTO.getTenantId() ? miniappDTO.getTenantId().toString() : AuthConstant.DEFAULT_TENANT_ID.toString());
        config.setConfigKey(config.getTenantId() + StringPool.UNDERSCORE + miniappDTO.getAppid());
        config.setAppid(miniappDTO.getAppid());
        config.setSecret(miniappDTO.getSecret());
        config.setToken(miniappDTO.getToken());
        config.setAesKey(miniappDTO.getAesKey());
        config.setMsgDataFormat(miniappDTO.getMsgDataFormat());
        config.setMd5(miniappDTO.getMd5());

        wxMaService.addConfig(config.getConfigKey(), config);
    }

    private void removeConfig(MiniappDTO miniappDTO){
        // wxMaService删除config
        wxMaService.removeConfig((null != miniappDTO.getTenantId() ? miniappDTO.getTenantId().toString() : AuthConstant.DEFAULT_TENANT_ID.toString()) + StringPool.UNDERSCORE + miniappDTO.getAppid());
    }
}
