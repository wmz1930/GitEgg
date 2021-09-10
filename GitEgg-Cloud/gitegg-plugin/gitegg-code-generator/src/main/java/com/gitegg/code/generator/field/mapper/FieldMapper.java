package com.gitegg.code.generator.field.mapper;

import com.gitegg.code.generator.field.entity.Field;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.gitegg.code.generator.field.dto.FieldDTO;
import com.gitegg.code.generator.field.dto.QueryFieldDTO;

/**
 * <p>
 * 字段属性配置表 Mapper 接口
 * </p>
 *
 * @author GitEgg
 * @since 2021-09-03 11:55:34
 */
public interface FieldMapper extends BaseMapper<Field> {

    /**
    * 查询字段属性配置表列表
    * @param page
    * @param fieldDTO
    * @return
    */
    Page<FieldDTO> queryFieldList(Page<FieldDTO> page, @Param("field") QueryFieldDTO fieldDTO);

    /**
    * 查询字段属性配置表信息
    * @param fieldDTO
    * @return
    */
    FieldDTO queryField(@Param("field") QueryFieldDTO fieldDTO);
}
