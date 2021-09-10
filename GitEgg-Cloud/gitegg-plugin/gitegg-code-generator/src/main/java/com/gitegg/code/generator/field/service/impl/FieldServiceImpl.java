package com.gitegg.code.generator.field.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import com.gitegg.code.generator.field.entity.Field;
import com.gitegg.code.generator.field.mapper.FieldMapper;
import com.gitegg.code.generator.field.service.IFieldService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.gitegg.platform.base.util.BeanCopierUtils;
import com.gitegg.code.generator.field.dto.FieldDTO;
import com.gitegg.code.generator.field.dto.CreateFieldDTO;
import com.gitegg.code.generator.field.dto.UpdateFieldDTO;
import com.gitegg.code.generator.field.dto.QueryFieldDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 字段属性配置表 服务实现类
 * </p>
 *
 * @author GitEgg
 * @since 2021-09-03 11:55:34
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FieldServiceImpl extends ServiceImpl<FieldMapper, Field> implements IFieldService {

    private final FieldMapper fieldMapper;

    /**
    * 分页查询字段属性配置表列表
    * @param page
    * @param queryFieldDTO
    * @return
    */
    @Override
    public Page<FieldDTO> queryFieldList(Page<FieldDTO> page, QueryFieldDTO queryFieldDTO) {
        Page<FieldDTO> fieldInfoList = fieldMapper.queryFieldList(page, queryFieldDTO);
        return fieldInfoList;
    }

    /**
    * 查询字段属性配置表详情
    * @param queryFieldDTO
    * @return
    */
    @Override
    public FieldDTO queryField(QueryFieldDTO queryFieldDTO) {
        FieldDTO fieldDTO = fieldMapper.queryField(queryFieldDTO);
        return fieldDTO;
    }

    /**
    * 创建字段属性配置表
    * @param field
    * @return
    */
    @Override
    public boolean createField(CreateFieldDTO field) {
        Field fieldEntity = BeanCopierUtils.copyByClass(field, Field.class);
        boolean result = this.save(fieldEntity);
        return result;
    }

    /**
    * 更新字段属性配置表
    * @param field
    * @return
    */
    @Override
    public boolean updateField(UpdateFieldDTO field) {
        Field fieldEntity = BeanCopierUtils.copyByClass(field, Field.class);
        QueryWrapper<Field> wrapper = new QueryWrapper<>();
        wrapper.eq("id", fieldEntity.getId());
        boolean result = this.update(fieldEntity, wrapper);
        return result;
    }

    /**
    * 删除字段属性配置表
    * @param fieldId
    * @return
    */
    @Override
    public boolean deleteField(Long fieldId) {
        boolean result = this.removeById(fieldId);
        return result;
    }

    /**
    * 批量删除字段属性配置表
    * @param fieldIds
    * @return
    */
    @Override
    public boolean batchDeleteField(List<Long> fieldIds) {
        boolean result = this.removeByIds(fieldIds);
        return result;
    }
}
