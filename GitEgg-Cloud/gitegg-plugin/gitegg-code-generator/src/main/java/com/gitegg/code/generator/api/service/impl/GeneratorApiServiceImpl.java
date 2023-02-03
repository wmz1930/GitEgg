package com.gitegg.code.generator.api.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import com.gitegg.code.generator.api.entity.GeneratorApi;
import com.gitegg.code.generator.api.mapper.GeneratorApiMapper;
import com.gitegg.code.generator.api.service.IGeneratorApiService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.gitegg.platform.base.util.BeanCopierUtils;
import com.gitegg.platform.base.exception.BusinessException;
import com.gitegg.code.generator.api.dto.GeneratorApiDTO;
import com.gitegg.code.generator.api.dto.CreateGeneratorApiDTO;
import com.gitegg.code.generator.api.dto.UpdateGeneratorApiDTO;
import com.gitegg.code.generator.api.dto.QueryGeneratorApiDTO;

import org.springframework.web.multipart.MultipartFile;

import com.gitegg.code.generator.api.bo.*;
import com.alibaba.excel.EasyExcel;
import com.gitegg.platform.base.util.BeanCopierUtils;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import org.springframework.util.CollectionUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 接口配置表 服务实现类
 * </p>
 *
 * @author GitEgg
 * @since 2022-12-12
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GeneratorApiServiceImpl extends ServiceImpl<GeneratorApiMapper, GeneratorApi> implements IGeneratorApiService {

    private final GeneratorApiMapper generatorApiMapper;

    /**
    * 分页查询接口配置表列表
    * @param page
    * @param queryGeneratorApiDTO
    * @return
    */
    @Override
    public Page<GeneratorApiDTO> queryGeneratorApiList(Page<GeneratorApiDTO> page, QueryGeneratorApiDTO queryGeneratorApiDTO) {
        Page<GeneratorApiDTO> generatorApiInfoList = generatorApiMapper.queryGeneratorApiList(page, queryGeneratorApiDTO);
        return generatorApiInfoList;
    }

    /**
    * 查询接口配置表列表
    * @param queryGeneratorApiDTO
    * @return
    */
    @Override
    public List<GeneratorApiDTO> queryGeneratorApiList(QueryGeneratorApiDTO queryGeneratorApiDTO) {
        List<GeneratorApiDTO> generatorApiInfoList = generatorApiMapper.queryGeneratorApiList(queryGeneratorApiDTO);
        return generatorApiInfoList;
    }

    /**
    * 查询接口配置表详情
    * @param queryGeneratorApiDTO
    * @return
    */
    @Override
    public GeneratorApiDTO queryGeneratorApi(QueryGeneratorApiDTO queryGeneratorApiDTO) {
        GeneratorApiDTO generatorApiDTO = generatorApiMapper.queryGeneratorApi(queryGeneratorApiDTO);
        return generatorApiDTO;
    }

    /**
    * 创建接口配置表
    * @param generatorApi
    * @return
    */
    @Override
    public boolean createGeneratorApi(CreateGeneratorApiDTO generatorApi) {
        GeneratorApi generatorApiEntity = BeanCopierUtils.copyByClass(generatorApi, GeneratorApi.class);
        boolean result = this.save(generatorApiEntity);
        return result;
    }

    /**
    * 更新接口配置表
    * @param generatorApi
    * @return
    */
    @Override
    public boolean updateGeneratorApi(UpdateGeneratorApiDTO generatorApi) {
        GeneratorApi generatorApiEntity = BeanCopierUtils.copyByClass(generatorApi, GeneratorApi.class);
        boolean result = this.updateById(generatorApiEntity);
        return result;
    }

    /**
    * 删除接口配置表
    * @param generatorApiId
    * @return
    */
    @Override
    public boolean deleteGeneratorApi(Long generatorApiId) {
        boolean result = this.removeById(generatorApiId);
        return result;
    }
    /**
    * 批量删除接口配置表
    * @param generatorApiIds
    * @return
    */
    @Override
    public boolean batchDeleteGeneratorApi(List<Long> generatorApiIds) {
        boolean result = this.removeByIds(generatorApiIds);
        return result;
    }
    /**
    * 导出接口配置表列表
    * @param queryGeneratorApiDTO
    * @return
    */
    @Override
    public List<GeneratorApiExportBO> exportGeneratorApiList(QueryGeneratorApiDTO queryGeneratorApiDTO) {
        List<GeneratorApiExportBO> generatorApiExportList = new ArrayList<>();
        List<GeneratorApiDTO> generatorApiInfoList = this.queryGeneratorApiList(queryGeneratorApiDTO);
        if (!CollectionUtils.isEmpty(generatorApiInfoList))
        {
            for (GeneratorApiDTO generatorApiInfo : generatorApiInfoList) {
                GeneratorApiExportBO generatorApiExportBO = BeanCopierUtils.copyByClass(generatorApiInfo, GeneratorApiExportBO.class);
                generatorApiExportList.add(generatorApiExportBO);
            }
        }
        return generatorApiExportList;
    }
    /**
    * 导入接口配置表列表
    * @param file
    * @return
    */
    @Override
    public boolean importGeneratorApiList(MultipartFile file) {
        boolean importSuccess = false;
        try {
            List<GeneratorApiImportBO> generatorApiImportList = EasyExcel.read(file.getInputStream(), GeneratorApiImportBO.class, null).sheet().doReadSync();
            if (!CollectionUtils.isEmpty(generatorApiImportList))
            {
                List<GeneratorApi> generatorApiList = new ArrayList<>();
                generatorApiImportList.stream().forEach(generatorApiImportBO-> {
                    generatorApiList.add(BeanCopierUtils.copyByClass(generatorApiImportBO, GeneratorApi.class));
                });
                importSuccess = this.saveBatch(generatorApiList);
            }
        } catch (IOException e) {
            log.error("批量导入接口配置表数据时发生错误:{}", e);
            throw new BusinessException("批量导入失败:" + e);
        }
        return importSuccess;
    }
}
