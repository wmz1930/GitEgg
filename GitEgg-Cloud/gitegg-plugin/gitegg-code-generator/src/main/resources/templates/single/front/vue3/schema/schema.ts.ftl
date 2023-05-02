import { BasicColumn } from '/@/components/Table';
import { FormSchema } from '/@/components/Table';
<#list fields as field>
    <#if (checkExist?? && checkExist == true) && !(field.min?? || field.max?? || field.maxLength?? || field.minLength?? || field.validateValue?? || field.validateRegular??)>
        <#assign hasValidate= true/>
    </#if>
</#list>
<#if (hasStatus?? && hasStatus == true) || ((hasDict?? && hasDict == true) && (dictCodeList?exists && dictCodeList?size gt 0)) || (checkExist?? && checkExist == true)>
import {<#if (hasDict?? && hasDict == true) && (dictCodeList?exists && dictCodeList?size gt 0)> renderDict,</#if><#if (hasStatus?? && hasStatus == true)> renderStatusSwitch</#if><#if (hasValidate?? && hasValidate == true)> renderCheckExistRules</#if> } from '/@/utils/gitegg/formUtils';
</#if>
<#if (hasStatus?? && hasStatus == true) || (checkExist?? && checkExist == true)>
import {
<#if hasStatus?? && hasStatus == true>
  update${entity}Status,
</#if>
<#if checkExist?? && checkExist == true>
  check${entity}Exist,
</#if>
} from '/@/api/${vueJsPath}';
</#if>
<#if (hasDict?? && hasDict == true) && (dictCodeList?exists && dictCodeList?size gt 0)>
import { getDictBusinessCache } from '/@/utils/gitegg/dictUtils';
</#if>
<#if hasProvince?? && hasProvince == true>
import { level } from 'province-city-china/data';
</#if>
<#if (hasApi?? && hasApi == true) && (apiList?exists && apiList?size gt 0)>
<#list apiMap?values as value>
import { ${value.apiMethod} } from '${value.apiPath}';
</#list>
</#if>
import dayjs from 'dayjs';

<#-- ----------  BEGIN 数据表格Column定义  ---------->
// 数据表格Column定义
export const columns: BasicColumn[] = [
<#-- ----------  BEGIN 字段循环遍历  ---------->
<#list fields as field>
<#if field?? && field.listShow == true>
  {
    title: '${field.comment?replace("'","\"")}',
    align: 'center',
    width: 200,
    ellipsis: true,
    dataIndex: '${field.entityName}',
    <#-- ----------  START 是否进行状态处理  ---------->
<#if hasStatus?? && hasStatus == true && field.entityName == statusName>
    customRender: ({ record }) => {
      return renderStatusSwitch(record, update${entity}Status, '${field.entityName}', '状态');
    },
<#elseif field.dictCode?? && field.dictCode != "API_DICT" >
    customRender: ({ text }) => {
      return renderDict(text, '${field.dictCode}', false, false);
    },
</#if>
<#-- ----------  END 是否进行状态处理  ---------->
  },
</#if>
</#list>
<#-- ----------  END 字段循环遍历  ---------->
];
<#-- ----------  END 数据表格Column定义  ---------->

<#-- ----------  BEGIN 查询条件表单定义  ---------->

