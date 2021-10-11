package com.gitegg.code.generator.field.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.gitegg.code.generator.config.entity.Config;
import com.gitegg.code.generator.config.service.IConfigService;
import com.gitegg.code.generator.engine.service.IEngineService;
import com.gitegg.code.generator.field.dto.*;
import com.gitegg.code.generator.field.entity.Field;
import com.gitegg.code.generator.field.mapper.FieldMapper;
import com.gitegg.code.generator.field.service.IFieldService;
import com.gitegg.code.generator.field.util.FieldUtils;
import com.gitegg.code.generator.join.entity.TableJoin;
import com.gitegg.code.generator.join.service.ITableJoinService;
import com.gitegg.platform.base.util.BeanCopierUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    private final IConfigService configService;

    private final IEngineService engineService;

    private final ITableJoinService tableJoinService;

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
     * 查询字段属性配置表列表
     * @param queryFieldDTO
     * @return
     */
    @Override
    public List<FieldDTO> queryFieldList(QueryFieldDTO queryFieldDTO) {
        List<FieldDTO> fieldInfoList = fieldMapper.queryFieldList(queryFieldDTO);
        return fieldInfoList;
    }

    /**
     * 查询字段属性配置表列表
     * @param queryFieldDTO
     * @return
     */
    @Override
    public List<TableFieldDTO> queryTableFieldList(QueryFieldDTO queryFieldDTO) {

        // 存放表名和对应的字段
        List<TableFieldDTO> tableFieldDTOList = new ArrayList<>();

        //获取代码配置
        Config config = configService.getById(queryFieldDTO.getGenerationId());
        String datasourceId = config.getDatasourceId().toString();
        List<String> tableNames = new ArrayList<>();
        tableNames.add(config.getTableName());

        TableFieldDTO tableFieldDTO = new TableFieldDTO();
        tableFieldDTO.setJoinId(0L);
        tableFieldDTO.setJoinTableName(config.getTableName());
        tableFieldDTOList.add(tableFieldDTO);

        //获取联表
        QueryWrapper<TableJoin> tableJoinQueryWrapper = new QueryWrapper<>();
        tableJoinQueryWrapper.eq("generation_id", queryFieldDTO.getGenerationId());
        List<TableJoin> tableJoinList = tableJoinService.list(tableJoinQueryWrapper);
        if (!CollectionUtils.isEmpty(tableJoinList))
        {
            tableJoinList.stream().forEach(tableJoin -> {
                tableNames.add(tableJoin.getJoinTableName());
                //初始化所有的表和字段对应关系
                TableFieldDTO tableFieldJoinDTO = new TableFieldDTO();
                tableFieldJoinDTO.setJoinId(tableJoin.getId());
                tableFieldJoinDTO.setJoinTableName(tableJoin.getJoinTableName());
                tableFieldDTOList.add(tableFieldJoinDTO);
            });
        }

        //获取所有字段信息
        List<TableInfo> tableInfoList = engineService.queryTableFields(datasourceId, tableNames);
        //数据库查的字段
        List<FieldDTO> fieldDTOList = new ArrayList<>();
        tableInfoList.forEach(tableInfo -> {
            List<TableField> tableFields = tableInfo.getFields();
            tableFields.forEach(field -> {
                FieldDTO fieldDTO = new FieldDTO();
                fieldDTO.setJoinTableName(tableInfo.getName());
                fieldDTO.setFieldName(field.getColumnName());
                fieldDTO.setFieldType(field.getType());
                fieldDTO.setComment(field.getComment());
                fieldDTO.setEntityName(field.getPropertyName());
                fieldDTO.setEntityType(field.getPropertyType());
                fieldDTO.setMinLength(1);
                fieldDTO.setMaxLength(field.getMetaInfo().getLength());

                // 页面控件默认都是单文本框
                fieldDTO.setControlType("INPUT_TEXT");

                // 自动填充字段默认新增和编辑表单不需要展示
                if(!FieldUtils.isExcludeField(field.getColumnName()))
                {
                    fieldDTO.setFormAdd(true);
                    fieldDTO.setFormEdit(true);
                    fieldDTO.setListShow(true);
                    fieldDTO.setExportFlag(true);
                    fieldDTO.setImportFlag(true);
                    // id和自动填充字段可以为空，其他字段设置为不能为null时，标注为非空字段
                    if(field.getMetaInfo().isNullable())
                    {
                        fieldDTO.setRequired(true);
                    }
                }

                fieldDTOList.add(fieldDTO);
            });
        });

        //获取已配置的字段信息
        Map<String, FieldDTO> fieldDTOMap = new HashMap<>();
        List<FieldDTO> fieldInfoList = fieldMapper.queryFieldList(queryFieldDTO);
        if (!CollectionUtils.isEmpty(fieldInfoList))
        {
            fieldDTOMap = fieldInfoList.stream().collect(Collectors.toMap(k -> String.format("%s|%s|%s",k.getJoinTableName(),k.getJoinId(),k.getFieldName()), k -> k));
        }

        //按照表名分组
        Map<String, List<FieldDTO>> fieldDTOListMap = fieldDTOList.stream().collect(Collectors.groupingBy(FieldDTO::getJoinTableName));

        //将旧值赋给新值
        Map<String, FieldDTO> finalFieldDTOMap = fieldDTOMap;
        tableFieldDTOList.forEach(tableField -> {
            // 需要深复制一个list，否则相同表的会覆盖
            List<FieldDTO> fieldDTOS = BeanCopierUtils.deepCopyList(fieldDTOListMap.get(tableField.getJoinTableName()));
            fieldDTOS.forEach(fieldDTO -> {
                fieldDTO.setJoinId(tableField.getJoinId());
                fieldDTO.setGenerationId(queryFieldDTO.getGenerationId());
                FieldDTO oldFieldDTO = finalFieldDTOMap.get(String.format("%s|%s|%s",fieldDTO.getJoinTableName(),fieldDTO.getJoinId(),fieldDTO.getFieldName()));
                if (null != oldFieldDTO)
                {
                    BeanCopierUtils.copyByObject(oldFieldDTO, fieldDTO);
                }
            });
            tableField.setFieldDTOList(fieldDTOS);
        });

        return tableFieldDTOList;
    }

    @Override
    public boolean editField(List<Field> fieldList) {
        if (!CollectionUtils.isEmpty(fieldList))
        {
            //保存或新增配置信息
            this.saveOrUpdateBatch(fieldList);
        }
        return true;
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
