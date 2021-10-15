package com.gitegg.code.generator.validate.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import com.gitegg.code.generator.validate.entity.Validate;
import com.gitegg.code.generator.validate.mapper.ValidateMapper;
import com.gitegg.code.generator.validate.service.IValidateService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.gitegg.platform.base.util.BeanCopierUtils;
import com.gitegg.code.generator.validate.dto.ValidateDTO;
import com.gitegg.code.generator.validate.dto.CreateValidateDTO;
import com.gitegg.code.generator.validate.dto.UpdateValidateDTO;
import com.gitegg.code.generator.validate.dto.QueryValidateDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 字段校验规则配置表 服务实现类
 * </p>
 *
 * @author GitEgg
 * @since 2021-10-15
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ValidateServiceImpl extends ServiceImpl<ValidateMapper, Validate> implements IValidateService {

    private final ValidateMapper validateMapper;

    /**
    * 分页查询字段校验规则配置表列表
    * @param page
    * @param queryValidateDTO
    * @return
    */
    @Override
    public Page<ValidateDTO> queryValidateList(Page<ValidateDTO> page, QueryValidateDTO queryValidateDTO) {
        Page<ValidateDTO> validateInfoList = validateMapper.queryValidateList(page, queryValidateDTO);
        return validateInfoList;
    }

    /**
    * 查询字段校验规则配置表列表
    * @param queryValidateDTO
    * @return
    */
    @Override
    public List<ValidateDTO> queryValidateList(QueryValidateDTO queryValidateDTO) {
        List<ValidateDTO> validateInfoList = validateMapper.queryValidateList(queryValidateDTO);
        return validateInfoList;
    }

    /**
    * 查询字段校验规则配置表详情
    * @param queryValidateDTO
    * @return
    */
    @Override
    public ValidateDTO queryValidate(QueryValidateDTO queryValidateDTO) {
        ValidateDTO validateDTO = validateMapper.queryValidate(queryValidateDTO);
        return validateDTO;
    }

    /**
    * 创建字段校验规则配置表
    * @param validate
    * @return
    */
    @Override
    public boolean createValidate(CreateValidateDTO validate) {
        Validate validateEntity = BeanCopierUtils.copyByClass(validate, Validate.class);
        boolean result = this.save(validateEntity);
        return result;
    }

    /**
    * 更新字段校验规则配置表
    * @param validate
    * @return
    */
    @Override
    public boolean updateValidate(UpdateValidateDTO validate) {
        Validate validateEntity = BeanCopierUtils.copyByClass(validate, Validate.class);
        QueryWrapper<Validate> wrapper = new QueryWrapper<>();
        wrapper.eq("id", validateEntity.getId());
        boolean result = this.update(validateEntity, wrapper);
        return result;
    }

    /**
    * 删除字段校验规则配置表
    * @param validateId
    * @return
    */
    @Override
    public boolean deleteValidate(Long validateId) {
        boolean result = this.removeById(validateId);
        return result;
    }

    /**
    * 批量删除字段校验规则配置表
    * @param validateIds
    * @return
    */
    @Override
    public boolean batchDeleteValidate(List<Long> validateIds) {
        boolean result = this.removeByIds(validateIds);
        return result;
    }
}