// 查询条件表单定义
export const searchFormSchema: FormSchema[] = [
<#list fields as field>
<#if field?? && field.queryTerm == true>
  {
    field: '${field.entityName}',
    label: '${field.comment}',
<#if field.controlType == "INPUT_TEXT" || field.controlType == "TEXTAREA" || field.controlType == "INPUT_PASSWORD">
    component: 'Input',
<#elseif field.controlType == "INPUT_NUMBER">
    component: 'InputNumber',
<#elseif field.controlType == "API_TREE_SELECT" || field.controlType == "API_TREE">
<#if field.controlType == "API_TREE_SELECT">
    component: 'ApiTreeSelect',
<#elseif field.controlType == "API_TREE">
    component: 'ApiTree',
</#if>
<#if field.dictCode?? && field.dictCode == "API_DICT" && field.apiId?? >
    componentProps: {
      api: ${apiMap[field.apiId?string].apiMethod},
<#if apiMap[field.apiId?string].apiParams?? && apiMap[field.apiId?string].apiParams != "" >
      params: ${apiMap[field.apiId?string].apiParams},
</#if>
<#if field.controlType == "API_TREE">
      replaceFields: {
        title: '${apiMap[field.apiId?string].labelField}',
        key: '${apiMap[field.apiId?string].valueField}',
      },
<#else>
      fieldNames: {
        label: '${apiMap[field.apiId?string].labelField}',
        key: '${apiMap[field.apiId?string].valueField}',
        value: '${apiMap[field.apiId?string].valueField}',
      },
</#if>
      style: 'width:100%;',
      getPopupContainer: () => document.body,
    },
<#else>
    componentProps: {
      api: getDictBusinessCache,
      params: { dictCode: '${field.dictCode}' },
      resultField: 'dictList',
      fieldNames: {
        label: 'dictName',
        key: 'dictCode',
        value: 'dictCode',
      },
      style: 'width:100%;',
      getPopupContainer: () => document.body,
    },
</#if>
<#elseif field.controlType == "CASCADER">
    component: 'ApiCascader',
<#if field.dictCode?? && field.dictCode == "API_DICT" && field.apiId??>
    componentProps: {
      api: ${apiMap[field.apiId?string].apiMethod},
      apiParamKey: 'parentId',
<#if apiMap[field.apiId?string].apiParams?? && apiMap[field.apiId?string].apiParams != "" >
      params: ${apiMap[field.apiId?string].apiParams},
</#if>
      dataField: 'list',
      labelField: '${apiMap[field.apiId?string].labelField}',
      valueField: '${apiMap[field.apiId?string].valueField}',
      initFetchParams: {
        parentId: 0,
      },
      style: 'width:100%;',
      isLeaf: (record) => {
        return record.isLeaf;
      },
    },
<#else>
    componentProps: {
      api: '请填写数据请求接口',
      apiParamKey: '请填写请求参数的key',
      params:  '请填写请求参数',
      dataField: 'list',
      labelField: '请填写label字段',
      valueField: '请填写value字段',
      initFetchParams: {
        // 填写初始化参数
      },
      style: 'width:100%;',
      isLeaf: (record) => {
        return record.isLeaf;
      },
    },
</#if>
<#-- BEGIN Select Radio start 查询时，Radio、CheckBox、Switch全部转换为下拉框-->
<#elseif field.controlType == "SELECT" || field.controlType == "SELECT_MULTI" || field.controlType == "RADIO" || field.controlType == "SWITCH" || field.controlType == "CHECKBOX">
    component: 'ApiSelect',
<#if field.dictCode?? && field.dictCode == "API_DICT" && field.apiId?? >
    componentProps: {
      api: ${apiMap[field.apiId?string].apiMethod},
      resultField: 'list',
<#if apiMap[field.apiId?string].apiParams?? && apiMap[field.apiId?string].apiParams != "" >
      params: ${apiMap[field.apiId?string].apiParams},
</#if>
      labelField: '${apiMap[field.apiId?string].labelField}',
      valueField: '${apiMap[field.apiId?string].valueField}',
<#if field.controlType == "SELECT_MULTI">
    mode: 'multiple',
</#if>
    },
<#else>
    componentProps: {
      api: getDictBusinessCache,
      params: { dictCode: '${field.dictCode}' },
      resultField: 'dictList',
      labelField: 'dictName',
      valueField: 'dictCode',
<#if field.controlType == "SELECT_MULTI">
    mode: 'multiple',
</#if>
    },
</#if>
<#-- END Select Radio end 查询时，Radio、CheckBox、Switch全部转换为下拉框 -->
<#elseif field.controlType == "DTAE_PICKER" || field.controlType == "DTAE_TIME_PICKER">
    component: 'DatePicker',
    componentProps: {
      showTime: {
        defaultValue: dayjs().startOf('day'),
      },
      showNow: false,
<#if field.controlType == "DTAE_PICKER">
      format: 'YYYY-MM-DD',
<#else>
      format: 'YYYY-MM-DD HH:mm:ss',
</#if>
      style: 'width:100%;',
    },
<#elseif field.controlType == "TIME_PICKER">
    component: 'TimePicker',
    componentProps: {
      style: { width: '100%' },
    },
<#elseif field.controlType == "PROVINCE_CITY_AREA">
    component: 'Cascader',
    required: true,
    componentProps: {
      options: level,
      fieldNames: {
        label: 'name',
        value: 'code',
        children: 'children',
      },
    },
<#elseif field.controlType == "RATE">
    component: 'Rate',
<#else>
    component: 'Input',
</#if>
<#if tableShowType?? && tableShowType == "table_table">
    colProps: { span: 8 },
<#else>
    colProps: { span: 6 },
</#if>
  },
</#if>
</#list>
];
<#-- ----------  END 查询条件表单定义  ---------->
<#--获取主键-->
<#list table.fields as field>
    <#if field.keyFlag>
        <#assign keyPropertyName="${field.propertyName}"/>
    </#if>
