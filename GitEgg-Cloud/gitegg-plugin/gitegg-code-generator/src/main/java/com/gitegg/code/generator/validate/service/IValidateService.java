package com.gitegg.code.generator.validate.service;

import java.util.List;

import com.gitegg.code.generator.validate.entity.Validate;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import com.gitegg.code.generator.validate.dto.ValidateDTO;
import com.gitegg.code.generator.validate.dto.CreateValidateDTO;
import com.gitegg.code.generator.validate.dto.UpdateValidateDTO;
import com.gitegg.code.generator.validate.dto.QueryValidateDTO;

/**
 * <p>
 * 字段校验规则配置表 服务类
 * </p>
 *
 * @author GitEgg
 * @since 2021-10-15
 */
public interface IValidateService extends IService<Validate> {

    /**
    * 分页查询字段校验规则配置表列表
    * @param page
    * @param queryValidateDTO
    * @return
    */
    Page<ValidateDTO> queryValidateList(Page<ValidateDTO> page, QueryValidateDTO queryValidateDTO);

    /**
    * 查询字段校验规则配置表列表
    * @param queryValidateDTO
    * @return
    */
    List<ValidateDTO> queryValidateList(QueryValidateDTO queryValidateDTO);

    /**
    * 查询字段校验规则配置表详情
    * @param queryValidateDTO
    * @return
    */
    ValidateDTO queryValidate(QueryValidateDTO queryValidateDTO);

    /**
    * 创建字段校验规则配置表
    * @param validate
    * @return
    */
    boolean createValidate(CreateValidateDTO validate);

    /**
    * 更新字段校验规则配置表
    * @param validate
    * @return
    */
    boolean updateValidate(UpdateValidateDTO validate);

    /**
    * 删除字段校验规则配置表
    * @param validateId
    * @return
    */
    boolean deleteValidate(Long validateId);

    /**
    * 批量删除字段校验规则配置表
    * @param validateIds
    * @return
    */
    boolean batchDeleteValidate(List<Long> validateIds);
}
