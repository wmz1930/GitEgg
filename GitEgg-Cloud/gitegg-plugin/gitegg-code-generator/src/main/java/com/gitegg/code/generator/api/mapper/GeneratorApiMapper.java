package com.gitegg.code.generator.api.mapper;

import com.gitegg.code.generator.api.entity.GeneratorApi;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.gitegg.code.generator.api.dto.GeneratorApiDTO;
import com.gitegg.code.generator.api.dto.QueryGeneratorApiDTO;

import java.util.List;

/**
 * <p>
 * 接口配置表 Mapper 接口
 * </p>
 *
 * @author GitEgg
 * @since 2022-12-12
 */
public interface GeneratorApiMapper extends BaseMapper<GeneratorApi> {

    /**
    * 分页查询接口配置表列表
    * @param page
    * @param generatorApiDTO
    * @return
    */
    Page<GeneratorApiDTO> queryGeneratorApiList(Page<GeneratorApiDTO> page, @Param("generatorApi") QueryGeneratorApiDTO generatorApiDTO);

    /**
    * 查询接口配置表列表
    * @param generatorApiDTO
    * @return
    */
    List<GeneratorApiDTO> queryGeneratorApiList(@Param("generatorApi") QueryGeneratorApiDTO generatorApiDTO);

    /**
    * 查询接口配置表信息
    * @param generatorApiDTO
    * @return
    */
    GeneratorApiDTO queryGeneratorApi(@Param("generatorApi") QueryGeneratorApiDTO generatorApiDTO);
}