</#list>
<#--判断是否存在主键-->
<#list formFields as field>
    <#if field?? && field.entityName == keyPropertyName>
        <#assign hasKeyFlag=true/>
    </#if>
</#list>
<#-- ----------  START 新增/修改表单定义  ---------->

// 新增/修改表单定义
export const formSchema: FormSchema[] = [
<#if !hasKeyFlag??>
  {
    field: '${keyPropertyName}',
    label: '主键',
    show: false,
    component: 'Input',
  },
</#if>
<#list formFields as field>
  {
    field: '${field.entityName}',
    label: '${field.comment}',
<#if hasKeyFlag?? && field.entityName == keyPropertyName>
    show: false,
</#if>
<#if field.defaultValue?? && field.defaultValue != "">
    defaultValue: '${field.defaultValue}',
</#if>
<#if field.controlType == "INPUT_TEXT">
    component: 'Input',
<#elseif field.controlType == "TEXTAREA">
    component: 'InputTextArea',
<#elseif field.controlType == "INPUT_PASSWORD">
    component: 'InputPassword',
<#elseif field.controlType == "INPUT_NUMBER">
    component: 'InputNumber',
<#elseif field.controlType == "API_TREE_SELECT" || field.controlType == "API_TREE">
<#if field.controlType == "API_TREE_SELECT">
    component: 'ApiTreeSelect',
<#elseif field.controlType == "API_TREE">
    component: 'ApiTree',
</#if>
<#if field.dictCode?? && field.dictCode == "API_DICT" && field.apiId?? >
    componentProps: {
      api: ${apiMap[field.apiId?string].apiMethod},
<#if apiMap[field.apiId?string].apiParams?? && apiMap[field.apiId?string].apiParams != "" >
      params: ${apiMap[field.apiId?string].apiParams},
</#if>
<#if field.controlType == "API_TREE">
      replaceFields: {
        title: '${apiMap[field.apiId?string].labelField}',
        key: '${apiMap[field.apiId?string].valueField}',
      },
<#else>
      fieldNames: {
        label: '${apiMap[field.apiId?string].labelField}',
        key: '${apiMap[field.apiId?string].valueField}',
        value: '${apiMap[field.apiId?string].valueField}',
      },
</#if>
      style: 'width:100%;',
      getPopupContainer: () => document.body,
    },
<#else>
    componentProps: {
      api: getDictBusinessCache,
      params: { dictCode: '${field.dictCode}' },
      resultField: 'dictList',
      fieldNames: {
        label: 'dictName',
        key: 'dictCode',
        value: 'dictCode',
      },
      style: 'width:100%;',
      getPopupContainer: () => document.body,
    },
</#if>
<#elseif field.controlType == "CASCADER">
    component: 'ApiCascader',
<#if field.dictCode?? && field.dictCode == "API_DICT" && field.apiId?? >
    componentProps: {
      api: ${apiMap[field.apiId?string].apiMethod},
      apiParamKey: 'parentId',
<#if apiMap[field.apiId?string].apiParams?? && apiMap[field.apiId?string].apiParams != "" >
      params: ${apiMap[field.apiId?string].apiParams},
</#if>
      dataField: 'list',
      labelField: '${apiMap[field.apiId?string].labelField}',
      valueField: '${apiMap[field.apiId?string].valueField}',
      initFetchParams: {
        parentId: 0,
      },
      style: 'width:100%;',
      isLeaf: (record) => {
        return record.isLeaf;
      },
    },
<#else>
    componentProps: {
      api: '请填写数据请求接口',
      apiParamKey: '请填写请求参数的key',
      params:  '请填写请求参数',
      dataField: 'list',
      labelField: '请填写label字段',
      valueField: '请填写value字段',
      initFetchParams: {
        // 填写初始化参数
      },
      style: 'width:100%;',
      isLeaf: (record) => {
        return record.isLeaf;
      },
    },
</#if>
<#-- BEGIN Select Radio -->
<#elseif field.controlType == "SELECT" || field.controlType == "SELECT_MULTI" || field.controlType == "RADIO">
<#if field.controlType == "SELECT" || field.controlType == "SELECT_MULTI">    component: 'ApiSelect',</#if>
<#if field.controlType == "RADIO">    component: 'ApiRadioGroup',</#if>
<#if field.dictCode?? && field.dictCode == "API_DICT" && field.apiId??>
    componentProps: {
      api: ${apiMap[field.apiId?string].apiMethod},
      resultField: 'list',
<#if apiMap[field.apiId?string].apiParams?? && apiMap[field.apiId?string].apiParams != "" >
      params: ${apiMap[field.apiId?string].apiParams},
</#if>
      labelField: '${apiMap[field.apiId?string].labelField}',
      valueField: '${apiMap[field.apiId?string].valueField}',
<#if field.controlType == "SELECT_MULTI">
    mode: 'multiple',
</#if>
    },
<#else>
    componentProps: {
      api: getDictBusinessCache,
      params: { dictCode: '${field.dictCode}' },
      resultField: 'dictList',
      labelField: 'dictName',
      valueField: 'dictCode',
<#if field.controlType == "SELECT_MULTI">
    mode: 'multiple',
</#if>
    },
</#if>
<#-- END Select Radio  -->
<#elseif field.controlType == "DTAE_PICKER" || field.controlType == "DTAE_TIME_PICKER">
    component: 'DatePicker',
    componentProps: {
      showTime: {
        defaultValue: dayjs().startOf('day'),
      },
      showNow: false,
<#if field.controlType == "DTAE_PICKER">
      format: 'YYYY-MM-DD',
<#else>
      format: 'YYYY-MM-DD HH:mm:ss',
</#if>
      style: 'width:100%;',
    },
<#elseif field.controlType == "TIME_PICKER">
    component: 'TimePicker',
    componentProps: {
      style: { width: '100%' },
    },
<#elseif field.controlType == "PROVINCE_CITY_AREA">
    component: 'Cascader',
    required: true,
    componentProps: {
      options: level,
      fieldNames: {
        label: 'name',
        value: 'code',
        children: 'children',
      },
    },
<#elseif field.controlType == "SWITCH">
    component: 'Switch',
<#elseif field.controlType == "CHECKBOX">
    component: 'CheckboxGroup',
<#elseif field.controlType == "RATE">
    component: 'Rate',
<#else>
    component: 'Input',
</#if>
<#-- ----------  START 是否进行状态处理  ---------->
<#if field?? && field.fieldUnique == true>
    dynamicRules: ({ model }) => {
  <#if (field.min?? || field.max?? || field.maxLength?? || field.minLength?? || field.validateValue?? || field.validateRegular??)>
      return [
    <#if field.required == true>
        { required: true, message: '${field.comment}不能为空！' },
    </#if>
    <#if field.min?? || field.max??>
        <#if  field.controlType != "DTAE_TIME_PICKER" && field.controlType != "DTAE_PICKER" && field.controlType != "TIME_PICKER" && field.controlType != "PROVINCE_CITY_AREA">
        { <#if field.min??>min: ${field.min}, </#if><#if field.max??>max: ${field.max}</#if>, message: '数值大小在<#if field.min??> ${field.min} 到</#if><#if field.max??> ${field.max}</#if> 之间', trigger: 'blur' },
        </#if>
    </#if>
    <#if field.maxLength?? || field.minLength??>
        <#if field.controlType != "DTAE_TIME_PICKER" && field.controlType != "DTAE_PICKER" && field.controlType != "TIME_PICKER" && field.controlType != "PROVINCE_CITY_AREA">
        { <#if field.minLength??>min: ${field.minLength}, </#if><#if field.maxLength??>max: ${field.maxLength}</#if>, message: '长度在<#if field.minLength??> ${field.minLength} 到</#if><#if field.maxLength??> ${field.maxLength}</#if> 个字符', trigger: 'blur' },
        </#if>
    </#if>
    <#if field.validateValue??>
        { pattern: /${field.validateValue?replace("\\\\","\\")}/, message: '${field.comment}格式错误' },
    </#if>
    <#if field.validateRegular??>
        { pattern: /${field.validateRegular?replace("\\\\","\\")}/, message: '${field.comment}格式错误' },
    </#if>
        {
          trigger: 'blur',
          message: '${field.comment}已存在',
          validator: (_, value) => {
            return new Promise((resolve, reject) => {
              const keyData = {
                id: model.id,
                checkField: '${field.fieldName}',
                checkValue: value,
              };
              check${entity}Exist(keyData)
                .then((response) => {
                  if (!response) {
                    reject('${field.comment}已存在');
                  } else {
                    resolve();
                  }
                })
                .catch((err) => {
                  reject(err.message || '验证失败');
                });
            });
          },
        },
      ];
    },
  <#else>
      return renderCheckExistRules(model, check${entity}Exist, '${field.fieldName}', '${field.comment}');
    },
  </#if>
<#elseif field?? && (field.controlType != "DTAE_TIME_PICKER" && field.controlType != "DTAE_PICKER" && field.controlType != "TIME_PICKER" && field.controlType != "PROVINCE_CITY_AREA") && (field.required == true || field.min?? || field.max?? || field.maxLength?? || field.minLength?? || field.validateValue?? || field.validateRegular??)>
    rules: [
    <#if field.required == true>
      { required: true, message: '${field.comment}不能为空！' },
    </#if>
    <#if field.min?? || field.max??>
      <#if field.controlType != "DTAE_TIME_PICKER" && field.controlType != "DTAE_PICKER" && field.controlType != "TIME_PICKER" && field.controlType != "PROVINCE_CITY_AREA">
      { <#if field.min??>min: ${field.min}, </#if><#if field.max??>max: ${field.max}</#if>, message: '数值大小在<#if field.min??> ${field.min} 到</#if><#if field.max??> ${field.max}</#if> 之间', trigger: 'blur' },
      </#if>
    </#if>
    <#if field.maxLength?? || field.minLength??>
      <#if field.controlType != "DTAE_TIME_PICKER" && field.controlType != "DTAE_PICKER" && field.controlType != "TIME_PICKER" && field.controlType != "PROVINCE_CITY_AREA">
      { <#if field.minLength??>min: ${field.minLength}, </#if><#if field.maxLength??>max: ${field.maxLength}</#if>, message: '长度在<#if field.minLength??> ${field.minLength} 到</#if><#if field.maxLength??> ${field.maxLength}</#if> 个字符', trigger: 'blur' },
      </#if>
    </#if>
    <#if field.validateValue??>
      { pattern: /${field.validateValue?replace("\\\\","\\")}/, message: '${field.comment}格式错误' },
    </#if>
    <#if field.validateRegular??>
      { pattern: /${field.validateRegular?replace("\\\\","\\")}/, message: '${field.comment}格式错误' },
    </#if>
    ],
<#elseif field?? && (field.controlType == "DTAE_TIME_PICKER" || field.controlType == "DTAE_PICKER" || field.controlType == "TIME_PICKER" || field.controlType == "PROVINCE_CITY_AREA") && (field.required == true || field.validateValue?? || field.validateRegular??)>
    rules: [
    <#if field.required == true>
        { required: true, message: '${field.comment}不能为空！' },
    </#if>
    <#if field.validateValue??>
        { pattern: /${field.validateValue?replace("\\\\","\\")}/, message: '${field.comment}格式错误' },
    </#if>
    <#if field.validateRegular??>
        { pattern: /${field.validateRegular?replace("\\\\","\\")}/, message: '${field.comment}格式错误' },
    </#if>
    ],
</#if>
<#-- ----------  END 是否进行状态处理  ---------->
    colProps: { span: ${config.formItemCol} },
  },
</#list>
];
<#-- ----------  END 新增/修改表单定义  ---------->