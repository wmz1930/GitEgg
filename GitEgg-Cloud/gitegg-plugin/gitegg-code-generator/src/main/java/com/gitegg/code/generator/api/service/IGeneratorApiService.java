package com.gitegg.code.generator.api.service;

import java.util.List;

import com.gitegg.code.generator.api.entity.GeneratorApi;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import com.gitegg.code.generator.api.bo.GeneratorApiExportBO;
import com.gitegg.code.generator.api.dto.GeneratorApiDTO;
import com.gitegg.code.generator.api.dto.CreateGeneratorApiDTO;
import com.gitegg.code.generator.api.dto.UpdateGeneratorApiDTO;
import com.gitegg.code.generator.api.dto.QueryGeneratorApiDTO;

import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 接口配置表 服务类
 * </p>
 *
 * @author GitEgg
 * @since 2022-12-12
 */
public interface IGeneratorApiService extends IService<GeneratorApi> {

    /**
    * 分页查询接口配置表列表
    * @param page
    * @param queryGeneratorApiDTO
    * @return
    */
    Page<GeneratorApiDTO> queryGeneratorApiList(Page<GeneratorApiDTO> page, QueryGeneratorApiDTO queryGeneratorApiDTO);

    /**
    * 查询接口配置表列表
    * @param queryGeneratorApiDTO
    * @return
    */
    List<GeneratorApiDTO> queryGeneratorApiList(QueryGeneratorApiDTO queryGeneratorApiDTO);

    /**
    * 查询接口配置表详情
    * @param queryGeneratorApiDTO
    * @return
    */
    GeneratorApiDTO queryGeneratorApi(QueryGeneratorApiDTO queryGeneratorApiDTO);

    /**
    * 创建接口配置表
    * @param generatorApi
    * @return
    */
    boolean createGeneratorApi(CreateGeneratorApiDTO generatorApi);

    /**
    * 更新接口配置表
    * @param generatorApi
    * @return
    */
    boolean updateGeneratorApi(UpdateGeneratorApiDTO generatorApi);

    /**
    * 删除接口配置表
    * @param generatorApiId
    * @return
    */
    boolean deleteGeneratorApi(Long generatorApiId);

    /**
    * 批量删除接口配置表
    * @param generatorApiIds
    * @return
    */
    boolean batchDeleteGeneratorApi(List<Long> generatorApiIds);

    /**
    * 导出接口配置表列表
    * @param queryGeneratorApiDTO
    * @return
    */
    List<GeneratorApiExportBO> exportGeneratorApiList(QueryGeneratorApiDTO queryGeneratorApiDTO);

    /**
    * 导入接口配置表列表
    * @param file
    * @return
    */
    boolean importGeneratorApiList(MultipartFile file);
}
