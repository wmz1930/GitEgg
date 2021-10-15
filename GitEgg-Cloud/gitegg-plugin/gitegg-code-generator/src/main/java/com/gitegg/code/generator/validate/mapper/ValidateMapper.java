package com.gitegg.code.generator.validate.mapper;

import com.gitegg.code.generator.validate.entity.Validate;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.gitegg.code.generator.validate.dto.ValidateDTO;
import com.gitegg.code.generator.validate.dto.QueryValidateDTO;

import java.util.List;

/**
 * <p>
 * 字段校验规则配置表 Mapper 接口
 * </p>
 *
 * @author GitEgg
 * @since 2021-10-15
 */
public interface ValidateMapper extends BaseMapper<Validate> {

    /**
    * 分页查询字段校验规则配置表列表
    * @param page
    * @param validateDTO
    * @return
    */
    Page<ValidateDTO> queryValidateList(Page<ValidateDTO> page, @Param("validate") QueryValidateDTO validateDTO);

    /**
    * 查询字段校验规则配置表列表
    * @param validateDTO
    * @return
    */
    List<ValidateDTO> queryValidateList(@Param("validate") QueryValidateDTO validateDTO);

    /**
    * 查询字段校验规则配置表信息
    * @param validateDTO
    * @return
    */
    ValidateDTO queryValidate(@Param("validate") QueryValidateDTO validateDTO);
}
