package com.gitegg.code.generator.field.service;

import java.util.List;

import com.gitegg.code.generator.field.dto.*;
import com.gitegg.code.generator.field.entity.Field;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 字段属性配置表 服务类
 * </p>
 *
 * @author GitEgg
 * @since 2021-09-03 11:55:34
 */
public interface IFieldService extends IService<Field> {

    /**
    * 分页查询字段属性配置表列表
    * @param page
    * @param queryFieldDTO
    * @return
    */
    Page<FieldDTO> queryFieldList(Page<FieldDTO> page, QueryFieldDTO queryFieldDTO);

    /**
     * 查询字段属性配置表列表
     * @param queryFieldDTO
     * @return
     */
    List<FieldDTO> queryFieldList(QueryFieldDTO queryFieldDTO);

    /**
     * 查询字段属性配置表列表
     * @param queryFieldDTO
     * @return
     */
    List<TableFieldDTO> queryTableFieldList(QueryFieldDTO queryFieldDTO);

    /**
     * 创建字段属性配置表
     * @param fieldList
     * @return
     */
    boolean editField(List<Field> fieldList);

    /**
    * 查询字段属性配置表详情
    * @param queryFieldDTO
    * @return
    */
    FieldDTO queryField(QueryFieldDTO queryFieldDTO);

    /**
    * 创建字段属性配置表
    * @param field
    * @return
    */
    boolean createField(CreateFieldDTO field);

    /**
    * 更新字段属性配置表
    * @param field
    * @return
    */
    boolean updateField(UpdateFieldDTO field);

    /**
    * 删除字段属性配置表
    * @param fieldId
    * @return
    */
    boolean deleteField(Long fieldId);

    /**
    * 批量删除字段属性配置表
    * @param fieldIds
    * @return
    */
    boolean batchDeleteField(List<Long> fieldIds);
}
