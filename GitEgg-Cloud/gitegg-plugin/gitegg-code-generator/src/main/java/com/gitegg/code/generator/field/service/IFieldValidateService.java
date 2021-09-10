package com.gitegg.code.generator.field.service;

import java.util.List;

import com.gitegg.code.generator.field.entity.FieldValidate;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import com.gitegg.code.generator.field.dto.FieldValidateDTO;
import com.gitegg.code.generator.field.dto.CreateFieldValidateDTO;
import com.gitegg.code.generator.field.dto.UpdateFieldValidateDTO;
import com.gitegg.code.generator.field.dto.QueryFieldValidateDTO;

/**
 * <p>
 * 字段校验规则配置表 服务类
 * </p>
 *
 * @author GitEgg
 * @since 2021-09-03 12:01:04
 */
public interface IFieldValidateService extends IService<FieldValidate> {

    /**
    * 分页查询字段校验规则配置表列表
    * @param page
    * @param queryFieldValidateDTO
    * @return
    */
    Page<FieldValidateDTO> queryFieldValidateList(Page<FieldValidateDTO> page, QueryFieldValidateDTO queryFieldValidateDTO);

    /**
    * 查询字段校验规则配置表详情
    * @param queryFieldValidateDTO
    * @return
    */
    FieldValidateDTO queryFieldValidate(QueryFieldValidateDTO queryFieldValidateDTO);

    /**
    * 创建字段校验规则配置表
    * @param fieldValidate
    * @return
    */
    boolean createFieldValidate(CreateFieldValidateDTO fieldValidate);

    /**
    * 更新字段校验规则配置表
    * @param fieldValidate
    * @return
    */
    boolean updateFieldValidate(UpdateFieldValidateDTO fieldValidate);

    /**
    * 删除字段校验规则配置表
    * @param fieldValidateId
    * @return
    */
    boolean deleteFieldValidate(Long fieldValidateId);

    /**
    * 批量删除字段校验规则配置表
    * @param fieldValidateIds
    * @return
    */
    boolean batchDeleteFieldValidate(List<Long> fieldValidateIds);
}
