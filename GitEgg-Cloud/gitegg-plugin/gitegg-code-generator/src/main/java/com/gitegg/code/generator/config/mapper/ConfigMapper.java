package com.gitegg.code.generator.config.mapper;

import com.gitegg.code.generator.config.entity.Config;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.gitegg.code.generator.config.dto.ConfigDTO;
import com.gitegg.code.generator.config.dto.QueryConfigDTO;

/**
 * <p>
 * 代码生成配置表 Mapper 接口
 * </p>
 *
 * @author GitEgg
 * @since 2021-09-02 18:09:28
 */
public interface ConfigMapper extends BaseMapper<Config> {

    /**
    * 查询代码生成配置表列表
    * @param page
    * @param configDTO
    * @return
    */
    Page<ConfigDTO> queryConfigList(Page<ConfigDTO> page, @Param("config") QueryConfigDTO configDTO);

    /**
    * 查询代码生成配置表信息
    * @param configDTO
    * @return
    */
    ConfigDTO queryConfig(@Param("config") QueryConfigDTO configDTO);
}
