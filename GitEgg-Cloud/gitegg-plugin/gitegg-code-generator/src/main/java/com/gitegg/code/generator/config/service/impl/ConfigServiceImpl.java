package com.gitegg.code.generator.config.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import com.gitegg.code.generator.config.entity.Config;
import com.gitegg.code.generator.config.mapper.ConfigMapper;
import com.gitegg.code.generator.config.service.IConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.gitegg.platform.base.util.BeanCopierUtils;
import com.gitegg.code.generator.config.dto.ConfigDTO;
import com.gitegg.code.generator.config.dto.CreateConfigDTO;
import com.gitegg.code.generator.config.dto.UpdateConfigDTO;
import com.gitegg.code.generator.config.dto.QueryConfigDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 代码生成配置表 服务实现类
 * </p>
 *
 * @author GitEgg
 * @since 2021-09-02 18:09:28
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ConfigServiceImpl extends ServiceImpl<ConfigMapper, Config> implements IConfigService {

    private final ConfigMapper configMapper;

    /**
    * 分页查询代码生成配置表列表
    * @param page
    * @param queryConfigDTO
    * @return
    */
    @Override
    public Page<ConfigDTO> queryConfigList(Page<ConfigDTO> page, QueryConfigDTO queryConfigDTO) {
        Page<ConfigDTO> configInfoList = configMapper.queryConfigList(page, queryConfigDTO);
        return configInfoList;
    }

    /**
    * 查询代码生成配置表详情
    * @param queryConfigDTO
    * @return
    */
    @Override
    public ConfigDTO queryConfig(QueryConfigDTO queryConfigDTO) {
        ConfigDTO configDTO = configMapper.queryConfig(queryConfigDTO);
        return configDTO;
    }

    /**
    * 创建代码生成配置表
    * @param config
    * @return
    */
    @Override
    public boolean createConfig(CreateConfigDTO config) {
        Config configEntity = BeanCopierUtils.copyByClass(config, Config.class);
        boolean result = this.save(configEntity);
        return result;
    }

    /**
    * 更新代码生成配置表
    * @param config
    * @return
    */
    @Override
    public boolean updateConfig(UpdateConfigDTO config) {
        Config configEntity = BeanCopierUtils.copyByClass(config, Config.class);
        QueryWrapper<Config> wrapper = new QueryWrapper<>();
        wrapper.eq("id", configEntity.getId());
        boolean result = this.update(configEntity, wrapper);
        return result;
    }

    /**
    * 删除代码生成配置表
    * @param configId
    * @return
    */
    @Override
    public boolean deleteConfig(Long configId) {
        boolean result = this.removeById(configId);
        return result;
    }

    /**
    * 批量删除代码生成配置表
    * @param configIds
    * @return
    */
    @Override
    public boolean batchDeleteConfig(List<Long> configIds) {
        boolean result = this.removeByIds(configIds);
        return result;
    }
}
