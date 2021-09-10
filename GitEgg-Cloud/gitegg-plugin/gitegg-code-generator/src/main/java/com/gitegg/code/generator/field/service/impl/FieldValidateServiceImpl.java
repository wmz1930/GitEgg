package com.gitegg.code.generator.field.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import com.gitegg.code.generator.field.entity.FieldValidate;
import com.gitegg.code.generator.field.mapper.FieldValidateMapper;
import com.gitegg.code.generator.field.service.IFieldValidateService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.gitegg.platform.base.util.BeanCopierUtils;
import com.gitegg.code.generator.field.dto.FieldValidateDTO;
import com.gitegg.code.generator.field.dto.CreateFieldValidateDTO;
import com.gitegg.code.generator.field.dto.UpdateFieldValidateDTO;
import com.gitegg.code.generator.field.dto.QueryFieldValidateDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 字段校验规则配置表 服务实现类
 * </p>
 *
 * @author GitEgg
 * @since 2021-09-03 12:01:04
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FieldValidateServiceImpl extends ServiceImpl<FieldValidateMapper, FieldValidate> implements IFieldValidateService {

    private final FieldValidateMapper fieldValidateMapper;

    /**
    * 分页查询字段校验规则配置表列表
    * @param page
    * @param queryFieldValidateDTO
    * @return
    */
    @Override
    public Page<FieldValidateDTO> queryFieldValidateList(Page<FieldValidateDTO> page, QueryFieldValidateDTO queryFieldValidateDTO) {
        Page<FieldValidateDTO> fieldValidateInfoList = fieldValidateMapper.queryFieldValidateList(page, queryFieldValidateDTO);
        return fieldValidateInfoList;
    }

    /**
    * 查询字段校验规则配置表详情
    * @param queryFieldValidateDTO
    * @return
    */
    @Override
    public FieldValidateDTO queryFieldValidate(QueryFieldValidateDTO queryFieldValidateDTO) {
        FieldValidateDTO fieldValidateDTO = fieldValidateMapper.queryFieldValidate(queryFieldValidateDTO);
        return fieldValidateDTO;
    }

    /**
    * 创建字段校验规则配置表
    * @param fieldValidate
    * @return
    */
    @Override
    public boolean createFieldValidate(CreateFieldValidateDTO fieldValidate) {
        FieldValidate fieldValidateEntity = BeanCopierUtils.copyByClass(fieldValidate, FieldValidate.class);
        boolean result = this.save(fieldValidateEntity);
        return result;
    }

    /**
    * 更新字段校验规则配置表
    * @param fieldValidate
    * @return
    */
    @Override
    public boolean updateFieldValidate(UpdateFieldValidateDTO fieldValidate) {
        FieldValidate fieldValidateEntity = BeanCopierUtils.copyByClass(fieldValidate, FieldValidate.class);
        QueryWrapper<FieldValidate> wrapper = new QueryWrapper<>();
        wrapper.eq("id", fieldValidateEntity.getId());
        boolean result = this.update(fieldValidateEntity, wrapper);
        return result;
    }

    /**
    * 删除字段校验规则配置表
    * @param fieldValidateId
    * @return
    */
    @Override
    public boolean deleteFieldValidate(Long fieldValidateId) {
        boolean result = this.removeById(fieldValidateId);
        return result;
    }

    /**
    * 批量删除字段校验规则配置表
    * @param fieldValidateIds
    * @return
    */
    @Override
    public boolean batchDeleteFieldValidate(List<Long> fieldValidateIds) {
        boolean result = this.removeByIds(fieldValidateIds);
        return result;
    }
}
