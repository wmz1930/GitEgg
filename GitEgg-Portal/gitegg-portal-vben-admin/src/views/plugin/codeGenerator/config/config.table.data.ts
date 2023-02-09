import { BasicColumn } from '/@/components/Table';
import { FormSchema } from '/@/components/Table';
import { getDataSourceAll } from '/@/api/plugin/codeGenerator/dataSource/data_source';
import { listGeneratorDict } from '/@/api/plugin/codeGenerator/dict/dict';

export const columns: BasicColumn[] = [
  {
    title: '序号',
    align: 'center',
    width: 80,
    dataIndex: 'id',
  },
  {
    title: '数据源',
    align: 'center',
    dataIndex: 'datasourceName',
  },
  {
    title: '模块名称',
    align: 'center',
    dataIndex: 'moduleName',
  },
  {
    title: '模块代码',
    align: 'center',
    dataIndex: 'moduleCode',
  },
  {
    title: '服务名称',
    align: 'center',
    dataIndex: 'serviceName',
  },
  {
    title: '表名',
    align: 'center',
    dataIndex: 'tableName',
  },

  {
    title: '表单类型',
    align: 'center',
    dataIndex: 'formType',
  },
  {
    title: '联表类型',
    align: 'center',
    dataIndex: 'tableType',
  },
  {
    title: '展示类型',
    align: 'center',
    dataIndex: 'tableShowType',
  },
];

export const searchFormSchema: FormSchema[] = [
  {
    field: 'datasourceId',
    label: '数据源',
    component: 'ApiSelect',
    componentProps: {
      api: getDataSourceAll,
      labelField: 'datasourceName',
      valueField: 'id',
      placeholder: '请选择数据源',
    },
    colProps: { span: 8 },
  },
  {
    field: 'moduleName',
    label: '模块名称',
    component: 'Input',
    componentProps: {
      placeholder: '请输入模块名称',
    },
    colProps: { span: 8 },
  },
  {
    field: 'tableName',
    label: '表名',
    component: 'Input',
    componentProps: {
      placeholder: '请输入表名',
    },
    colProps: { span: 8 },
  },
];

