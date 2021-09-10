package com.gitegg.code.generator.field.mapper;

import com.gitegg.code.generator.field.entity.FieldValidate;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.gitegg.code.generator.field.dto.FieldValidateDTO;
import com.gitegg.code.generator.field.dto.QueryFieldValidateDTO;

/**
 * <p>
 * 字段校验规则配置表 Mapper 接口
 * </p>
 *
 * @author GitEgg
 * @since 2021-09-03 12:01:04
 */
public interface FieldValidateMapper extends BaseMapper<FieldValidate> {

    /**
    * 查询字段校验规则配置表列表
    * @param page
    * @param fieldValidateDTO
    * @return
    */
    Page<FieldValidateDTO> queryFieldValidateList(Page<FieldValidateDTO> page, @Param("fieldValidate") QueryFieldValidateDTO fieldValidateDTO);

    /**
    * 查询字段校验规则配置表信息
    * @param fieldValidateDTO
    * @return
    */
    FieldValidateDTO queryFieldValidate(@Param("fieldValidate") QueryFieldValidateDTO fieldValidateDTO);
}