export const formSchema: FormSchema[] = [
  {
    field: 'serviceType',
    label: '服务类型',
    required: true,
    component: 'ApiSelect',
    defaultValue: 'cloud',
    componentProps: {
      api: listGeneratorDict,
      params: 'SERVICE_TYPE',
      resultField: 'list',
      // use name as label
      labelField: 'dictName',
      // use id as value
      valueField: 'dictCode',
      placeholder: '请选择后台服务类型',
    },
    colProps: { span: 12 },
  },
  {
    field: 'frontType',
    label: '前端版本',
    required: true,
    component: 'ApiSelect',
    defaultValue: 'vue3',
    componentProps: {
      api: listGeneratorDict,
      params: 'FRONT_TYPE',
      resultField: 'list',
      // use name as label
      labelField: 'dictName',
      // use id as value
      valueField: 'dictCode',
      placeholder: '请选择前端版本',
    },
    colProps: { span: 12 },
  },
  {
    field: 'divider-service',
    component: 'Divider',
    label: '服务配置',
    colProps: {
      span: 24,
    },
    componentProps: {
      style: { 'margin-top': '6px' },
    },
  },
  {
    field: 'id',
    label: '主键',
    show: false,
    component: 'Input',
  },
  {
    field: 'datasourceId',
    label: '数据源',
    component: 'ApiSelect',
    componentProps: {
      api: getDataSourceAll,
      labelField: 'datasourceName',
      valueField: 'id',
      placeholder: '请选择数据源',
    },
    required: true,
    colProps: { span: 12 },
  },
  {
    field: 'tableName',
    label: '表名',
    required: true,
    component: 'Select',
    componentProps: {
      placeholder: '请选择表名',
    },
    colProps: { span: 12 },
  },
  {
    field: 'serviceName',
    label: '服务名称',
    required: true,
    component: 'Input',
    helpMessage: [
      '和微服务名称保持一致',
      '前端页面请求微服务接口的前缀',
      '数据权限表resource_url字段的前缀',
    ],
    componentProps: {
      placeholder: '例：gitegg-service-system',
    },
    colProps: { span: 12 },
  },
  {
    field: 'moduleName',
    label: '模块名称',
    required: true,
    component: 'Input',
    helpMessage: ['生成代码时的模块名称', '菜单名称'],
    componentProps: {
      placeholder: '输入模块名称，例：系统管理',
    },
    colProps: { span: 12 },
  },
  {
    field: 'moduleCode',
    label: '模块代码',
    required: true,
    component: 'Input',
    helpMessage: ['生成代码时的包名', '多个表可能在同一个包下，这里可以统一设置包名'],
    componentProps: {
      placeholder: '输入模块代码，例：system',
    },
    colProps: { span: 12 },
  },
  {
    field: 'tableAlias',
    label: '表别名',
    required: true,
    component: 'Input',
    helpMessage: ['用于多表查询时使用的表别名'],
    componentProps: {
      placeholder: '输入表别名，例：user',
    },
    colProps: { span: 12 },
  },
  {
    field: 'tablePrefix',
    label: '表前缀',
    required: true,
    component: 'Input',
    helpMessage: ['生成代码时，会去掉表前缀，通过实际意义的表名生成实体类'],
    componentProps: {
      placeholder: '输入表前缀，例：t_sys_',
    },
    colProps: { span: 12 },
  },
  {
    field: 'parentPackage',
    label: '父级包名',
    required: true,
    component: 'Input',
    helpMessage: ['模块包名的上级包名'],
    componentProps: {
      placeholder: '例：com.gitegg.system',
    },
    colProps: { span: 12 },
  },
  {
    field: 'domainName',
    label: '实体名称',
    required: true,
    component: 'Input',
    helpMessage: ['界面展示名称'],
    componentProps: {
      placeholder: '用于该功能界面展示，及操作提示的名称',
    },
    colProps: { span: 12 },
  },
  {
    field: 'divider-front',
    component: 'Divider',
    label: '界面配置',
    colProps: {
      span: 24,
    },
  },
  {
    field: 'formType',
    label: '表单类型',
    required: true,
    component: 'ApiSelect',
    defaultValue: 'Modal',
    componentProps: {
      api: listGeneratorDict,
      params: 'FORM_TYPE',
      resultField: 'list',
      // use name as label
      labelField: 'dictName',
      // use id as value
      valueField: 'dictCode',
      placeholder: '请选择表单展示类型',
    },
    colProps: { span: 12 },
  },
  {
    field: 'formItemCol',
    label: '表单列数',
    required: true,
    component: 'ApiSelect',
    defaultValue: '24',
    componentProps: {
      api: listGeneratorDict,
      params: 'FORM_COL',
      resultField: 'list',
      // use name as label
      labelField: 'dictName',
      // use id as value
      valueField: 'dictCode',
      placeholder: '请选择表单列数',
    },
    colProps: { span: 12 },
  },
  {
    field: 'tableShowType',
    label: '数据展示',
    required: true,
    component: 'ApiSelect',
    defaultValue: 'table',
    componentProps: {
      api: listGeneratorDict,
      params: 'TABLE_DATA_SHOW_TYPE',
      resultField: 'list',
      // use name as label
      labelField: 'dictName',
      // use id as value
      valueField: 'dictCode',
      placeholder: '请选择数据展示类型',
    },
    colProps: { span: 12 },
  },
  {
    field: 'leftTreeType',
    label: '左树类型',
    required: true,
    component: 'ApiSelect',
    componentProps: {
      api: listGeneratorDict,
      params: 'TREE_TYPE',
      resultField: 'list',
      // use name as label
      labelField: 'dictName',
      // use id as value
      valueField: 'dictCode',
      placeholder: '请选择左树类型',
    },
    dynamicDisabled: ({ values }) => {
      return (
        values.tableShowType !== 'left_tree_table' &&
        values.tableShowType !== 'left_tree_tree_table' &&
        values.tableShowType !== 'tree_tree'
      );
    },
    colProps: { span: 12 },
    dynamicRules: ({ values }) => {
      if (
        values.tableShowType === 'left_tree_table' ||
        values.tableShowType === 'left_tree_tree_table' ||
        values.tableShowType === 'tree_tree'
      ) {
        return [
          {
            required: true,
            message: '请选择左树类型',
          },
        ];
      }
      return [];
    },
  },
  {
    field: 'controllerPath',
    label: 'Controller请求路径',
    labelWidth: 150,
    required: true,
    component: 'Input',
    componentProps: {
      placeholder: '输入controller请求路径，例：/system/user',
    },
  },
  {
    field: 'frontCodeDir',
    label: '页面文件目录自定义',
    labelWidth: 150,
    required: true,
    component: 'Input',
    componentProps: {
      placeholder: '存放.vue页面文件的目录名称，用于区分不同系统，例如：system',
    },
  },
  {
    field: 'divider-code',
    component: 'Divider',
    label: '生成配置',
    colProps: {
      span: 24,
    },
  },
  {
    field: 'codeType',
    label: '生成类型',
    required: true,
    component: 'ApiRadioGroup',
    defaultValue: 'ALL',
    componentProps: {
      api: listGeneratorDict,
      params: 'CODE_TYPE',
      resultField: 'list',
      // use name as label
      labelField: 'dictName',
      // use id as value
      valueField: 'dictCode',
    },
    colProps: { span: 12 },
  },
  {
    field: 'codeSaveType',
    label: '保存方式',
    required: true,
    component: 'ApiRadioGroup',
    defaultValue: 'SERVER_DOWNLOAD',
    componentProps: {
      api: listGeneratorDict,
      params: 'CODE_SAVE_TYPE',
      resultField: 'list',
      // use name as label
      labelField: 'dictName',
      // use id as value
      valueField: 'dictCode',
    },
    colProps: { span: 12 },
  },
  {
    field: 'serviceCodePath',
    label: '后端代码保存路径',
    labelWidth: 150,
    ifShow(renderCallbackParams) {
      if (
        renderCallbackParams?.values?.codeType !== 'FRONT' &&
        renderCallbackParams?.values?.codeSaveType === 'LOCAL_OVERLAY'
      ) {
        return true;
      }
      return false;
    },
    dynamicRules: ({ values }) => {
      if (values?.codeType !== 'FRONT' && values?.codeSaveType === 'LOCAL_OVERLAY') {
        return [
          {
            required: true,
            message: '请输入后端代码保存路径',
          },
        ];
      }
      return [];
    },
    component: 'Input',
    componentProps: {
      placeholder: '输入后端代码存放路径',
    },
  },
  {
    field: 'frontCodePath',
    label: '前端代码保存路径',
    labelWidth: 150,
    ifShow(renderCallbackParams) {
      if (
        renderCallbackParams?.values?.codeType !== 'SERVICE' &&
        renderCallbackParams?.values?.codeSaveType === 'LOCAL_OVERLAY'
      ) {
        return true;
      }
      return false;
    },
    dynamicRules: ({ values }) => {
      if (values?.codeType !== 'SERVICE' && values?.codeSaveType === 'LOCAL_OVERLAY') {
        return [
          {
            required: true,
            message: '请输入前端代码保存路径',
          },
        ];
      }
      return [];
    },
    component: 'Input',
    componentProps: {
      placeholder: '输入前端代码存放路径',
    },
  },
  {
    label: '状态处理',
    field: 'statusHandling',
    required: true,
    component: 'ApiRadioGroup',
    defaultValue: 'true',
    helpMessage: ['如果表中有状态字段，会在数据表中展示状态处理按钮'],
    componentProps: {
      api: listGeneratorDict,
      params: 'TRUE_FALSE',
      resultField: 'list',
      // use name as label
      labelField: 'dictName',
      // use id as value
      valueField: 'dictCode',
    },
    colProps: { span: 12 },
  },
  {
    field: 'queryReuse',
    label: '查询复用',
    component: 'ApiRadioGroup',
    defaultValue: 'true',
    helpMessage: ['列表查询和单个记录查询是否使用相同代码'],
    componentProps: {
      api: listGeneratorDict,
      params: 'TRUE_FALSE',
      resultField: 'list',
      // use name as label
      labelField: 'dictName',
      // use id as value
      valueField: 'dictCode',
    },
    colProps: { span: 12 },
  },
  {
    label: '支持导出',
    field: 'exportFlag',
    required: true,
    component: 'ApiRadioGroup',
    defaultValue: 'true',
    helpMessage: ['界面展示数据导出按钮'],
    componentProps: {
      api: listGeneratorDict,
      params: 'TRUE_FALSE',
      resultField: 'list',
      // use name as label
      labelField: 'dictName',
      // use id as value
      valueField: 'dictCode',
    },
    colProps: { span: 12 },
  },
  {
    label: '支持导入',
    field: 'importFlag',
    required: true,
    component: 'ApiRadioGroup',
    defaultValue: 'true',
    helpMessage: ['界面展示数据导入按钮'],
    componentProps: {
      api: listGeneratorDict,
      params: 'TRUE_FALSE',
      resultField: 'list',
      // use name as label
      labelField: 'dictName',
      // use id as value
      valueField: 'dictCode',
    },
    colProps: { span: 12 },
  },
  {
    label: '联表类型',
    field: 'tableType',
    required: true,
    defaultValue: 'single',
    component: 'ApiRadioGroup',
    helpMessage: ['该模块是单表数据还是多表数据'],
    componentProps: {
      api: listGeneratorDict,
      params: 'TABLE_DATA_TYPE',
      resultField: 'list',
      // use name as label
      labelField: 'dictName',
      // use id as value
      valueField: 'dictCode',
      placeholder: '请选择联表类型',
    },
    colProps: { span: 12 },
  },
  {
    field: 'extendsFlag',
    label: '是否继承',
    component: 'ApiRadioGroup',
    defaultValue: 'true',
    helpMessage: ['主子表类型时，DTO类是否抽取公共字段供其他DTO继承'],
    componentProps: {
      api: listGeneratorDict,
      params: 'TRUE_FALSE',
      resultField: 'list',
      // use name as label
      labelField: 'dictName',
      // use id as value
      valueField: 'dictCode',
    },
    ifShow(renderCallbackParams) {
      if (renderCallbackParams?.values?.tableType === 'main_sub') {
        return true;
      }
      return false;
    },
    colProps: { span: 12 },
  },
];
